package com.minelittlepony.common.client.gui.dimension;

/**
 * Interface for elements that have a defined dimensions (bounds) filling space on the screen.
 *
 * @author     Sollace
 */
public interface IBounded {
    /**
     * Gets the relatively position bounding rectable of this elements.
     * The bounds include all content and text labels rendered with this element.
     */
    Bounds getBounds();

    /**
     * Sets the element's bounds.
     */
    void setBounds(Bounds bounds);
}
