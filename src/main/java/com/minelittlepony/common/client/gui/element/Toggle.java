package com.minelittlepony.common.client.gui.element;

import java.util.Objects;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.minelittlepony.common.client.gui.IField;
import com.minelittlepony.common.client.gui.dimension.Bounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;

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
        Component label = getStyle().getText();
        int labelWidth = Minecraft.getInstance().font.width(label);

        bounds.width = labelWidth > 0 ? Math.max(bounds.width, width + 10 + labelWidth) : width;

        return bounds;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        super.onPress(input);
        setValue(!on);
    }

    @Override
    protected void renderBackground(GuiGraphics context, Minecraft mc, int mouseX, int mouseY) {
        context.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURES.get(false, isFocused()), getX(), getY(), getWidth(), getHeight(), ARGB.white(alpha));
        int sliderX = getX() + (on ? getWidth() - 8 : 0);
        context.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURES.get(active, isFocused()), sliderX, getY(), 8, getHeight(), ARGB.white(alpha));
    }

    @Override
    protected void renderForeground(GuiGraphics context, ActiveTextCollector drawer, int mouseX, int mouseY) {
        Bounds bounds = getBounds();
        Component text = getStyle().getText();
        drawer.acceptScrollingWithDefaultCenter(text, getX() + width + 10, bounds.right() - 2, bounds.top, bounds.bottom());
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return Component.translatable("narration.checkbox", getMessage());
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput builder) {
        super.updateWidgetNarration(builder);
        builder.add(NarratedElementType.TITLE, createNarrationMessage());
        if (active) {
            builder.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage." + (isFocused() ? "focused" : "hovered")));
        }
    }
}
