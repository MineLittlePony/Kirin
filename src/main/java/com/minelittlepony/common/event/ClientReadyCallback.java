package com.minelittlepony.common.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

/**
 * Callback for the first tick on the client. Call {@link Handler#register()}
 * to register
 */
public interface ClientReadyCallback {

    Event<ClientReadyCallback> EVENT = EventFactory.createArrayBacked(ClientReadyCallback.class, listeners -> client -> {
        for (ClientReadyCallback event : listeners) {
            event.onClientPostInit(client);
        }
    });

    void onClientPostInit(MinecraftClient client);

    class Handler implements ClientTickEvents.EndTick  {

        private static Handler instance;

        private boolean firstTick = true;

        private Handler() {
        }

        /**
         * Call this at least once to register the base event.
         */
        public static void register() {
            // make sure to only register once
            if (instance == null) {
                instance = new Handler();
                ClientTickEvents.END_CLIENT_TICK.register(instance);
            }
        }

        @Override
        public void onEndTick(MinecraftClient client) {
            if (firstTick) {
                ClientReadyCallback.EVENT.invoker().onClientPostInit(client);
                firstTick = false;
            }
        }
    }
}
