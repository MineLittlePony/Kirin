package com.minelittlepony.common.util.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minelittlepony.common.client.gui.IField.IChangeCallback;

import java.util.function.Consumer;

/**
 * Any settings.
 */
public interface Setting<T> extends IChangeCallback<T> {
    String name();

    @NotNull
    T getDefault();

    /**
     * Gets the config value associated with this entry.
     */
    @NotNull
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