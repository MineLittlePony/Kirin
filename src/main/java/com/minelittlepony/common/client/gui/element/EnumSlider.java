package com.minelittlepony.common.client.gui.element;

import net.minecraft.text.Text;

/**
 * Also a slider, but conveniently works with Enum values.
 *
 * @author Sollace
 */
public class EnumSlider<T extends Enum<T>> extends AbstractSlider<T> {

    private final T[] values;

    @SuppressWarnings("unchecked")
    public EnumSlider(int x, int y, T value) {
        super(x, y, 0, value.getClass().getEnumConstants().length - 1, value);
        values = (T[])value.getClass().getEnumConstants();

        setTextFormat(s -> Text.of(getValue().name()));
    }

    @Override
    protected float valueToFloat(T value) {
        return value.ordinal();
    }

    @Override
    protected T floatToValue(float value) {
        value = Math.round(value);

        while (value < 0) {
            value += values.length;
        }

        return values[(int)value % values.length];
    }
}
