package com.minelittlepony.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;

@FunctionalInterface
public interface ScreenInitCallback {

    Event<ScreenInitCallback> EVENT = EventFactory.createArrayBacked(ScreenInitCallback.class, listeners -> (screen, buttons) -> {
        for (ScreenInitCallback event : listeners) {
            event.init(screen, buttons);
        }
    });

    void init(Screen screen, ButtonList buttons);

    interface ButtonList {
        <T extends Element & Drawable & Selectable> T add(T button);
    }
}
