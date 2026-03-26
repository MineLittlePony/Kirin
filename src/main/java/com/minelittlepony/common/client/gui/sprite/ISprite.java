package com.minelittlepony.common.client.gui.sprite;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface ISprite extends IBounded {

    ISprite EMPTY = (_, _, _, _, _, _) -> {};

    void render(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY, float tickDelta);

    default void render(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY, float tickDelta, float alpha) {
        render(context, x, y, mouseX, mouseY, tickDelta);
    }

    @Override
    default Bounds getBounds() {
        return Bounds.empty();
    }

    @Override
    default void setBounds(Bounds bounds) {

    }
}
