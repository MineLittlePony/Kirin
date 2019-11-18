package com.minelittlepony.common.client.gui.element;

import javax.annotation.Nonnull;

import com.minelittlepony.common.client.gui.IField;
import com.minelittlepony.common.client.gui.dimension.Bounds;

import net.minecraft.client.MinecraftClient;

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
    public Bounds getBounds() {
        Bounds bounds = super.getBounds();

        // The text label sits outside the bounds of the main toggle widget,
        // so we have to include that in our calculations.
        String label = getStyle().getText();
        int labelWidth = MinecraftClient.getInstance().textRenderer.getStringWidth(label);

        bounds.width = labelWidth > 0 ? Math.max(bounds.width, width + 10 + labelWidth) : width;

        return bounds;
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
        int sliderX = x + (on ? 1 : 0) * (width - 8);

        renderButtonBlit(sliderX, y, i, 8, height);
    }

    @Override
    protected void renderForground(MinecraftClient mc, int mouseX, int mouseY, int foreColor) {
        int textY = y + mc.textRenderer.fontHeight / 2;
        int textX = x + width + 10;

        drawLabel(getStyle().getText(), textX, textY, foreColor, 0);
    }

    @Override
    protected int getYImage(boolean mouseOver) {
        return 0;
    }
}
