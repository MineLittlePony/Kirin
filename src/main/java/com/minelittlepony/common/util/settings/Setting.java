package com.minelittlepony.common.util.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.minelittlepony.common.client.gui.IField.IChangeCallback;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Any settings.
 */
public interface Setting<T> extends IChangeCallback<T>, Supplier<T> {
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
    @Override
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
            Either<TypeToken<T>, Codec<T>> token
    ) {
        public static <T> Type<T> of(Supplier<T> defaultValue, Codec<T> codec) {
            return new Type<>(defaultValue, Either.right(codec));
        }

        @SuppressWarnings("unchecked")
        public static <T> Type<T> of(Supplier<T> defaultValue) {
            return new Type<>(defaultValue, Either.left(TypeToken.get((Class<T>)defaultValue.get().getClass())));
        }

        @SuppressWarnings("unchecked")
        public static <T> Type<T> of(Supplier<T> defaultValue, java.lang.reflect.Type...parameters) {
            return new Type<>(defaultValue, Either.left((TypeToken<T>)TypeToken.getParameterized(defaultValue.get().getClass(), parameters)));
        }

        @SuppressWarnings("unchecked")
        JsonElement write(Setting<?> value, Gson gson) throws IOException {
            return token().map(
                    token -> gson.toJsonTree(value, Setting.class),
                    codec ->  ((Codec<Object>)codec).encodeStart(JsonOps.INSTANCE, value.get()).getOrThrow()
            );
        }

        T read(Setting<T> setting, JsonElement value, Gson gson) {
            return token().map(
                token -> gson.getAdapter(token).fromJsonTree(value),
                codec -> codec.decode(JsonOps.INSTANCE, value).result().map(Pair::getFirst).orElseGet(setting::getDefault)
            );
        }
    }
}