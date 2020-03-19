package com.minelittlepony.common.client.gui.style;

import com.minelittlepony.common.util.MoreStreams;

/**
 * Represents an element that can have more than one style.
 *
 * @author     Sollace
 *
 * @param <T>
 */
public interface IMultiStyled<T extends IMultiStyled<T>> {

    /**
     * Sets this element's styles, copied from the given list of elements.
     */
    default T setStyles(IStyleFactory... styles) {
        return setStyles(MoreStreams.map(styles, IStyleFactory::getStyle, Style[]::new));
    }

    /**
     * Sets this element's styles.
     */
    T setStyles(Style... styles);

    /**
     * Gets all the styles this element is able to use.
     * @return
     */
    Style[] getStyles();
}
