package com.minelittlepony.common.util.settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

/**
 * Any value that can be stored in this config file.
 */
class Value<T> implements Setting<T> {

    private T value;

    private transient final T def;
    private transient final String name;

    public Value(Config config, String name, T def) {
        this.name = name;
        this.def = Preconditions.checkNotNull(def);
        this.value = def;

        config.entries.putIfAbsent(name().toLowerCase(), this);
    }

    @Override
    @Nonnull
    public T get() {
        if (value == null) {
            return set(getDefault());
        }

        return value;
    }

    @Override
    public T set(@Nullable T value) {
        this.value = value == null ? getDefault() : value;
        return this.value;
    }

    @Override
    public String name() {
        return name;
    }

    @Nonnull
    @Override
    public T getDefault() {
        return def;
    }

    @Override
    public String toString() {
        return name();
    }
}