package com.minelittlepony.common.client.gui;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ObjectUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
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
     */
    public static BlockEntityRenderDispatcher configure(@Nullable World world) {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockEntityRenderDispatcher dispatcher = mc.method_31975();

        world = ObjectUtils.firstNonNull(dispatcher.world, world, mc.world);

        dispatcher.configure(world,
                mc.gameRenderer.getCamera(),
                mc.crosshairTarget);

        mc.getEntityRenderDispatcher().configure(world,
                mc.gameRenderer.getCamera(),
                mc.targetedEntity);

        return dispatcher;
    }

    /**
     * Renders a ItemStack to the screen.
     *
     * @param stack The stack to render.
     * @param x The left-X position (in pixels)
     * @param y The top-Y position (in pixels)
     */
    public static void renderStack(ItemStack stack, int x, int y) {
        configure(null);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(stack, x, y);
    }
}
