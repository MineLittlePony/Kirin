package com.minelittlepony.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface ScreenInitCallback {
    Event<ScreenInitCallback> EVENT = EventFactory.createArrayBacked(ScreenInitCallback.class, listeners -> (screen, buttons) -> {
        for (ScreenInitCallback event : listeners) {
            event.init(screen, buttons);
        }
    });

    void init(Screen screen, ButtonList buttons);

    interface ButtonList {
        /**
         * Adds a button to this screen.
         * <p>
         * Made public to help with mod development.
         */
        <T extends GuiEventListener & Renderable & NarratableEntry> T addButton(T button);
    }
}
