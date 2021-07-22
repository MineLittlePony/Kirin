package com.minelittlepony.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.texture.NativeImage;

public interface SkinFilterCallback {

    Event<SkinFilterCallback> EVENT = EventFactory.createArrayBacked(SkinFilterCallback.class, listeners -> {
        return new SkinFilterCallback() {
            @Override
            public void processImage(NativeImage image, boolean legacy) {
                for (SkinFilterCallback event : listeners) {
                    event.processImage(image, legacy);
                }
            }

            @Override
            public boolean shouldAllowTransparency(NativeImage image, boolean legacy) {
                for (SkinFilterCallback event : listeners) {
                    if (event.shouldAllowTransparency(image, legacy)) {
                        return true;
                    }
                }

                return false;
            }
        };
    });

    void processImage(NativeImage image, boolean legacy);

    default boolean shouldAllowTransparency(NativeImage image, boolean legacy) {
        return true; // default is true since in most cases this is the desired effect
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
