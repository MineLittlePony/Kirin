package com.minelittlepony.common.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Interface element that renders a tooltip when hovered.
 *
 * @author     Sollace
 *
 * @param  <T> The subclass element.
 */
public interface ITooltipped<T extends ITooltipped<T>> {
    /**
     * Draws this element's tooltip.
     */
    void renderToolTip(MatrixStack matrices, Screen parent, int mouseX, int mouseY);
}
