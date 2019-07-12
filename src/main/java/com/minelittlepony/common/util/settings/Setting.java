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

    /**
     * Gets the config value associated with this entry.
     */
    @Nonnull
    T get();

    /**
     * Sets the config value associated with this entry.
     */
    T set(@Nullable T value);

    @Override
    default T perform(T value) {
        return set(value);
    }
}