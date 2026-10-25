package com.maxim.bodymarket;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/** Вся логика: меню, продажа, выкуп, эффекты, болезни. */
public final class Market {
    public static final Identifier OPEN_MENU = new Identifier(BodyMarket.MODID, "open_menu");
    public static final Identifier TRADE = new Identifier(BodyMarket.MODID, "trade");
    public static final Identifier DISEASE = new Identifier(BodyMarket.MODID, "disease");

    /** Режимы сделки (пакет от клиента). */
    public static final int SELL = 0, BUY = 1, RANDOM = 2, REROLL = 3;

    private static final Random RND = new Random();
    private static final int ROLL_TICKS = 6000; // 5 минут
    private static final int OFFER_COUNT = 8;

    private static final String[] GREETS = {
        "Опа, свежий клиент! Что продаём?",
        "Заходи, брат. Органы дорого, совесть бесплатно.",
        "Я Максим. Я большой, я лысый, я покупаю.",
        "Сегодня скидки на твоё здоровье!",
        "Цены сегодня плавают, как Джетт в дыму."
    };
    private static final String[] OUTROS = {
        "Приходи ещё, у тебя много всего ценного.",
        "Сделка! Я бы пожал руку, но ты её, может, уже продал.",
        "Красавчик. Следующий!",
        "Не рыдай, это всего лишь тело."
    };

    /** Текущие случайные предложения Максима для каждого игрока. */
    private static final Map<UUID, LinkedHashMap<String, Integer>> OFFERS = new HashMap<>();

    private Market() {}

    public static void say(ServerPlayerEntity p, String text) {
        p.sendMessage(Text.literal("§6[Максим] §r" + text));
    }

    public static void onJoin(ServerPlayerEntity p) {
        BodyData data = BodyData.get(p.getServer());
        BodyData.PlayerBody b = data.of(p.getUuid());
        if (!b.egg) {
            b.egg = true;
            data.markDirty();
            p.giveItemStack(new ItemStack(BodyMarket.MAXIM_EGG));
            say(p, "Привет! Я Максим. Поставь меня яйцом на землю, и я буду покупать твои органы за изумруды.");
        }
        sendDisease(p, b.disease);
    }

    // ---------- Меню ----------

    public static void openMenu(ServerPlayerEntity p) {
        rerollOffers(p);
        sendMenu(p, GREETS[RND.nextInt(GREETS.length)]);
    }

    private static void rerollOffers(ServerPlayerEntity p) {
        BodyData.PlayerBody b = BodyData.get(p.getServer()).of(p.getUuid());
        List<Part> free = new ArrayList<>();
        for (Part part : Part.values()) if (!b.parts.contains(part.id)) free.add(part);
        Collections.shuffle(free, RND);
        LinkedHashMap<String, Integer> offers = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(OFFER_COUNT, free.size()); i++) {
            Part part = free.get(i);
            // случайная цена: от 60% до 150% базовой
            int price = (int) Math.round(part.price * (0.6 + RND.nextDouble() * 0.9));
            offers.put(part.id, Math.max(1, Math.min(price, 64)));
        }
        OFFERS.put(p.getUuid(), offers);
    }

    private static void sendMenu(ServerPlayerEntity p, String greeting) {
        BodyData.PlayerBody b = BodyData.get(p.getServer()).of(p.getUuid());
        Map<String, Integer> offers = OFFERS.getOrDefault(p.getUuid(), new LinkedHashMap<>());
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(greeting);
        buf.writeVarInt(countEmeralds(p));
        buf.writeVarInt(offers.size());
        offers.forEach((id, price) -> {
            buf.writeString(id);
            buf.writeVarInt(price);
        });
        List<Part> sold = new ArrayList<>();
        for (Part part : Part.values()) if (b.parts.contains(part.id)) sold.add(part);
        buf.writeVarInt(sold.size());
        for (Part part : sold) {
            buf.writeString(part.id);
            buf.writeVarInt(part.price * 3);
        }
        ServerPlayNetworking.send(p, OPEN_MENU, buf);
    }

    public static void sendDisease(ServerPlayerEntity p, String id) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(id == null ? "" : id);
        ServerPlayNetworking.send(p, DISEASE, buf);
    }

    // ---------- Сделки ----------

    public static void handleTrade(ServerPlayerEntity p, int mode, String id) {
        boolean near = !p.getWorld()
            .getEntitiesByClass(MaximEntity.class, p.getBoundingBox().expand(8), e -> true).isEmpty();
        if (!near) {
            p.sendMessage(Text.literal("§cМаксима нет рядом. Подойди ближе."));
            return;
        }
        BodyData data = BodyData.get(p.getServer());
        BodyData.PlayerBody b = data.of(p.getUuid());
        LinkedHashMap<String, Integer> offers = OFFERS.computeIfAbsent(p.getUuid(), k -> new LinkedHashMap<>());

        switch (mode) {
            case REROLL -> {
                rerollOffers(p);
                sendMenu(p, "Вот свежие предложения. Цены другие, тело то же.");
                return;
            }
            case RANDOM -> {
                if (offers.isEmpty()) {
                    say(p, "Нечего продавать. Ты и так уже почти всё продал.");
                    sendMenu(p, "Пусто!");
                    return;
                }
                List<String> ids = new ArrayList<>(offers.keySet());
                id = ids.get(RND.nextInt(ids.size()));
                sell(p, data, b, offers, id, true);
            }
            case SELL -> sell(p, data, b, offers, id, false);
            case BUY -> buyBack(p, data, b, id);
            default -> { return; }
        }
        applyEffects(p, b);
        sendMenu(p, OUTROS[RND.nextInt(OUTROS.length)]);
    }

    private static void sell(ServerPlayerEntity p, BodyData data, BodyData.PlayerBody b,
                             Map<String, Integer> offers, String id, boolean random) {
        Part part = Part.byId(id);
        Integer price = offers.get(id);
        if (part == null || price == null || b.parts.contains(id)) {
            say(p, "Такого предложения нет. Обнови список.");
            return;
        }
        b.parts.add(id);
        offers.remove(id);
        data.markDirty();
        p.giveItemStack(new ItemStack(Items.EMERALD, price));
        say(p, (random ? "Рандом выбрал за тебя! " : "") + "Продано: " + part.ru + " за " + price + " изумр.");
        say(p, part.msg);
        say(p, part.joke);
    }

    private static void buyBack(ServerPlayerEntity p, BodyData data, BodyData.PlayerBody b, String id) {
        Part part = Part.byId(id);
        if (part == null || !b.parts.contains(id)) {
            say(p, "Ты это не продавал, шутник.");
            return;
        }
        int cost = part.price * 3;
        if (countEmeralds(p) < cost) {
            say(p, "Не хватает изумрудов. Нужно " + cost + ". Обратно дороже, брат.");
            return;
        }
        takeEmeralds(p, cost);
        b.parts.remove(id);
        data.markDirty();
        say(p, "Держи свой(ю) «" + part.ru + "» обратно. Приходи ещё!");
    }

    public static int reset(ServerCommandSource src) {
        ServerPlayerEntity p = src.getPlayer();
        if (p == null) return 0;
        BodyData data = BodyData.get(p.getServer());
        BodyData.PlayerBody b = data.of(p.getUuid());
        b.parts.clear();
        b.disease = "";
        b.nextRoll = 0;
        data.markDirty();
        p.clearStatusEffects();
        sendDisease(p, "");
        p.sendMessage(Text.literal("§aВсё вернул: части тела и болезни сброшены."));
        return 1;
    }

    // ---------- Тики: болезни и эффекты ----------

    /** Раз в секунду. */
    public static void tick(MinecraftServer server) {
        BodyData data = BodyData.get(server);
        long now = server.getOverworld().getTime();
        for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
            BodyData.PlayerBody b = data.of(p.getUuid());
            if (b.nextRoll == 0) {
                b.nextRoll = now + ROLL_TICKS;
                data.markDirty();
            } else if (now >= b.nextRoll) {
                Disease d;
                do {
                    d = Disease.values()[RND.nextInt(Disease.values().length)];
                } while (d.id.equals(b.disease));
                b.disease = d.id;
                b.nextRoll = now + ROLL_TICKS;
                data.markDirty();
                sendDisease(p, d.id);
                p.sendMessage(Text.literal("§c☣ Ты заболел: " + d.ru + " §7(Valorant: " + d.agent + ")"));
                p.sendMessage(Text.literal("§7" + d.desc + " Следующая болезнь через 5 минут."));
            }
            applyEffects(p, b);
            Disease cur = Disease.byId(b.disease);
            if (cur != null) special(p, cur, now);
        }
    }

    private static void eff(ServerPlayerEntity p, StatusEffect e, int duration, int amp) {
        p.addStatusEffect(new StatusEffectInstance(e, duration, amp, true, false, true));
    }

    /** Периодические симптомы. Ничего из этого не убивает. */
    private static void special(ServerPlayerEntity p, Disease d, long now) {
        long sec = now / 20;
        switch (d) {
            case NARCOLEPSY -> {
                // экран гаснет на клиенте (DiseaseFx), здесь — «засыпаешь на ходу»
                if (sec % 5 == 0) eff(p, StatusEffects.SLOWNESS, 40, 6);
            }
            case ASTHMA -> {
                if (sec % 20 == 0) {
                    eff(p, StatusEffects.SLOWNESS, 80, 2);
                    eff(p, StatusEffects.HUNGER, 100, 0);
                    p.playSound(SoundEvents.ENTITY_FOX_SNIFF, 1.0f, 0.6f);
                }
            }
            case NIGHTBLIND -> {
                long t = p.getWorld().getTimeOfDay() % 24000;
                if (t >= 13000 && t <= 23000) eff(p, StatusEffects.DARKNESS, 100, 0);
            }
            case AMNESIA -> {
                if (sec % 15 == 0) {
                    int slot = RND.nextInt(9);
                    p.getInventory().selectedSlot = slot;
                    p.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(slot));
                }
            }
            case HICCUPS -> {
                if (sec % 10 == 0 && p.isOnGround()) {
                    p.addVelocity(0, 0.45, 0);
                    p.velocityModified = true;
                    p.playSound(SoundEvents.ENTITY_FROG_AMBIENT, 1.0f, 1.4f);
                }
            }
            case VERTIGO -> {
                if (sec % 40 == 0) eff(p, StatusEffects.NAUSEA, 160, 0);
            }
            case DIABETES -> {
                if (sec % 60 == 0) {
                    eff(p, StatusEffects.WEAKNESS, 160, 2);
                    eff(p, StatusEffects.SLOWNESS, 160, 1);
                }
            }
            case COLD -> {
                if (sec % 25 == 0) {
                    eff(p, StatusEffects.SLOWNESS, 30, 3);
                    p.playSound(SoundEvents.ENTITY_FOX_SNIFF, 1.0f, 1.5f);
                }
            }
            case ARRHYTHMIA -> {
                if (sec % 30 == 0) eff(p, StatusEffects.SLOWNESS, 30, 3);
            }
            default -> {}
        }
    }

    public static void applyEffects(ServerPlayerEntity p, BodyData.PlayerBody b) {
        Map<StatusEffect, Integer> fx = new LinkedHashMap<>();

        for (String id : b.parts) {
            Part part = Part.byId(id);
            if (part == null) continue;
            for (Part.Fx f : part.fx) fx.merge(f.effect(), f.amp(), Math::max);
        }
        // комбо: потерял обе части
        if (b.parts.contains("left_eye") && b.parts.contains("right_eye")) {
            fx.merge(StatusEffects.BLINDNESS, 0, Math::max);
        }
        if (b.parts.contains("left_ear") && b.parts.contains("right_ear")) {
            fx.merge(StatusEffects.SLOWNESS, 1, Math::max);
        }
        if (b.parts.contains("left_arm") && b.parts.contains("right_arm")) {
            fx.merge(StatusEffects.MINING_FATIGUE, 4, Math::max);
            fx.merge(StatusEffects.WEAKNESS, 4, Math::max);
        }
        if (b.parts.contains("left_leg") && b.parts.contains("right_leg")) {
            fx.merge(StatusEffects.SLOWNESS, 3, Math::max);
            fx.merge(StatusEffects.JUMP_BOOST, -128, Math::max); // нельзя прыгать
        }

        Disease d = Disease.byId(b.disease);
        if (d != null) for (Part.Fx f : d.fx) fx.merge(f.effect(), f.amp(), Math::max);

        // защита: при малом здоровье убираем всё, что может добить
        if (p.getHealth() <= 4.0f) {
            fx.remove(StatusEffects.POISON);
            fx.remove(StatusEffects.HUNGER);
        }

        for (Map.Entry<StatusEffect, Integer> e : fx.entrySet()) {
            StatusEffectInstance cur = p.getStatusEffect(e.getKey());
            if (cur == null || cur.getDuration() <= 40 || cur.getAmplifier() != e.getValue()) {
                p.addStatusEffect(new StatusEffectInstance(e.getKey(), 100, e.getValue(), true, false, true));
            }
        }
    }

    private static int countEmeralds(ServerPlayerEntity p) {
        int n = 0;
        PlayerInventory inv = p.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack s = inv.getStack(i);
            if (s.isOf(Items.EMERALD)) n += s.getCount();
        }
        return n;
    }

    private static void takeEmeralds(ServerPlayerEntity p, int amount) {
        PlayerInventory inv = p.getInventory();
        for (int i = 0; i < inv.size() && amount > 0; i++) {
            ItemStack s = inv.getStack(i);
            if (s.isOf(Items.EMERALD)) {
                int t = Math.min(amount, s.getCount());
                s.decrement(t);
                amount -= t;
            }
        }
    }
}
