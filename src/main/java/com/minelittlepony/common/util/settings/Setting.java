package com.minelittlepony.common.util.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minelittlepony.common.client.gui.IField.IChangeCallback;

import java.util.List;
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
     * An optional comment to include alongside this setting.
     */
    List<String> getComments();

    /**
     * Adds a comment to this setting. If a comment already exists, will append as another line.
     */
    Setting<T> addComment(String comment);

    /**
     * Adds a change listener which gets called when {@link #set} is called.
     */
    void onChanged(Consumer<T> listener);

    @Override
    default T perform(T value) {
        return set(value);
    }
}