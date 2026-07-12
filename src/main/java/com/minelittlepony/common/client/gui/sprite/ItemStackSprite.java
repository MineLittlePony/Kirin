package com.minelittlepony.common.client.gui.sprite;


import org.jetbrains.annotations.Nullable;

import com.minelittlepony.common.util.registry.ComponentUtils;
import com.minelittlepony.common.util.registry.ForwardingHolder;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.ItemLike;

public class ItemStackSprite implements ISprite {

    @Nullable
    private ItemStackTemplate stack;
    @Nullable
    private ItemStack itemStack;

    private int tint = CommonColors.WHITE;

    public ItemStackSprite setStack(ItemLike item) {
        return setStack(new ItemStackTemplate(item.asItem()));
    }

    public ItemStackSprite setStack(ItemStackTemplate stack) {
        this.stack = stack;
        this.itemStack = null;
        return setTint(tint);
    }

    public ItemStackSprite setTint(int tint) {
        this.tint = tint;
        if (stack != null) {
            stack = new ItemStackTemplate(stack.item(), stack.count(), ComponentUtils.copy(stack.components())
                    .set(DataComponents.DYED_COLOR, new DyedItemColor(tint))
                    .build());
        }
        return this;
    }

    @Override
    public void render(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY, float tickDelta) {
        render(context, x, y, mouseX, mouseY, tickDelta, 1);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY, float tickDelta, float alpha) {
        if (alpha >= 0.5F && stack != null) {
            if (itemStack == null) {
                if (stack.item().areComponentsBound()) {
                    itemStack = stack.create();
                } else {
                    var baseComponents = DataComponentMap.builder()
                            .addAll(DataComponents.COMMON_ITEM_COMPONENTS)
                            .set(DataComponents.ITEM_MODEL, stack.item().unwrapKey().orElseThrow().identifier())
                            .build();
                    itemStack = new ItemStack(ForwardingHolder.withComponents(stack.item(), baseComponents), 1, stack.components());
                    // ensure components are bound
                    itemStack.getItem().builtInRegistryHolder().bindComponents(baseComponents);
                }
            }

            context.fakeItem(itemStack, x + 2, y + 2);
        }
    }
}
