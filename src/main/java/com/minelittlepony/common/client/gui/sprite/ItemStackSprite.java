package com.minelittlepony.common.client.gui.sprite;

import com.minelittlepony.common.client.gui.OutsideWorldRenderer;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public class ItemStackSprite implements ISprite {

    private ItemStack stack = ItemStack.EMPTY;

    private int tint = 0xFFFFFFFF;

    private boolean renderFailed;
    private boolean needsWorld;

    public ItemStackSprite setStack(ItemConvertible iitem) {
        return setStack(new ItemStack(iitem));
    }

    public ItemStackSprite setStack(ItemStack stack) {
        this.stack = stack;

        return setTint(tint);
    }

    public ItemStackSprite setTint(int tint) {
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(tint, true));
        return this;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (renderFailed) {
            return;
        }

        if (!needsWorld) {
            try {
                context.drawItem(stack, x, y);
                RenderSystem.disableDepthTest();
                return;
            } catch (Throwable ignored) {
                needsWorld = true;
            }
        }

        try {
            OutsideWorldRenderer.configure(null);
            context.drawItem(stack, x, y);
            RenderSystem.disableDepthTest();
        } catch (Throwable ignored) {
            renderFailed = true;
        }
    }
}
