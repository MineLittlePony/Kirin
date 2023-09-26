package com.minelittlepony.common.util;

import net.minecraft.util.math.ColorHelper;

/**
 * Colouration Utilities
 */
public interface Color {
    /**
     * Returns the ALPHA channel for the given colour hex code.
     */
    static float a(int hex) {
        return ColorHelper.Argb.getAlpha(hex) / 255F;
    }

    /**
     * Returns the RED channel for the given colour hex code.
     */
    static float r(int hex) {
        return ColorHelper.Argb.getRed(hex) / 255F;
    }

    /**
     * Returns the GREEN channel for the given colour hex code.
     */
    static float g(int hex) {
        return ColorHelper.Argb.getGreen(hex) / 255F;
    }

    /**
     * Returns the BLUE channel for the given colour hex code.
     */
    static float b(int hex) {
        return ColorHelper.Argb.getBlue(hex) / 255F;
    }

    /**
     * Converts the given rgb floats on a range of 0-1 into a colour hex code.
     */
    static int argbToHex(float a, float r, float g, float b) {
        return ColorHelper.Argb.getArgb((int)(a * 255), (int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    /**
     * Converts the given rbg int on a range of 0-255 into a colour hex code.
     */
    static int argbToHex(int a, int r, int g, int b) {
        return ColorHelper.Argb.getArgb(a, r, g, b);
    }

    /**
     * Converts a colour hex code from BGR to RGB (and back).
     */
    static int abgrToArgb(int color) {
        return ColorHelper.Argb.getArgb(
                ColorHelper.Argb.getAlpha(color),
                ColorHelper.Argb.getBlue(color),
                ColorHelper.Argb.getGreen(color),
                ColorHelper.Argb.getRed(color)
        );
    }
}
