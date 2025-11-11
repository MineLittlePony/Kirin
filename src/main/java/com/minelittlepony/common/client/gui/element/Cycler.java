package com.minelittlepony.common.client.gui.element;

import org.jetbrains.annotations.NotNull;

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
 * styles as you toggle through its different states.
 *
 * @author     Sollace
 *
 */
public class Cycler extends Button implements IMultiStyled<Cycler>, IField<Integer, Cycler> {

    private Style[] styles = new Style[] {
            getStyle()
    };

    private int value;

    @NotNull
    private IChangeCallback<Integer> action = IChangeCallback::none;

    public Cycler(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public Cycler onChange(IChangeCallback<Integer> action) {
        this.action = action;

        return this;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public Cycler setValue(Integer value) {
        if (this.value != value) {
            this.value = action.perform(value) % styles.length;
            this.setStyle(styles[this.value]);
        }

        return this;
    }

    /**
     * Sets the styles to use for each state this toggle is able to be in.
     * The number of styles here determines the number of possible states
     * and the value is the index to the array of styles.
     */
    @Override
    public Cycler setStyles(Style... styles) {
        this.styles = styles;

        value = value % styles.length;
        setStyle(styles[value]);

        return this;
    }

    @Override
    public Style[] getStyles() {
        return styles;
    }

    @Override
    public void onPress(AbstractInput input) {
        setValue(value + 1);
        super.onPress(input);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return Text.translatable("gui.narrate.cycle_button", getMessage());
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        super.appendClickableNarrations(builder);
        builder.put(NarrationPart.TITLE, getNarrationMessage());
        if (active) {
            Text nextValue = (styles[(value + 1) % styles.length]).getText();
            builder.put(NarrationPart.USAGE, Text.translatable("narration.cycle_button.usage." + (isFocused() ? "focused" : "hovered"), nextValue));
        }
    }
}
