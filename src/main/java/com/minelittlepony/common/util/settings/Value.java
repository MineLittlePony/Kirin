package com.minelittlepony.common.util.settings;

import javax.annotation.Nonnull;

/**
 * Any value that can be stored in this config file.
 */
class Value<T> implements Setting<T> {
    private final Config config;

    private final T def;
    private final String name;

    public Value(Config config, String name, T def) {
        this.config = config;
        this.name = name;
        this.def = def;

        this.config.entries.putIfAbsent(name().toLowerCase(), def);
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
    public Config config() {
        return config;
    }

    @Override
    public String toString() {
        return name();
    }
}