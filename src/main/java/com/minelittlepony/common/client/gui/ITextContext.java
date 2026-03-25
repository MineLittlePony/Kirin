package com.minelittlepony.common.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

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
    default Font getFont() {
        return Minecraft.getInstance().font;
    }

    /**
     * Draws a piece of coloured, left-aligned text to the screen.
     *
     * @param text The text to render
     * @param x The left X position (in pixel)
     * @param y The top Y position (in pixel)
     * @param color The font colour
     */
    default void drawLabel(GuiGraphicsExtractor context, Component text, int x, int y, int color) {
        context.text(getFont(), text, x, y, color, false);
    }

    /**
     * Draws a piece of coloured, centered text to the screen.
     *
     * @param text The text to render
     * @param x The left X position (in pixel)
     * @param y The top Y position (in pixel)
     * @param color The font colour
     */
    default void drawCenteredLabel(GuiGraphicsExtractor context, Component text, int x, int y, int color) {
        drawLabel(context, text, x - getFont().width(text)/2, y, color);
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
    default void drawTextBlock(GuiGraphicsExtractor context, FormattedText text, int x, int y, int maxWidth, int color) {
        context.textWithWordWrap(getFont(), text, x, y, maxWidth, color, false);
    }
}
