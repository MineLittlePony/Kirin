package com.minelittlepony.common.client.gui.element;

import java.util.Objects;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.minelittlepony.common.client.gui.IField;
import com.minelittlepony.common.client.gui.dimension.Bounds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextConsumer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

/**
 * Implements a toggle (switch) element with two states (ON/OFF).
 *
 * @author     Sollace
 */
public class Toggle extends Button implements IField<Boolean, Toggle> {

    private boolean on;

    @NotNull
    private IChangeCallback<Boolean> action = IChangeCallback::none;

    public Toggle(int x, int y, Supplier<Boolean> value) {
        this(x, y, Objects.requireNonNull(value.get(), "value was null"));
    }

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
    public void onPress(AbstractInput input) {
        super.onPress(input);
        setValue(!on);
    }

    @Override
    protected void renderBackground(DrawContext context, MinecraftClient mc, int mouseX, int mouseY) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURES.get(false, isSelected()), getX(), getY(), getWidth(), getHeight(), ColorHelper.getWhite(alpha));
        int sliderX = getX() + (on ? getWidth() - 8 : 0);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURES.get(active, isSelected()), sliderX, getY(), 8, getHeight(), ColorHelper.getWhite(alpha));
    }

    @Override
    protected void renderForeground(DrawContext context, TextConsumer drawer, int mouseX, int mouseY) {
        Bounds bounds = getBounds();
        Text text = getStyle().getText();
        drawer.text(text, getX() + width + 10, bounds.right() - 2, bounds.top, bounds.bottom());
    }

    @Override
    protected MutableText getNarrationMessage() {
        return Text.translatable("narration.checkbox", getMessage());
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        super.appendClickableNarrations(builder);
        builder.put(NarrationPart.TITLE, getNarrationMessage());
        if (active) {
            builder.put(NarrationPart.USAGE, Text.translatable("narration.checkbox.usage." + (isFocused() ? "focused" : "hovered")));
        }
    }
}
