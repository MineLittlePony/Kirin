package com.minelittlepony.common.client.gui.element;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minelittlepony.common.client.gui.IField;
import com.minelittlepony.common.client.gui.style.IMultiStyled;
import com.minelittlepony.common.client.gui.style.Style;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * Represents a toggle button that switches between different
 * values as you toggle through its different states.
 *
 * @author     Sollace
 *
 */
public class Selector<T> extends Button implements IMultiStyled<Selector<T>>, IField<T, Selector<T>> {
    private final Style defaultStyle;
    private final T defaultValue;
    private T value;

    @NotNull
    private IChangeCallback<T> action = IChangeCallback::none;

    private final Function<T, Style> styleFunction;

    public Selector(int x, int y, int width, int height, T value, Function<T, Style> styleFunction) {
        super(x, y, width, height);
        this.defaultValue = value;
        this.value = value;
        this.styleFunction = styleFunction;
        this.defaultStyle = styleFunction.apply(value);
        setStyle(this.defaultStyle);
    }

    @Override
    public Selector<T> onChange(IChangeCallback<T> action) {
        this.action = action;

        return this;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public Selector<T> setValue(@Nullable T value) {
        this.value = value == null ? defaultValue : value;
        this.setStyle(styleFunction.apply(this.value));

        return this;
    }

    /**
     * Sets the styles to use for each state this toggle is able to be in.
     * The number of styles here determines the number of possible states
     * and the value is the index to the array of styles.
     */
    @Override
    public Selector<T> setStyles(Style... styles) {
        return this;
    }

    @Override
    public Style[] getStyles() {
        return new Style[] {getStyle()};
    }

    @Override
    public void onPress(AbstractInput input) {
        setValue(action.perform(getValue()));
        super.onPress(input);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return Text.translatable("gui.narrate.selector", getMessage());
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        super.appendClickableNarrations(builder);
        builder.put(NarrationPart.TITLE, getNarrationMessage());
        if (active) {
            builder.put(NarrationPart.USAGE, Text.translatable("narration.selector.usage." + (isFocused() ? "focused" : "hovered")));
        }
    }
}
