package com.minelittlepony.common.util;

import net.minecraft.client.texture.NativeImage;

/**
 * Converter for altering native images at runtime.
 */
@FunctionalInterface
public interface TextureConverter {

    void convertTexture(Drawer drawer);

    interface Drawer {
        /**
         * Gets the native image we are currently modifying.
         */
        NativeImage getImage();

        /**
         * Copies a texture region from one area to another.
         */
        default void copy(
                int xFrom, int yFrom,  // source coordinates
                int xTo, int yTo,      // destination coordinates
                int width, int height, // section size
                boolean mirrorX, boolean mirrorY) {
            getImage().method_4304(xFrom, yFrom, xTo, yTo, width, height, mirrorX, mirrorY);
        }
    }
}
