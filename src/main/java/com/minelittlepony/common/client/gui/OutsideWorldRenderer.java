package com.minelittlepony.common.client.gui;

import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.util.math.Vector4f;

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
        BlockEntityRenderDispatcher dispatcher = mc.getBlockEntityRenderDispatcher();

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
     *
     * @deprecated @see {@link OutsideWorldRenderer#renderStack(MatrixStack, ItemStack, int, int)}
     */
    @Deprecated
    public static void renderStack(ItemStack stack, int x, int y) {
        configure(null);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(stack, x, y);
    }

    /**
     * Renders a ItemStack to the screen.
     *
     * @param stack The stack to render.
     * @param x The left-X position (in pixels)
     * @param y The top-Y position (in pixels)
     *
     * @since 1.15.0
     */
    public static void renderStack(MatrixStack matrices, ItemStack stack, int x, int y) {
        configure(null);
        Vector4f v = new Vector4f(x, y, 0, 1);
        v.transform(matrices.peek().getPositionMatrix());
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(stack, (int)v.getX(), (int)v.getY());
    }
}
