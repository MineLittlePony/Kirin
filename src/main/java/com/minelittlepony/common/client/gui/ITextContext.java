package com.minelittlepony.common.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.client.util.math.AffineTransformation;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Context utility for things that want to render text to the screen.
 * <p>
 * These methods are provided as an alternative to the Vanilla ones,
 * which one slight change to allow text to be rendered over content
 * that would normally be layered on top of it.
 * <p>
 * If you have entities in your screen and can't see text behind them,
 * use this.
 *
 * @author     Sollace
 */
public interface ITextContext {

    /**
     * Gets the global TextRenderer instance.
     */
    default TextRenderer getFont() {
        return MinecraftClient.getInstance().textRenderer;
    }

    /**
     * Draws a piece of coloured, left-aligned text to the screen.
     *
     * @param text The text to render
     * @param x The left X position (in pixel)
     * @param y The top Y position (in pixel)
     * @param color The font colour
     * @param zIndex The Z-index used when layering multiple elements.
     */
    default void drawLabel(String text, int x, int y, int color, double zIndex) {
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        MatrixStack stack = new MatrixStack();
        stack.translate(0, 0, zIndex);
        Matrix4f matrix = stack.peek().getModel();

        getFont().draw(text, x, y, color, true, matrix, immediate, true, 0, 0xF000F0);
        immediate.draw();
    }

    /**
     * Draws a piece of coloured, centered text to the screen.
     *
     * @param text The text to render
     * @param x The left X position (in pixel)
     * @param y The top Y position (in pixel)
     * @param color The font colour
     * @param zIndex The Z-index used when layering multiple elements.
     */
    default void drawCenteredLabel(String text, int x, int y, int color, double zIndex) {
        int width = getFont().getStringWidth(text);

        drawLabel(text, x - width/2, y, color, zIndex);
    }

    /**
     * Draws a block of text spanning multiple lines. Content is left-aligned,
     * and wrapped to fit in the given page width.
     *
     * @param text The text to render
     * @param x The left X position (in pixel)
     * @param y The top Y position (in pixel)
     * @param maxWidth The maximum page width
     * @param color The font colour
     */
    default void drawTextBlock(String text, int x, int y, int maxWidth, int color) {
        while(text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }

        Matrix4f matrix = AffineTransformation.identity().getMatrix();

        for (String line : getFont().wrapStringToWidthAsList(text, maxWidth)) {
            float left = x;
            if (getFont().isRightToLeft()) {
                left += maxWidth - getFont().getStringWidth(getFont().mirror(line));
            }

            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            getFont().draw(line, left, y, color, false, matrix, immediate, true, 0, 0xF000F0);
            immediate.draw();

            y += 9;
        }
    }
}
