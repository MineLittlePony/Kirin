package com.minelittlepony.common.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.SoundEvent;

public abstract class GameGui extends GuiScreen {

    public static void playSound(SoundEvent event) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(event, 1));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        buttons.forEach(button -> {
            if (button instanceof ITooltipped) {
                ((ITooltipped<?>)button).renderToolTip(mc, mouseX, mouseY);
            }
        });
    }
}
