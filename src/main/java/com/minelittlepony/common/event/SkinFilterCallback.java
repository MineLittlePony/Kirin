package com.minelittlepony.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.texture.NativeImage;

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

    static boolean isLegacyAspectRatio(int width, int height) {
        return width == height * 2;
    }

    static int getResolutionScale(int width, int height) {
        return width / VANILLA_SKIN_WIDTH;
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
        int scale = image.getWidth() / 64;
        image.copyRect(
                xFrom * scale, yFrom * scale,
                xOffset * scale, yOffset * scale,
                width * scale, height * scale,
                mirrorX, mirrorY);
    }
}
