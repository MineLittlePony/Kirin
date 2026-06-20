package com.minelittlepony.common.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;

/**
 * Utility for rendering objects such as ItemStacks, Entities, and BlockEntities, when there is no client world running.
 * <p>
 * This class performs all the neccessary setup to ensure the above objects render correctly.
 *
 * @author     Sollace
 *
 */
public class OutsideWorldRenderer {
    /**
     * Gets a pre-configured BlockEntityRenderDispatcher
     * for rendering BlockEntities outside of the world.
     *
     * @return a pre-configured BlockEntityRenderManager
     */
    public static BlockEntityRenderDispatcher configure() {
        Minecraft mc = Minecraft.getInstance();
        BlockEntityRenderDispatcher dispatcher = mc.getBlockEntityRenderDispatcher();
        dispatcher.prepare(mc.gameRenderer.mainCamera().position());
        mc.getEntityRenderDispatcher().prepare(mc.gameRenderer.mainCamera(), mc.crosshairPickEntity);

        return dispatcher;
    }

    /**
     * Renders a ItemStack to the screen.
     *
     * @param stack The stack to render.
     * @param x The left-X position (in pixels)
     * @param y The top-Y position (in pixels)
     */
    public static void renderStack(GuiGraphicsExtractor context, ItemStack stack, int x, int y) {
        try {
            configure();
        } catch (Throwable ignored) {}
        context.fakeItem(stack, x, y);
    }
}
