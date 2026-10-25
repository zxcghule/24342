package com.maxim.bodymarket.client;

import com.maxim.bodymarket.BodyMarket;
import com.maxim.bodymarket.Market;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import java.util.ArrayList;
import java.util.List;

public class BodyMarketClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(BodyMarket.MAXIM, MaximRenderer::new);

        // сервер открывает меню Максима
        ClientPlayNetworking.registerGlobalReceiver(Market.OPEN_MENU, (client, handler, buf, sender) -> {
            String greeting = buf.readString();
            int emeralds = buf.readVarInt();
            List<MaximScreen.Entry> offers = new ArrayList<>();
            int n = buf.readVarInt();
            for (int i = 0; i < n; i++) offers.add(new MaximScreen.Entry(buf.readString(), buf.readVarInt()));
            List<MaximScreen.Entry> sold = new ArrayList<>();
            int m = buf.readVarInt();
            for (int i = 0; i < m; i++) sold.add(new MaximScreen.Entry(buf.readString(), buf.readVarInt()));

            client.execute(() -> {
                boolean tab = false;
                int page = 0;
                if (client.currentScreen instanceof MaximScreen old) {
                    tab = old.buyTab;
                    page = old.page;
                }
                MaximScreen screen = new MaximScreen(greeting, emeralds, offers, sold);
                screen.buyTab = tab;
                screen.page = page;
                client.setScreen(screen);
            });
        });

        // сервер сообщает текущую болезнь
        ClientPlayNetworking.registerGlobalReceiver(Market.DISEASE, (client, handler, buf, sender) -> {
            String id = buf.readString();
            client.execute(() -> ClientState.disease = id);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientState.disease = "");
        HudRenderCallback.EVENT.register((ctx, tickDelta) -> DiseaseFx.render(ctx));
        ClientTickEvents.END_CLIENT_TICK.register(DiseaseFx::tick);
    }
}
