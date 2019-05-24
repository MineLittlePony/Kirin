package com.minelittlepony.common.client.gui.element;

import net.minecraft.client.MinecraftClient;

public class IconicButton extends Button {

    public IconicButton(int x, int y) {
        super(x, y, 20, 20);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        if (getStyle().hasIcon()) {
            MinecraftClient.getInstance().getItemRenderer().renderGuiItem(getStyle().getIcon(), x + 2, y + 2);
        }
    }
}
