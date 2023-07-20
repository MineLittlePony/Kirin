package com.minelittlepony.common.client.gui.style;

import java.util.function.Function;

public interface IStyled<T extends IStyled<T>> extends IStyleFactory {

    default T styled(Function<Style, Style> styleChange) {
        return setStyle(styleChange.apply(getStyle()));
    }

    default T setStyle(IStyleFactory factory) {
        return setStyle(factory.getStyle());
    }

    T setStyle(Style style);
}
