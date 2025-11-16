package com.minelittlepony.common.client.gui.element;

import com.minelittlepony.common.client.gui.dimension.Bounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.MutableComponent;

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

        Font fonts = Minecraft.getInstance().font;

        bounds.width = fonts.width(getStyle().getText());
        if (this.center) {
            bounds.left = getX() - bounds.width/2;
        }

        return bounds;
    }

    @Override
    protected boolean isValidClickButton(MouseButtonInfo input) {
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public ScreenRectangle getRectangle() {
        return ScreenRectangle.empty();
    }

    @Override
    public void renderContents(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        int textY = (int)(getY() + Minecraft.getInstance().font.lineHeight/1.5F);

        if (center) {
            drawCenteredLabel(context, getStyle().getText(), getX(), textY, getStyle().getColor());
        } else {
            drawLabel(context, getStyle().getText(), getX(), textY, getStyle().getColor());
        }
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return getMessage().copy();
    }
}
