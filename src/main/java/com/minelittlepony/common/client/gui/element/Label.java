package com.minelittlepony.common.client.gui.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

/**
 * A simple label for drawing text to a gui screen.
 *
 * @author Sollace
 *
 */
public class Label extends Button {

    private boolean center;

    public Label(int x, int y) {
        super(x, y);
    }

    public Label setCentered() {
        this.center = true;

        return this;
    }
    
    @Override
    protected boolean isValidClickButton(int button) {
    	return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;

        if (center) {
            drawCenteredString(fontRenderer, getStyle().getText(), x, y, getStyle().getColor());
        } else {
            drawString(fontRenderer, getStyle().getText(), x, y, getStyle().getColor());
        }
    }
}
