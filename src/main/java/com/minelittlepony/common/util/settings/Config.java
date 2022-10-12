package com.minelittlepony.common.util.settings;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * A configuration container that lets you programmatically index values by a key.
 */
public abstract class Config implements Iterable<Grouping> {
    @SuppressWarnings("deprecation")
    public static final Adapter FLATTENED_JSON_ADAPTER = LegacyJsonConfigAdapter.DEFAULT;
    public static final Adapter HEIRARCHICAL_JSON_ADAPTER = HeirarchicalJsonConfigAdapter.DEFAULT;

    private final Map<String, Grouping> categories = new HashMap<>();

    private final Adapter adapter;
    private final Path path;

    protected Config(Adapter adapter, Path path) {
        this.adapter = adapter;
        this.path = path;
    }

    /**
     * Initializes a new value for this config.
     */
    protected <T> Setting<T> value(String key, T def) {
        return value("root", key, def);
    }

    /**
     * Initializes a new value for this config and assigns it to a named category.
     */
    @SuppressWarnings("unchecked")
    protected <T> Setting<T> value(String category, String key, T def) {
        return (Setting<T>)((MapGrouping)categories.computeIfAbsent(category, c -> new MapGrouping(new HashMap<>())))
                .map()
                .computeIfAbsent(key.toLowerCase(), k -> new Value<>(k, def));
    }

    @Deprecated
    public Iterable<Setting<?>> getByCategory(String category) {
        return categories.get(category).entries();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public <T> Setting<T> get(String key) {
        return (Setting<T>)categories.values().stream().flatMap(c -> c.stream()).filter(entry -> entry.name().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

    @Deprecated
    public boolean containsKey(String key) {
        return get(key) != null;
    }

    public Iterable<String> categoryNames() {
        return categories.keySet();
    }

    public Grouping getCategory(String categoryName) {
        return categories.getOrDefault(categoryName, Grouping.EMPTY);
    }

    public Optional<Grouping> getCategoryOrEmpty(String categoryName) {
        return categories.containsKey(categoryName) ? Optional.of(categories.get(categoryName)) : Optional.empty();
    }

    public Stream<Map.Entry<String, Grouping>> categories() {
        return categories.entrySet().stream();
    }

    @Override
    public Iterator<Grouping> iterator() {
        return categories.values().iterator();
    }

    /**
     * Commits any unsaved changes for this config.
     */
    public void save() {
        adapter.save(this, path);
    }

    public void load() {
        adapter.load(this, path);
    }

    public interface Adapter {
        void load(Config config, Path file);

        void save(Config config, Path file);
    }
}
