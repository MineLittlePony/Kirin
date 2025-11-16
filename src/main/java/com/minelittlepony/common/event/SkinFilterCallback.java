package com.minelittlepony.common.event;

import com.mojang.blaze3d.platform.NativeImage;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface SkinFilterCallback {
    int VANILLA_SKIN_WIDTH = 64;
    int VANILLA_SKIN_HEIGHT = 64;
    int OLD_VANILLA_SKIN_HEIGHT = 32;

    Event<SkinFilterCallback> EVENT = EventFactory.createArrayBacked(SkinFilterCallback.class, listeners -> {
        return new SkinFilterCallback() {
            @Override
            public NativeImage processImage(NativeImage image, int initialWidth, int initialHeight) {
                for (SkinFilterCallback event : listeners) {
                    image = event.processImage(image, initialWidth, initialHeight);
                }

                return image;
            }

            @Deprecated
            @Override
            public boolean shouldAllowTransparency(NativeImage image, int initialWidth, int initialHeight) {
                for (SkinFilterCallback event : listeners) {
                    if (event.shouldAllowTransparency(image, initialWidth, initialHeight)) {
                        return true;
                    }
                }

                return false;
            }
        };
    });

    NativeImage processImage(NativeImage image, int initialWidth, int initialHeight);

    default boolean shouldAllowTransparency(NativeImage image, int initialWidth, int initialHeight) {
        return true; // default is true since in most cases this is the desired effect
    }

    /**
     * Checks whether the incoming image size corresponds to the old 1:2 ratio player skins.
     */
    static boolean isLegacyAspectRatio(int width, int height) {
        return width == height * 2;
    }

    /**
     * Gets the scale of the image as a multiple of its vanilla image width.
     */
    static int getResolutionScale(int width, int height) {
        return width / VANILLA_SKIN_WIDTH;
    }

    /**
     * Fills a scaled section of an image with a solid color.
     * @param image
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     * @param color
     */
    static void fill(NativeImage image, int xFrom, int yFrom, int xTo, int yTo, int color) {
        int scale = getResolutionScale(image.getWidth(), image.getHeight());
        image.fillRect(xFrom * scale, yFrom * scale, xTo * scale, yTo * scale, color);
    }

    /**
     * Copies a scaled section from one region to another.
     *
     * @param xFrom   Source x
     * @param yFrom   Source y
     * @param xOffset Distance moved x
     * @param yOffset Distance moved y
     * @param width   Section width
     * @param height  Section height
     * @param mirrorX Mirror on x axis
     * @param mirrorY Mirror on y axis
     */
    static void copy(NativeImage image,
                      int xFrom, int yFrom,
                      int xOffset, int yOffset,
                      int width, int height,
                      boolean mirrorX, boolean mirrorY) {
        int scale = getResolutionScale(image.getWidth(), image.getHeight());
        image.copyRect(
                xFrom * scale, yFrom * scale,
                xOffset * scale, yOffset * scale,
                width * scale, height * scale,
                mirrorX, mirrorY);
    }
}
