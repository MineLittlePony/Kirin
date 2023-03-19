package com.minelittlepony.common.client.gui.element;

import org.jetbrains.annotations.NotNull;

import com.minelittlepony.common.client.gui.IField;
import com.minelittlepony.common.client.gui.dimension.Bounds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * Implements a toggle (switch) element with two states (ON/OFF).
 *
 * @author     Sollace
 */
public class Toggle extends Button implements IField<Boolean, Toggle> {

    private boolean on;

    @NotNull
    private IChangeCallback<Boolean> action = IChangeCallback::none;

    public Toggle(int x, int y, boolean value) {
        super(x, y, 30, 15);

        on = value;
    }

    @Override
    public Toggle onChange(@NotNull IChangeCallback<Boolean> action) {
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
        Text label = getStyle().getText();
        int labelWidth = MinecraftClient.getInstance().textRenderer.getWidth(label);

        bounds.width = labelWidth > 0 ? Math.max(bounds.width, width + 10 + labelWidth) : width;

        return bounds;
    }

    @Override
    public void onPress() {
        super.onPress();
        setValue(!on);
    }

    @Override
    protected void renderBackground(MatrixStack matrices, MinecraftClient mc, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(WIDGETS_TEXTURE);

        int sliderX = getX() + (on ? 1 : 0) * (width - 8);

        renderButtonBlit(matrices, sliderX, getY(), getTextureY(), 8, height);
    }

    @Override
    protected void renderForground(MatrixStack matrices, MinecraftClient mc, int mouseX, int mouseY, int foreColor) {
        int textY = getY() + mc.textRenderer.fontHeight / 2;
        int textX = getX() + width + 10;

        drawLabel(matrices, getStyle().getText(), textX, textY, foreColor, 0);
    }

    @Override
    protected int getTextureY() {
        return 46;
    }
}
