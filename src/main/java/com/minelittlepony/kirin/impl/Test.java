package com.minelittlepony.kirin.impl;

import com.minelittlepony.common.client.gui.element.Button;
import com.minelittlepony.common.event.ScreenInitCallback;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;

public class Test implements ClientModInitializer {
    public static final boolean DEBUG = Boolean.getBoolean("kirin.debug");

    @Override
    public void onInitializeClient() {
        if (DEBUG) {
            ScreenInitCallback.EVENT.register((screen, buttons) -> {
                if (screen instanceof TitleScreen) {
                    Button button = buttons.addButton(new Button(50, 20, 20, 20))
                            .onClick(_ -> Minecraft.getInstance().gui.setScreen(new KirinTestScreen(screen)));
                        button.getStyle()
                                .setTooltip("Kirin Test Screen", 0, 10);
                }
            });
        }
    }

}
