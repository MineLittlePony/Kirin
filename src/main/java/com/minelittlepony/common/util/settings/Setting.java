package com.minelittlepony.common.util.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.reflect.TypeToken;
import com.minelittlepony.common.client.gui.IField.IChangeCallback;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Any settings.
 */
public interface Setting<T> extends IChangeCallback<T> {
    String name();

    @NotNull
    default T getDefault() {
        return getType().defaultValue().get();
    }

    /**
     * The type of this setting.
     * Contains the default value and an optional element type.
     */
    @NotNull
    Type<T> getType();

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

    public record Type<T> (
            Supplier<T> defaultValue,
            Optional<Class<?>> elementType
    ) {
        @SuppressWarnings("unchecked")
        public Class<T> rawType() {
            return (Class<T>)defaultValue.get().getClass();
        }

        @SuppressWarnings("unchecked")
        public TypeToken<T> token() {
            if (elementType.isPresent()) {
                return (TypeToken<T>)TypeToken.getParameterized(rawType(), elementType.get());
            }
            return TypeToken.get(rawType());
        }
    }
}