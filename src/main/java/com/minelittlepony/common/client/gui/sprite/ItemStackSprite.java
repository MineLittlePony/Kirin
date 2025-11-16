package com.minelittlepony.common.client.gui.sprite;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.ItemLike;

public class ItemStackSprite implements ISprite {

    private ItemStack stack = ItemStack.EMPTY;

    private int tint = CommonColors.WHITE;

    public ItemStackSprite setStack(ItemLike iitem) {
        return setStack(new ItemStack(iitem));
    }

    public ItemStackSprite setStack(ItemStack stack) {
        this.stack = stack;
        return setTint(tint);
    }

    public ItemStackSprite setTint(int tint) {
        this.tint = tint;
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(tint));
        return this;
    }

    @Override
    public void render(GuiGraphics context, int x, int y, int mouseX, int mouseY, float tickDelta) {
        render(context, x, y, mouseX, mouseY, tickDelta, 1);
    }


    @Override
    public void render(GuiGraphics context, int x, int y, int mouseX, int mouseY, float tickDelta, float alpha) {
        if (alpha >= 0.5F) {
            context.renderFakeItem(stack, x + 2, y + 2);
        }
    }
}
