package com.minelittlepony.common.util.settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.minelittlepony.common.client.gui.IField.IChangeCallback;

import java.util.function.Consumer;

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

    /**
     * Adds a change listener which gets called when {@link #set} is called.
     */
    void onChanged(Consumer<T> listener);

    @Override
    default T perform(T value) {
        return set(value);
    }
}