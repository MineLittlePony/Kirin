package com.minelittlepony.common.config;

import com.google.common.base.MoreObjects;

/**
 * Any value that can be stored in this config file.
 */
public class Value<T> {

    private final T def;

    private T value;

    public Value(T def) {
        this.def = def;
        this.value = def;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public T getDefault() {
        return def;
    }

    public void reset() {
        set(def);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("def", def)
                .add("value", value)
                .toString();
    }
}