package com.minelittlepony.common.util.settings;

import com.minelittlepony.common.client.gui.IField.IChangeCallback;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A configuration container that lets you programmatically index values by a key.
 */
public abstract class Config {

    protected Map<String, Object> entries = new HashMap<>();

    /**
     * Inserts a series of settings values into this config for serialization.
     */
    protected void initWith(Setting<?>... settings) {
        for (Setting<?> s : settings) {
            entries.putIfAbsent(s.name().toLowerCase(), s.getDefault());
        }
    }

    /**
     * Commits any unsaved changes for this config.
     */
    public abstract void save();

    /**
     * Any value that can be stored in this config file.
     */
    public class Value<T> implements Setting<T> {
        private final T def;
        private final String name;

        public Value(String name, T def) {
            this.name = name;
            this.def = def;

            entries.putIfAbsent(name().toLowerCase(), def);
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
            return Config.this;
        }

        @Override
        public String toString() {
            return name();
        }
    }

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
                t = getDefault();

                set(t);
            }

            return t;
        }

        /**
         * Sets the config value associated with this entry.
         */
        default void set(@Nullable T value) {
            value = value == null ? getDefault() : value;
            config().entries.put(name().toLowerCase(), value);
        }

        @Override
        default T perform(T v) {
            set(v);
            return v;
        }
    }
}
