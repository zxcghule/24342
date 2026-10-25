package com.maxim.bodymarket.client;

import com.maxim.bodymarket.Market;
import com.maxim.bodymarket.Part;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.List;

/** Меню торговца Максима. */
public class MaximScreen extends Screen {
    public record Entry(String id, int price) {}

    private static final int PER_PAGE = 8;

    private final String greeting;
    private final int emeralds;
    private final List<Entry> offers;
    private final List<Entry> sold;

    boolean buyTab = false;
    int page = 0;

    public MaximScreen(String greeting, int emeralds, List<Entry> offers, List<Entry> sold) {
        super(Text.literal("Максим"));
        this.greeting = greeting;
        this.emeralds = emeralds;
        this.offers = offers;
        this.sold = sold;
    }

    private List<Entry> current() {
        return buyTab ? sold : offers;
    }

    private void send(int mode, String id) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(mode);
        buf.writeString(id);
        ClientPlayNetworking.send(Market.TRADE, buf);
    }

    private void rebuild() {
        clearChildren();
        init();
    }

    @Override
    protected void init() {
        int cx = width / 2;
        int top = height / 2 - 90;
        List<Entry> list = current();
        int maxPage = Math.max(0, (list.size() - 1) / PER_PAGE);
        if (page > maxPage) page = maxPage;

        addDrawableChild(ButtonWidget.builder(Text.literal(buyTab ? "Продать" : "» Продать «"),
            b -> { buyTab = false; page = 0; rebuild(); }).dimensions(cx - 160, top, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(buyTab ? "» Выкупить «" : "Выкупить"),
            b -> { buyTab = true; page = 0; rebuild(); }).dimensions(cx - 55, top, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Закрыть"),
            b -> close()).dimensions(cx + 60, top, 100, 20).build());

        int from = page * PER_PAGE;
        for (int i = 0; i < PER_PAGE && from + i < list.size(); i++) {
            Entry e = list.get(from + i);
            Part part = Part.byId(e.id());
            if (part == null) continue;
            int col = i % 2;
            int row = i / 2;
            String label = (buyTab ? "Выкупить: " : "") + part.ru + " — " + e.price() + " изумр.";
            ButtonWidget btn = ButtonWidget.builder(Text.literal(label),
                    b -> send(buyTab ? Market.BUY : Market.SELL, e.id()))
                .dimensions(cx - 160 + col * 165, top + 28 + row * 24, 155, 20)
                .tooltip(Tooltip.of(Text.literal(part.msg)))
                .build();
            addDrawableChild(btn);
        }

        int bottom = top + 28 + 4 * 24 + 4;
        addDrawableChild(ButtonWidget.builder(Text.literal("<"), b -> { page = Math.max(0, page - 1); rebuild(); })
            .dimensions(cx - 160, bottom, 30, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(">"), b -> { page = Math.min(maxPage, page + 1); rebuild(); })
            .dimensions(cx + 130, bottom, 30, 20).build());
        if (!buyTab) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Случайная часть"), b -> send(Market.RANDOM, ""))
                .dimensions(cx - 125, bottom, 120, 20)
                .tooltip(Tooltip.of(Text.literal("Рандом выберет за тебя. Максим любит риск!")))
                .build());
            addDrawableChild(ButtonWidget.builder(Text.literal("Новые предложения"), b -> send(Market.REROLL, ""))
                .dimensions(cx + 5, bottom, 120, 20).build());
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);
        int cx = width / 2;
        int top = height / 2 - 90;
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§6Максим: §f" + greeting), cx, top - 30, 0xFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§aИзумрудов у тебя: " + emeralds), cx, top - 16, 0xFFFFFF);
        if (current().isEmpty()) {
            ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal(buyTab ? "Ты пока ничего не продавал." : "Продавать больше нечего!"), cx, top + 60, 0xAAAAAA);
        }
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
