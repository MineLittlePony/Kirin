package com.minelittlepony.common.client.gui.element;

import javax.annotation.Nonnull;

import com.minelittlepony.common.client.gui.IField;
import com.minelittlepony.common.client.gui.style.IMultiStyled;
import com.minelittlepony.common.client.gui.style.Style;

/**
 * Represents a toggle button that switches between different
 * styles as you toggle between its different states.
 * <p>
 * "Iconic" here refers to how it uses an icon instead of text
 *
 * @author     Sollace
 *
 */
public class IconicToggle extends Button implements IMultiStyled<IconicToggle>, IField<Integer, IconicToggle> {

    private Style[] styles = new Style[] {
            getStyle()
    };

    private int value;

    @Nonnull
    private IChangeCallback<Integer> action = IChangeCallback::none;

    public IconicToggle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public IconicToggle onChange(IChangeCallback<Integer> action) {
        this.action = action;

        return this;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public IconicToggle setValue(Integer value) {
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
    public IconicToggle setStyles(Style... styles) {
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
    public void onClick(double mouseX, double mouseY) {
        setValue(value + 1);
        super.onClick(mouseX, mouseY);
    }
}
