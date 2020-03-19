package com.minelittlepony.common.client.gui.dimension;

/**
 * An element's external padding.
 *
 * @author     Sollace
 */
public class Padding {

    public int top;
    public int left;

    public int bottom;
    public int right;

    public Padding(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     * Sets all sides to the given value.
     */
    public void setAll(int padding) {
        setVertical(padding);
        setHorizontal(padding);
    }

    /**
     * Sets the top and bottom padding to the given value.
     */
    public void setVertical(int padding) {
        top = padding;
        bottom = padding;
    }

    /**
     * Sets the left and right padding to the given value.
     */
    public void setHorizontal(int padding) {
        left = padding;
        right = padding;
    }
}
