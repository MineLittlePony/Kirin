package com.minelittlepony.common.util.settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.minelittlepony.common.client.gui.IField.IChangeCallback;

/**
 * Any settings.
 */
public interface Setting<T> extends IChangeCallback<T> {
    String name();

    @Nonnull
    T getDefault();

    Config config();

    /**
     * Gets the config value associated with this entry.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default T get() {
        T t = (T)config().entries.computeIfAbsent(name().toLowerCase(), k -> getDefault());

        if (t == null) {
            return set(getDefault());
        }

        return t;
    }

    /**
     * Sets the config value associated with this entry.
     */
    default T set(@Nullable T value) {
        value = value == null ? getDefault() : value;
        config().entries.put(name().toLowerCase(), value);
        return value;
    }

    @Override
    default T perform(T value) {
        return set(value);
    }
}