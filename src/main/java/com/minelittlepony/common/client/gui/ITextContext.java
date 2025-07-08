package com.minelittlepony.common.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

/**
 * Context utility for things that want to render text to the screen.
 * <p>
 * These methods are provided as an alternative to the Vanilla ones,
 * with one slight change to allow text to be rendered over content
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
     *
     * @deprecated Z-Index is no longer used. Use {@code context.createNewRootLayer()} if you need manual layering
     */
    @Deprecated
    default void drawLabel(DrawContext context, Text text, int x, int y, int color, @Deprecated double zIndex) {
        context.drawText(getFont(), text, x, y, color, false);
    }

    /**
     * Draws a piece of coloured, left-aligned text to the screen.
     *
     * @param text The text to render
     * @param x The left X position (in pixel)
     * @param y The top Y position (in pixel)
     * @param color The font colour
     */
    default void drawLabel(DrawContext context, Text text, int x, int y, int color) {
        context.drawText(getFont(), text, x, y, color, false);
    }

    /**
     * Draws a piece of coloured, centered text to the screen.
     *
     * @param text The text to render
     * @param x The left X position (in pixel)
     * @param y The top Y position (in pixel)
     * @param color The font colour
     * @param zIndex The Z-index used when layering multiple elements.
     *
     * @deprecated Z-Index is no longer used. Use {@code context.createNewRootLayer()} if you need manual layering
     */
    @Deprecated
    default void drawCenteredLabel(DrawContext context, Text text, int x, int y, int color, @Deprecated double zIndex) {
        drawLabel(context, text, x - getFont().getWidth(text)/2, y, color);
    }

    /**
     * Draws a piece of coloured, centered text to the screen.
     *
     * @param text The text to render
     * @param x The left X position (in pixel)
     * @param y The top Y position (in pixel)
     * @param color The font colour
     */
    default void drawCenteredLabel(DrawContext context, Text text, int x, int y, int color) {
        drawLabel(context, text, x - getFont().getWidth(text)/2, y, color);
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
    default void drawTextBlock(DrawContext context, StringVisitable text, int x, int y, int maxWidth, int color) {
        context.drawWrappedText(getFont(), text, x, y, maxWidth, color, false);
    }
}
