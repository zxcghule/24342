package com.maxim.bodymarket.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.Random;

/** Эффекты болезней на экране игрока (только визуал). */
public final class DiseaseFx {
    private static final Random RND = new Random();

    private DiseaseFx() {}

    private static void overlay(DrawContext ctx, double alpha, int rgb) {
        int a = (int) Math.max(0, Math.min(255, alpha * 255));
        if (a <= 0) return;
        ctx.fill(0, 0, ctx.getScaledWindowWidth(), ctx.getScaledWindowHeight(), (a << 24) | (rgb & 0xFFFFFF));
    }

    public static void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || ClientState.disease.isEmpty()) return;
        long t = mc.world.getTime();

        switch (ClientState.disease) {
            case "narcolepsy" -> {
                // каждые 5 секунд (100 тиков) экран гаснет на 2 секунды
                int phase = (int) (t % 100);
                if (phase < 40) {
                    double a = phase < 10 ? phase / 10.0 : phase > 30 ? (40 - phase) / 10.0 : 1.0;
                    overlay(ctx, a, 0x000000);
                }
            }
            case "migraine" -> overlay(ctx, 0.30 + 0.25 * Math.sin(t * 0.12), 0x101010);
            case "phoenix" -> overlay(ctx, 0.12 + 0.06 * Math.sin(t * 0.08), 0xFF2000);
            case "colorblind" -> overlay(ctx, 0.55, 0x808080);
            case "arrhythmia" -> {
                // быстрый и неровный «удар сердца»
                int beat = (int) (t % 30);
                if (beat < 4) overlay(ctx, 0.25 - beat * 0.05, 0xFF0000);
                if (beat >= 12 && beat < 14) overlay(ctx, 0.12, 0xFF0000);
            }
            default -> {}
        }
    }

    /** Тряска камеры. */
    public static void tick(MinecraftClient mc) {
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.currentScreen != null) return;
        if ("tremor".equals(ClientState.disease)) {
            p.setYaw(p.getYaw() + (RND.nextFloat() - 0.5f) * 2.4f);
            float pitch = p.getPitch() + (RND.nextFloat() - 0.5f) * 1.6f;
            p.setPitch(Math.max(-90f, Math.min(90f, pitch)));
        }
    }
}
