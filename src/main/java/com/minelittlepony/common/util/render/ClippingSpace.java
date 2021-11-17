package com.minelittlepony.common.util.render;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

/**
 * Wrapper around GLScissor for clipping a rendered object to a defined rectangle.
 *
 * @author     Sollace
 *
 */
public class ClippingSpace {

    /**
     * Sets up a clipping region around a render call.
     *
     * @param x The left edge of the clipping area.
     * @param y The top edge of the clipping area.
     * @param width The total width.
     * @param height The total height.
     * @param renderTask A function to call (render content) whilst the clipping is active.
     */
    public static void renderClipped(int x, int y, int width, int height, Runnable renderTask) {
        enableClipRegion(x, y, width, height);

        renderTask.run();

        RenderSystem.disableScissor();
    }

    /**
     * Excludes a particular render call from an active scissor.
     * @param renderTask A function to call (render content) whilst the clipping is inactive.
     */
    public static void renderUnclipped(Runnable renderTask) {
        if (GL11.glGetBoolean(GL11.GL_SCISSOR_TEST)) {
            GlStateManager._disableScissorTest();
            renderTask.run();
            GlStateManager._enableScissorTest();
        } else {
            renderTask.run();
        }
    }

    private static void enableClipRegion(int x, int y, int width, int height) {
        Window window = MinecraftClient.getInstance().getWindow();
        double f = window.getScaleFactor();
        int windowHeight = (int)Math.round(window.getScaledHeight() * f);

        x *= f;
        y *= f;
        width *= f;
        height *= f;

        RenderSystem.enableScissor(
                Math.round(x),
                windowHeight - height - y,
                Math.round(width),
                Math.round(height)
        );
    }
}
