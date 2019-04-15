package com.minelittlepony.common;

import com.minelittlepony.common.client.gui.IField.IChangeCallback;

/**
 * A sensible config container that actually lets us programmatically index values by a key.
 *
 * Reflection because Mumfrey pls.
 *
 */
// Mumfrey pls.
public abstract class SensibleConfig {

    private static SensibleConfig instance;

    public SensibleConfig() {
        instance = this;
    }

    public abstract void save();

    public interface Setting extends IChangeCallback<Boolean> {
        String name();

        /**
         * Gets the config value associated with this entry.
         */
        default boolean get() {
            return instance.getValue(this);
        }

        /**
         * Sets the config value associated with this entry.
         */
        default void set(boolean value) {
            instance.setValue(this, value);
        }

        @Override
        default Boolean perform(Boolean v) {
            set(v);
            return v;
        }
    }

    public boolean getValue(Setting key) {
        try {
            return getClass().getField(key.name().toLowerCase()).getBoolean(this);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ignored) {
            return true;
        }
    }

    public boolean setValue(Setting key, boolean value) {
        try {
            getClass().getField(key.name().toLowerCase()).setBoolean(this, value);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ignored) {
        }
        return value;
    }
}
