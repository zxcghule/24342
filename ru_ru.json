package com.maxim.bodymarket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class BodyMarket implements ModInitializer {
    public static final String MODID = "bodymarket";

    public static final EntityType<MaximEntity> MAXIM = Registry.register(
        Registries.ENTITY_TYPE, new Identifier(MODID, "maxim"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MaximEntity::new)
            .dimensions(EntityDimensions.fixed(0.8f, 2.4f)).build());

    public static final Item MAXIM_EGG = Registry.register(
        Registries.ITEM, new Identifier(MODID, "maxim_spawn_egg"),
        new SpawnEggItem(MAXIM, 0x3b2a1a, 0xd9a066, new FabricItemSettings()));

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(MAXIM, MaximEntity.createAttributes());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(e -> e.add(MAXIM_EGG));

        // яйцо Максима выдаётся при первом входе в мир
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Market.onJoin(handler.getPlayer()));

        // раз в секунду: болезни и эффекты
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % 20 == 0) Market.tick(server);
        });

        // клиент нажал кнопку в меню Максима
        ServerPlayNetworking.registerGlobalReceiver(Market.TRADE, (server, player, handler, buf, responseSender) -> {
            int mode = buf.readVarInt();
            String id = buf.readString();
            server.execute(() -> Market.handleTrade(player, mode, id));
        });

        // бессонница: нельзя спать
        EntitySleepEvents.ALLOW_SLEEPING.register((player, pos) -> {
            if (player instanceof ServerPlayerEntity sp) {
                BodyData.PlayerBody b = BodyData.get(sp.getServer()).of(sp.getUuid());
                if ("insomnia".equals(b.disease)) {
                    sp.sendMessage(Text.literal("§cБессонница: ты не можешь уснуть."), true);
                    return PlayerEntity.SleepFailureReason.OTHER_PROBLEM;
                }
            }
            return null;
        });

        // продал язык: нельзя писать в чат
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            BodyData.PlayerBody b = BodyData.get(sender.getServer()).of(sender.getUuid());
            if (b.parts.contains("tongue")) {
                sender.sendMessage(Text.literal("§6[Максим] §rТы продал язык, молчи! Сообщение не отправлено."));
                return false;
            }
            return true;
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(CommandManager.literal("maxim")
                .then(CommandManager.literal("reset")
                    .requires(src -> src.hasPermissionLevel(2))
                    .executes(c -> Market.reset(c.getSource())))));
    }
}
