package com.minelittlepony.common.client.gui.dimension;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Represents the bounding rectangle of an element on the screen.
 *
 * @author     Sollace
 */
public class Bounds {

    public int top;
    public int left;

    public int width;
    public int height;

    public static Bounds empty() {
        return new Bounds(0, 0, 0, 0);
    }

    public Bounds(int top, int left, int width, int height) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
    }

    public boolean isEmpty() {
        return width <= 0 || height <= 0;
    }

    /**
     * Determines whether the given point is somewhere in this bounding rectangle.
     *
     * @param x The X position
     * @param y The Y Position
     * @return True if the point overlaps of touches the edge of this rectangle
     */
    public boolean contains(double x, double y) {
        return !isEmpty() && x >= left && x <= (left + width) && y >= top && y <= (top + height);
    }

    /**
     * Computes a new bounds expanded on all sides by the given padding
     *
     * @param other The padding to add to these bounds.
     * @return A new bounds with the computed dimensions.
     */
    public Bounds add(Padding other) {
        return new Bounds(
            top - other.top,
            left - other.left,
            width + other.left + other.right,
            height + other.top + other.bottom
        );
    }

    /**
     * Computes a new bounds with the same dimensions as this one,
     * but shifted by the given margin.
     *
     * @param other the margins to move this bounds by.
     * @return A new bounds with the computed dimensions.
     */
    public Bounds offset(Padding other) {
        return new Bounds(
            top + other.top,
            left + other.left,
            width,
            height
        );
    }

    /**
     * Computes a new bounds that encompasses both this one and the one passed in.
     *
     * @param other The bounds to add to this one.
     * @return A new bounds with the computed dimensions.
     */
    public Bounds add(Bounds other) {

        if (other.isEmpty()) {
            return this;
        }

        if (isEmpty()) {
            return other;
        }

        int t = Math.min(top, other.top);
        int l = Math.min(left, other.left);

        int b = Math.max(top + height, other.top + other.height);
        int r = Math.max(left + width, other.left + other.width);

        int h = b - t;
        int w = r - l;

        return new Bounds(t, l, w, h);
    }

    /**
     * Copies the values of another bounds into this one.
     */
    public void copy(Bounds other) {
        top = other.top;
        left = other.left;
        width = other.width;
        height = other.height;
    }

    /**
     * Draws a coloured rectangle over the area covered by this bounds.
     * Useful for debugging.
     */
    public void draw(MatrixStack matrices, int tint) {
        DrawableHelper.fill(matrices, left, top, left + width, top + height, tint);
    }

    public void debugMeasure(MatrixStack matrices) {
        Window window = MinecraftClient.getInstance().getWindow();
        DrawableHelper.fill(matrices, left, 0, left + 1, window.getScaledHeight(), 0xFFFFFFFF);
        DrawableHelper.fill(matrices, left + width, 0, left + width + 1, window.getScaledHeight(), 0xFFFFFFFF);

        DrawableHelper.fill(matrices, 0, top, window.getScaledWidth(), top + 1, 0xFFFFFFFF);
        DrawableHelper.fill(matrices, 0, top + height, window.getScaledWidth(), top + height + 1, 0xFFFFFFFF);
    }
}
