package com.minelittlepony.common.client.gui.sprite;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;

import net.minecraft.client.gui.DrawContext;

public interface ISprite extends IBounded {

    ISprite EMPTY = (m, x, y, mx, my, t) -> {};

    void render(DrawContext context, int x, int y, int mouseX, int mouseY, float partialTicks);

    @Override
    default Bounds getBounds() {
        return Bounds.empty();
    }

    @Override
    default void setBounds(Bounds bounds) {

    }
}
