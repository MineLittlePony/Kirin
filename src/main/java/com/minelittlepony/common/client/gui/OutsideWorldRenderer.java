package com.minelittlepony.common.client.gui;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ObjectUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class OutsideWorldRenderer {
    public static BlockEntityRenderDispatcher configure(@Nullable World world) {
        BlockEntityRenderDispatcher dispatcher = BlockEntityRenderDispatcher.INSTANCE;
        MinecraftClient mc = MinecraftClient.getInstance();

        world = ObjectUtils.firstNonNull(dispatcher.world, world, mc.world);

        dispatcher.configure(world,
                mc.getTextureManager(),
                mc.getEntityRenderManager().getTextRenderer(),
                mc.gameRenderer.getCamera(),
                mc.crosshairTarget);

        mc.getEntityRenderManager().configure(world,
                mc.gameRenderer.getCamera(),
                mc.targetedEntity);

        return dispatcher;
    }

    public static void renderStack(ItemStack stack, int x, int y) {
        configure(null);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, x, y);
    }
}
