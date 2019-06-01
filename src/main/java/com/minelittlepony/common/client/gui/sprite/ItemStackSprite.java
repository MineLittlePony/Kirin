package com.minelittlepony.common.client.gui.sprite;

import net.minecraft.client.MinecraftClient;
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
        stack.getOrCreateSubCompoundTag("display").putInt("color", tint);
        return this;
    }

    @Override
    public void render(int x, int y, int mouseX, int mouseY, float partialTicks) {
        MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, x + 2, y + 2);
    }
}
