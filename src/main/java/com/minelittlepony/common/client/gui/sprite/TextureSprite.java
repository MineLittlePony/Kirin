package com.minelittlepony.common.client.gui.sprite;

import com.minelittlepony.common.client.gui.dimension.Bounds;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class TextureSprite implements ISprite {

    private Identifier texture = Identifier.withDefaultNamespace("widget/button");

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
    public void render(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY, float tickDelta) {
        render(context, x, y, mouseX, mouseY, tickDelta, 1);
    }

    @Override
    public void render(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY, float tickDelta, float alpha) {
        context.blit(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x + bounds.left, y + bounds.top,
                textureBounds.left, textureBounds.top,
                bounds.width, bounds.height,
                textureBounds.width, textureBounds.height,
                ARGB.white(alpha));
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
