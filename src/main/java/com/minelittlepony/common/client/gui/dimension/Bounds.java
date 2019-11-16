package com.minelittlepony.common.client.gui.dimension;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;

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

    public boolean contains(double x, double y) {
        return !isEmpty() && x >= left && x <= (left + width) && y >= top && y <= (top + height);
    }

    public Bounds add(Padding other) {
        return new Bounds(
            top - other.top,
            left - other.left,
            width + other.left + other.right,
            height + other.top + other.bottom
        );
    }

    public Bounds offset(Padding other) {
        return new Bounds(
            top + other.top,
            left + other.left,
            width,
            height
        );
    }

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
    public void draw(int tint) {
        DrawableHelper.fill(left, top, left + width, top + height, tint);
    }

    public void debugMeasure() {
        Window window = MinecraftClient.getInstance().getWindow();
        DrawableHelper.fill(left, 0, left + 1, window.getScaledHeight(), 0xFFFFFFFF);
        DrawableHelper.fill(left + width, 0, left + width + 1, window.getScaledHeight(), 0xFFFFFFFF);

        DrawableHelper.fill(0, top, window.getScaledWidth(), top + 1, 0xFFFFFFFF);
        DrawableHelper.fill(0, top + height, window.getScaledWidth(), top + height + 1, 0xFFFFFFFF);
    }
}
