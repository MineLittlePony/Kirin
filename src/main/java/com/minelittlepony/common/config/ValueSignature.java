package com.minelittlepony.common.config;

import com.google.common.base.MoreObjects;

import java.lang.reflect.Type;

public final class ValueSignature<T> {
    public final Type type;
    public final Value<T> value;

    ValueSignature(Type type, Value<T> value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("value", value)
                .toString();
    }
}
