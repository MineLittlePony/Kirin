package com.minelittlepony.common.client.gui.sprite;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Colors;

public class ItemStackSprite implements ISprite {

    private ItemStack stack = ItemStack.EMPTY;

    private int tint = Colors.WHITE;

    public ItemStackSprite setStack(ItemConvertible iitem) {
        return setStack(new ItemStack(iitem));
    }

    public ItemStackSprite setStack(ItemStack stack) {
        this.stack = stack;
        return setTint(tint);
    }

    public ItemStackSprite setTint(int tint) {
        this.tint = tint;
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(tint));
        return this;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float tickDelta) {
        render(context, x, y, mouseX, mouseY, tickDelta, 1);
    }


    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float tickDelta, float alpha) {
        if (alpha >= 0.5F) {
            context.drawItemWithoutEntity(stack, x + 2, y + 2);
        }
    }
}
