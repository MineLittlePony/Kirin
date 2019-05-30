package com.minelittlepony.common.client.gui.element;

import javax.annotation.Nonnull;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

import com.minelittlepony.common.client.gui.IField;

public class Toggle extends Button implements IField<Boolean, Toggle> {

    private boolean on;

    @Nonnull
    private IChangeCallback<Boolean> action = IChangeCallback::none;

    public Toggle(int x, int y, boolean value) {
        super(x, y, 30, 15);

        on = value;
    }

    @Override
    public Toggle onChange(@Nonnull IChangeCallback<Boolean> action) {
        this.action = action;
        return this;
    }

    @Override
    public Boolean getValue() {
        return on;
    }

    @Override
    public Toggle setValue(Boolean value) {
        if (value != on) {
            on = action.perform(value);
        }

        return this;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        setValue(!on);
    }

    @Override
    protected void renderBg(MinecraftClient mc, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(WIDGETS_LOCATION);

        int i = 46 + (isHovered() ? 2 : 1) * 20;
        int sliderX = x + (int)((on ? 1 : 0) * (width - 8));

        renderButtonBlit(sliderX, y, i, 8, height);
    }

    @Override
    protected void renderForground(MinecraftClient mc, int mouseX, int mouseY, int foreColor) {
        TextRenderer font = mc.textRenderer;

        int textY = (int)(y + font.fontHeight / 2);
        int textX = x + width + 10;

        drawString(font, getStyle().getText(), textX, textY, foreColor);
    }

    @Override
    protected int getYImage(boolean mouseOver) {
        return 0;
    }
}
