package com.minelittlepony.common.util;

import net.minecraft.util.ARGB;

/**
 * Colouration Utilities
 */
public interface Color {
    /**
     * Returns the ALPHA channel for the given colour hex code.
     *
     * @deprecated Will be removed in MC1.22. Replace with {@link ColorHelper#getAlphaFloat}
     */
    @Deprecated(since = "1.20.1", forRemoval = true)
    static float a(int hex) {
        return ARGB.alphaFloat(hex);
    }

    /**
     * Returns the RED channel for the given colour hex code.
     *
     * @deprecated Will be removed in MC1.22. Replace with {@link ColorHelper#getRedFloat}
     */
    @Deprecated(since = "1.20.1", forRemoval = true)
    static float r(int hex) {
        return ARGB.redFloat(hex);
    }

    /**
     * Returns the GREEN channel for the given colour hex code.
     *
     * @deprecated Will be removed in MC1.22. Replace with {@link ColorHelper#getGreenFloat}
     */
    @Deprecated(since = "1.20.1", forRemoval = true)
    static float g(int hex) {
        return ARGB.greenFloat(hex);
    }

    /**
     * Returns the BLUE channel for the given colour hex code.
     *
     * @deprecated Will be removed in MC1.22. Replace with {@link ColorHelper#getBlueFloat}
     */
    @Deprecated(since = "1.20.1", forRemoval = true)
    static float b(int hex) {
        return ARGB.blueFloat(hex);
    }

    /**
     * Converts the given rgb floats on a range of 0-1 into a colour hex code.
     *
     * @deprecated Will be removed in MC1.22. Replace with {@link ColorHelper#fromFloats}
     */
    @Deprecated(since = "1.19.4", forRemoval = true)
    static int argbToHex(float a, float r, float g, float b) {
        return ARGB.colorFromFloat(a, r, g, b);
    }

    /**
     * Converts the given rbg int on a range of 0-255 into a colour hex code.
     *
     * @deprecated Will be removed in MC1.22. Replace with {@link ColorHelper#getArgb}
     */
    @Deprecated(since = "1.19.4", forRemoval = true)
    static int argbToHex(int a, int r, int g, int b) {
        return ARGB.color(a, r, g, b);
    }

    /**
     * Converts a colour hex code from BGR to RGB (and back).
     *
     * @deprecated Will be removed in MC1.22. Replace with {@link ColorHelper#fromAbgr}
     */
    @Deprecated
    static int abgrToArgb(int color) {
        return ARGB.fromABGR(color);
    }

    /**
     * Interpolates between two colours
     *
     * @deprecated Will be removed in MC1.22. Replace with {@link ColorHelper#lerp}
     */
    @Deprecated(since = "1.19.4", forRemoval = true)
    static int lerp(float delta, int from, int to) {
        return ARGB.linearLerp(delta, from, to);
    }
}
