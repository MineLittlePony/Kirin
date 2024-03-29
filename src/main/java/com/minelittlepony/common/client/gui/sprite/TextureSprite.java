package com.minelittlepony.common.client.gui.sprite;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class TextureSprite implements ISprite {

    private Identifier texture = new Identifier("widget/button");

    private final Bounds bounds = new Bounds(0, 0, 0, 0);
    private final Bounds textureBounds = new Bounds(0, 0, 256, 256);

    public TextureSprite setPosition(int x, int y) {
        bounds.left = x;
        bounds.top = y;

        return this;
    }

    public TextureSprite setSize(int width, int height) {
        bounds.width = width;
        bounds.height = height;

        return this;
    }

    public TextureSprite setTexture(Identifier texture) {
        this.texture = texture;

        return this;
    }

    public TextureSprite setTextureOffset(int x, int y) {
        textureBounds.left = x;
        textureBounds.top = y;

        return this;
    }

    public TextureSprite setTextureSize(int width, int height) {
        textureBounds.width = width;
        textureBounds.height = height;

        return this;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float partialTicks) {
        context.drawTexture(
                texture,
                x + bounds.left, y + bounds.top,
                0,
                textureBounds.left, textureBounds.top,
                bounds.width, bounds.height,
                textureBounds.width, textureBounds.height);
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(Bounds bounds) {
        setPosition(bounds.left, bounds.top);
    }
}
