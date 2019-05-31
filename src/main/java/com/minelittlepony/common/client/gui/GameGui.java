package com.minelittlepony.common.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sound.SoundEvent;

public abstract class GameGui extends Screen {

    protected GameGui(Component title) {
        super(title);
    }

    public static void playSound(SoundEvent event) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(event, 1));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        buttons.forEach(button -> {
            if (button instanceof ITooltipped) {
                ((ITooltipped<?>)button).renderToolTip(minecraft, mouseX, mouseY);
            }
        });
    }
}
