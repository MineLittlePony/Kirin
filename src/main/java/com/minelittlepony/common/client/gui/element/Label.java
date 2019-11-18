package com.minelittlepony.common.client.gui.element;

import com.minelittlepony.common.client.gui.dimension.Bounds;

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
    public Bounds getBounds() {
        Bounds bounds = super.getBounds();

        TextRenderer fonts = MinecraftClient.getInstance().textRenderer;

        bounds.width = fonts.getStringWidth(getStyle().getText());
        if (this.center) {
            bounds.left = x - bounds.width/2;
        }

        return bounds;
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
        int textY = (int)(y + MinecraftClient.getInstance().textRenderer.fontHeight/1.5F);

        if (center) {
            drawCenteredLabel(getStyle().getText(), x, textY, getStyle().getColor(), 0);
        } else {
            drawLabel(getStyle().getText(), x, textY, getStyle().getColor(), 0);
        }
    }
}
