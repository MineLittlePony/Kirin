package com.minelittlepony.common.client.gui;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.block.entity.BlockEntityRenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
     * <p>
     *
     * @param world An optional World instance to configure the renderer against. May be null.
     *
     * @return a pre-configured BlockEntityRenderDispatcher
     *
     * @deprecated Will be removed in MC1.22. World is no longer needed
     */
    @Deprecated(forRemoval = true)
    public static BlockEntityRenderManager configure(@Nullable World world) {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockEntityRenderManager dispatcher = mc.getBlockEntityRenderDispatcher();
        dispatcher.configure(mc.gameRenderer.getCamera());
        mc.getEntityRenderDispatcher().configure(mc.gameRenderer.getCamera(), mc.targetedEntity);

        return dispatcher;
    }
    /**
     * Gets a pre-configured BlockEntityRenderDispatcher
     * for rendering BlockEntities outside of the world.
     *
     * @return a pre-configured BlockEntityRenderManager
     */
    public static BlockEntityRenderManager configure() {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockEntityRenderManager dispatcher = mc.getBlockEntityRenderDispatcher();
        dispatcher.configure(mc.gameRenderer.getCamera());
        mc.getEntityRenderDispatcher().configure(mc.gameRenderer.getCamera(), mc.targetedEntity);

        return dispatcher;
    }

    /**
     * Renders a ItemStack to the screen.
     *
     * @param stack The stack to render.
     * @param x The left-X position (in pixels)
     * @param y The top-Y position (in pixels)
     */
    public static void renderStack(DrawContext context, ItemStack stack, int x, int y) {
        try {
            configure();
        } catch (Throwable ignored) {}
        context.drawItem(stack, x, y);
    }
}
