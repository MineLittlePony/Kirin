package com.minelittlepony.common.client.gui.sprite;

import com.minelittlepony.common.client.gui.OutsideWorldRenderer;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public class ItemStackSprite implements ISprite {

    private ItemStack stack = ItemStack.EMPTY;

    private int tint = 0xFFFFFFFF;

    public ItemStackSprite setStack(ItemConvertible iitem) {
        return setStack(new ItemStack(iitem));
    }

    public ItemStackSprite setStack(ItemStack stack) {
        this.stack = stack;

        return setTint(tint);
    }

    public ItemStackSprite setTint(int tint) {
        stack.getOrCreateSubNbt("display").putInt("color", tint);
        return this;
    }

    @Override
    public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float partialTicks) {
        OutsideWorldRenderer.renderStack(matrices, stack, x + 2, y + 2);
        RenderSystem.disableDepthTest();
    }
}
