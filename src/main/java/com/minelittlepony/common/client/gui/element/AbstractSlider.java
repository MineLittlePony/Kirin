package com.minelittlepony.common.client.gui.element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.MinecraftClient;

import com.minelittlepony.common.client.gui.IField;

import java.util.function.Function;

public abstract class AbstractSlider<T> extends Button implements IField<T, AbstractSlider<T>> {

    private float min;
    private float max;

    private float value;

    @Nonnull
    private IChangeCallback<T> action = IChangeCallback::none;

    @Nullable
    private Function<T, String> formatter;

    public AbstractSlider(int x, int y, float min, float max, T value) {
        super(x, y);

        this.min = min;
        this.max = max;
        this.value = convertFromRange(valueToFloat(value), min, max);
    }

    protected abstract float valueToFloat(T value);

    protected abstract T floatToValue(float value);

    @Override
    public AbstractSlider<T> onChange(@Nonnull IChangeCallback<T> action) {
        this.action = action;
        return this;
    }

    public AbstractSlider<T> setFormatter(@Nonnull Function<T, String> formatter) {
        this.formatter = formatter;
        getStyle().setText(formatter.apply(getValue()));

        return this;
    }

    @Override
    public AbstractSlider<T> setValue(T value) {
        setClampedValue(convertFromRange(valueToFloat(value), min, max));

        return this;
    }

    protected void setClampedValue(float value) {
        value = clamp(value, 0, 1);

        if (value != this.value) {
            this.value = value;

            value = convertFromRange(valueToFloat(action.perform(getValue())), min, max);
            if (value != this.value) {
                this.value = value;

                if (formatter != null) {
                    getStyle().setText(formatter.apply(getValue()));
                }
            }
        }
    }

    private void onChange(double mouseX) {
        // convert pixel coordinate to range (0 - 1)
        setClampedValue((float)(mouseX - (x + 4)) / (width - 8));
    }

    @Override
    public T getValue() {
        return floatToValue(convertToRange(value, min, max));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        onChange(mouseX);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double mouseDX, double mouseDY) {
        onChange(mouseX);
    }

    @Override
    protected void renderBg(MinecraftClient mc, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(WIDGETS_LOCATION);

        int i = 46 + (isHovered() ? 2 : 1) * 20;
        int sliderX = x + (int)(value * (width - 8));

        blit(sliderX,     y, 0,   i, 4, 20);
        blit(sliderX + 4, y, 196, i, 4, 20);
    }

    @Override
    protected int getYImage(boolean mouseOver) {
        return 0;
    }

    static float convertFromRange(float value, float min, float max) {
        return (clamp(value, min, max) - min) / (max - min);
    }

    static float convertToRange(float value, float min, float max) {
        return clamp(min + (value * (max - min)), min, max);
    }

    static float clamp(float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }
}
