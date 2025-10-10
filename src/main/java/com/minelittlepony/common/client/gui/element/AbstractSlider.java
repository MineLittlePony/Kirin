package com.minelittlepony.common.client.gui.element;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
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

    private float min;
    private float max;

    private float value;

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

            float step = (max - min) / 4F;

            if (input.isLeft()) {
                step *= -1;
            }

            setClampedValue(value + step);
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
        setClampedValue((float)(mouseX - (getX() + 4)) / (getWidth() - 8));
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
    protected void renderBackground(DrawContext context, MinecraftClient mc, int mouseX, int mouseY) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURES.get(false, isSelected()), getX(), getY(), getWidth(), getHeight(), ColorHelper.getWhite(alpha));
        int sliderX = getX() + (int)(value * (getWidth() - 8));
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURES.get(active, isSelected()), sliderX, getY(), 8, getHeight(), ColorHelper.getWhite(alpha));
    }

    static float convertFromRange(float value, float min, float max) {
        return (MathHelper.clamp(value, min, max) - min) / (max - min);
    }

    static float convertToRange(float value, float min, float max) {
        return MathHelper.clamp(min + (value * (max - min)), min, max);
    }
}
