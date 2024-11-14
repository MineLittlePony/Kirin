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
        return ColorHelper.floatFromChannel(ColorHelper.getAlpha(hex));
    }

    /**
     * Returns the RED channel for the given colour hex code.
     */
    static float r(int hex) {
        return ColorHelper.floatFromChannel(ColorHelper.getRed(hex));
    }

    /**
     * Returns the GREEN channel for the given colour hex code.
     */
    static float g(int hex) {
        return ColorHelper.floatFromChannel(ColorHelper.getGreen(hex));
    }

    /**
     * Returns the BLUE channel for the given colour hex code.
     */
    static float b(int hex) {
        return ColorHelper.floatFromChannel(ColorHelper.getBlue(hex));
    }

    /**
     * Converts the given rgb floats on a range of 0-1 into a colour hex code.
     *
     * @Deprecated(since = "1.19.4", forRemoval = true)
     */
    @Deprecated(since = "1.19.4", forRemoval = true)
    static int argbToHex(float a, float r, float g, float b) {
        return ColorHelper.fromFloats(a, r, g, b);
    }

    /**
     * Converts the given rbg int on a range of 0-255 into a colour hex code.
     *
     * @deprecated Will be removed in MC1.22
     */
    @Deprecated(since = "1.19.4", forRemoval = true)
    static int argbToHex(int a, int r, int g, int b) {
        return ColorHelper.getArgb(a, r, g, b);
    }

    /**
     * Converts a colour hex code from BGR to RGB (and back).
     *
     * @deprecated Will be removed in MC1.22
     */
    @Deprecated
    static int abgrToArgb(int color) {
        return ColorHelper.getArgb(
                ColorHelper.getAlpha(color),
                ColorHelper.getBlue(color),
                ColorHelper.getGreen(color),
                ColorHelper.getRed(color)
        );
    }

    /**
     * Interpolates between two colours
     *
     * @deprecated Will be removed in MC1.22
     */
    @Deprecated(since = "1.19.4", forRemoval = true)
    static int lerp(float delta, int from, int to) {
        return ColorHelper.lerp(delta, from, to);
    }
}
