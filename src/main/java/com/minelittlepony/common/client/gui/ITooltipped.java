package com.minelittlepony.common.client.gui;

import net.minecraft.client.gui.Screen;

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
    void renderToolTip(Screen parent, int mouseX, int mouseY);
}
