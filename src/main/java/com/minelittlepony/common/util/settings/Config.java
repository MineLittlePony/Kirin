package com.minelittlepony.common.util.settings;

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
     * Initializes a new value for this config.
     */
    protected <T> Setting<T> value(String key, T def) {
        return new Value<>(this, key, def);
    }

    /**
     * Commits any unsaved changes for this config.
     */
    public abstract void save();
}
