package com.minelittlepony.common.util.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A configuration container that lets you programmatically index values by a key.
 */
public abstract class Config {

    protected Map<String, Setting<?>> entries = new HashMap<>();

    private Map<String, List<Setting<?>>> categories = new HashMap<>();

    /**
     * Initializes a new value for this config.
     */
    protected <T> Setting<T> value(String key, T def) {
        return new Value<>(this, key, def);
    }

    /**
     * Initializes a enw value for this config and assigns it to a named category.
     */
    protected <T> Setting<T> value(String category, String key, T def) {
        Setting<T> setting = value(key, def);

        categories.computeIfAbsent(category,c -> new ArrayList<>()).add(setting);

        return setting;
    }

    public Iterable<Setting<?>> getByCategory(String category) {
        return categories.get(category);
    }

    @SuppressWarnings("unchecked")
    public <T> Setting<T> get(String key) {
        return (Setting<T>)entries.get(key);
    }

    /**
     * Commits any unsaved changes for this config.
     */
    public abstract void save();
}
