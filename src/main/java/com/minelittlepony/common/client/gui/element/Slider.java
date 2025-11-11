package com.minelittlepony.common.client.gui.element;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A slider for sliding.
 *
 * @author Sollace
 */
public class Slider extends AbstractSlider<Float> {

    private final float valueRange;

    public Slider(int x, int y, float min, float max, Supplier<? extends Number> value) {
        this(x, y, min, max, Objects.requireNonNull(value.get(), "value was null").floatValue());
    }

    public Slider(int x, int y, float min, float max, float value) {
        super(x, y, min, max, value);
        valueRange = (max - min);
    }

    @Override
    protected float valueToFloat(Float value) {
        return value;
    }

    @Override
    protected Float floatToValue(float value) {
        return value;
    }

    @Override
    protected Float nextValue(Float value, int steps) {
        float valuePerPixel = valueRange / (getWidth() - SLIDER_WIDTH);
        return value.floatValue() + (valuePerPixel * SLIDER_WIDTH * steps);
    }
}
