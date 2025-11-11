package com.minelittlepony.common.client.gui.element;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.Cursor;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import com.minelittlepony.common.client.gui.IField;
import com.minelittlepony.common.client.gui.Tooltip;

import java.util.function.Function;

/**
 * Base class for a slider element.
 *
 * @author     Sollace
 *
 * @param <T> The value type for this slider.
 */
public abstract class AbstractSlider<T> extends Button implements IField<T, AbstractSlider<T>> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/slider");
    private static final Identifier HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/slider_highlighted");
    private static final Identifier HANDLE_TEXTURE = Identifier.ofVanilla("widget/slider_handle");
    private static final Identifier HANDLE_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/slider_handle_highlighted");

    protected static final ButtonTextures TEXTURES = new ButtonTextures(TEXTURE, TEXTURE, HIGHLIGHTED_TEXTURE);
    protected static final ButtonTextures HANDLE_TEXTURES = new ButtonTextures(HANDLE_TEXTURE, HANDLE_TEXTURE, HANDLE_HIGHLIGHTED_TEXTURE);

    public static final int SLIDER_WIDTH = 8;
    public static final int HALF_SLIDER_WIDTH = SLIDER_WIDTH / 2;

    private float min;
    private float max;

    private float value;

    private boolean handleFocused;

    @NotNull
    private IChangeCallback<T> action = IChangeCallback::none;

    @Nullable
    private Function<AbstractSlider<T>, Text> textFunc;
    @Nullable
    private Function<AbstractSlider<T>, Tooltip> tooltipFunc;

    public AbstractSlider(int x, int y, float min, float max, T value) {
        super(x, y);

        this.min = min;
        this.max = max;
        this.value = convertFromRange(valueToFloat(value), min, max);
    }

    protected abstract float valueToFloat(T value);

    protected abstract T floatToValue(float value);

    protected abstract T nextValue(T value, int steps);

    @Override
    public AbstractSlider<T> onChange(@NotNull IChangeCallback<T> action) {
        this.action = action;
        return this;
    }

    /**
     * Sets a function to use when formatting the slider's current value for display.
     *
     * @param formatter The formatting function to call.
     * @return {@code this} for chaining purposes
     */
    public AbstractSlider<T> setTextFormat(@NotNull Function<AbstractSlider<T>, Text> formatter) {
        this.textFunc = formatter;
        getStyle().setText(formatter.apply(this));

        return this;
    }
    /**
     * Sets a function to use when formatting the slider's current value for display in its tooltip.
     *
     * @param formatter The formatting function to call.
     * @return {@code this} for chaining purposes
     */
    public AbstractSlider<T> setTooltipFormat(@NotNull Function<AbstractSlider<T>, Tooltip> formatter) {
        this.tooltipFunc = formatter;
        getStyle().setTooltip(formatter.apply(this));

        return this;
    }

    @Override
    public AbstractSlider<T> setValue(T value) {
        setClampedValue(convertFromRange(valueToFloat(value), min, max));

        return this;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (active && visible && (input.isLeft() || input.isRight())) {
            playDownSound(MinecraftClient.getInstance().getSoundManager());
            setClampedValue(valueToFloat(nextValue(floatToValue(value), input.isLeft() ? -1 : 1)));
            onPress(input);

            return true;
        }
        return false;
    }

    protected void setClampedValue(float value) {
        value = MathHelper.clamp(value, 0, 1);

        if (value != this.value) {
            float initial = this.value;
            this.value = value;
            this.value = convertFromRange(valueToFloat(action.perform(getValue())), min, max);

            if (this.value != initial) {
                if (textFunc != null) {
                    getStyle().setText(textFunc.apply(this));
                }
                if (tooltipFunc != null) {
                    getStyle().setTooltip(tooltipFunc.apply(this));
                }
            }
        }
    }

    private void onChange(double mouseX) {
        // convert pixel coordinate to range (0 - 1)
        setClampedValue((float)(mouseX - (getX() + HALF_SLIDER_WIDTH)) / (getWidth() - SLIDER_WIDTH));
    }

    @Override
    public T getValue() {
        return floatToValue(convertToRange(value, min, max));
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        super.onClick(click, doubled);
        onChange(click.x());
    }

    @Override
    protected void onDrag(Click click, double mouseDX, double mouseDY) {
        onChange(click.x());
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            handleFocused = false;
        } else {
            GuiNavigationType guiNavigationType = MinecraftClient.getInstance().getNavigationType();
            handleFocused |= guiNavigationType == GuiNavigationType.MOUSE || guiNavigationType == GuiNavigationType.KEYBOARD_TAB;
        }
    }

    private int getSliderX() {
        return (int)(value * (getWidth() - SLIDER_WIDTH));
    }

    @Override
    protected Cursor getCursor(int mouseX, int mouseY) {
        mouseX -= getX();
        int sliderX = getSliderX();
        return mouseX >= sliderX && mouseX <= (sliderX + SLIDER_WIDTH) ? StandardCursors.RESIZE_EW : super.getCursor(mouseX, mouseY);
    }

    @Override
    protected void renderBackground(DrawContext context, MinecraftClient mc, int mouseX, int mouseY) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURES.get(active, isSelected() && !handleFocused), getX(), getY(), getWidth(), getHeight(), ColorHelper.getWhite(alpha));
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, HANDLE_TEXTURES.get(active, isSelected() && handleFocused), getX() + getSliderX(), getY(), 8, getHeight(), ColorHelper.getWhite(alpha));
    }

    @Override
    protected MutableText getNarrationMessage() {
        return Text.translatable("gui.narrate.slider", getMessage());
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        super.appendClickableNarrations(builder);
        builder.put(NarrationPart.TITLE, getNarrationMessage());
        if (active) {
            builder.put(NarrationPart.USAGE, Text.translatable("narration.slider.usage." + (isFocused() ? (handleFocused ? "focused" : "focused.keyboard_cannot_change_value") : "hovered")));
        }
    }

    static float convertFromRange(float value, float min, float max) {
        return (MathHelper.clamp(value, min, max) - min) / (max - min);
    }

    static float convertToRange(float value, float min, float max) {
        return MathHelper.clamp(min + (value * (max - min)), min, max);
    }
}
