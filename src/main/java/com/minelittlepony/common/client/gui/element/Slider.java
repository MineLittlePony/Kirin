package com.minelittlepony.common.client.gui.element;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A slider for sliding.
 *
 * @author Sollace
 */
public class Slider extends AbstractSlider<Float> {

    public Slider(int x, int y, float min, float max, Supplier<Float> value) {
        this(x, y, min, max, Objects.requireNonNull(value.get(), "value was null"));
    }

    public Slider(int x, int y, float min, float max, float value) {
        super(x, y, min, max, value);
    }

    @Override
    protected float valueToFloat(Float value) {
        return value;
    }

    @Override
    protected Float floatToValue(float value) {
        return value;
    }
}
