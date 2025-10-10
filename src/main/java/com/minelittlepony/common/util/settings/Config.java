package com.minelittlepony.common.util.settings;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.minelittlepony.common.util.io.PathMonitor;
import com.mojang.serialization.Codec;

/**
 * A configuration container that lets you programmatically index values by a key.
 */
public abstract class Config implements Iterable<Grouping> {
    /**
     * @deprecated Will be removed in MC1.22. Use HEIRARCHICAL_JSON_ADAPTER instead! It's backwards compatible! :D
     */
    @Deprecated(forRemoval = true)
    public static final Adapter FLATTENED_JSON_ADAPTER = LegacyJsonConfigAdapter.DEFAULT;
    public static final Adapter HEIRARCHICAL_JSON_ADAPTER = HeirarchicalJsonConfigAdapter.DEFAULT;

    private final Map<String, Grouping> categories = new HashMap<>();

    private final Adapter adapter;
    private final Path path;

    private final List<Consumer<Config>> listeners = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private final PathMonitor monitor = new PathMonitor(event -> {
        switch (event) {
            case MODIFY:
                load();
                break;
            case DELETE:
                categories.forEach((name, category) -> {
                    category.entries().forEach(setting -> {
                        ((Setting<Object>)setting).set(setting.getDefault());
                    });
                });
        }
        listeners.forEach(listener -> listener.accept(this));
    });

    protected Config(Adapter adapter, Path path) {
        this.adapter = adapter;
        this.path = path;
        monitor.set(path, adapter.getAlternatives(path).toList());
    }

    public void onChangedExternally(Consumer<Config> listener) {
        listeners.add(listener);
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
    protected <T> Setting<T> value(String category, String key, T def) {
        return value(category, key, Value.Type.of(() -> def));
    }

    /**
     * Initializes a new value for this config and assigns it to a named category.
     */
    protected <T, C extends Collection<T>> Setting<C> value(String category, String key, Supplier<C> def, Class<T> elementType) {
        return value(category, key, Value.Type.of(def, elementType));
    }

    /**
     * Initializes a new value for this config and assigns it to a named category.
     */
    protected <K, V, C extends Map<K, V>> Setting<C> value(String category, String key, Supplier<C> def, Class<K> keyType, Class<V> valueType) {
        return value(category, key, Value.Type.of(def, keyType, valueType));
    }

    /**
     * Initializes a new value for this config and assigns it to a named category.
     */
    protected <V> Setting<V> value(String category, String key, Supplier<V> def, Codec<V> codec) {
        return value(category, key, Value.Type.of(def, codec));
    }

    /**
     * Initializes a new value for this config and assigns it to a named category.
     */
    @SuppressWarnings("unchecked")
    protected <T> Setting<T> value(String category, String key, Value.Type<T> type) {
        return (Setting<T>)((MapGrouping)categories.computeIfAbsent(category, c -> new MapGrouping(new HashMap<>(), new ArrayList<>())))
                .map()
                .computeIfAbsent(key.toLowerCase(), k -> new Value<>(key, type));
    }

    /**
     * @deprecated Will be removed in MC1.22. Use getCategory(category) instead!
     */
    @Deprecated(forRemoval = true)
    public Iterable<Setting<?>> getByCategory(String category) {
        return categories.get(category).entries();
    }

    /**
     * @deprecated Will be removed in MC1.22. Use getCategory(category).get(key) instead!
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings("unchecked")
    public <T> Setting<T> get(String key) {
        return (Setting<T>)categories.values().stream().flatMap(c -> c.stream()).filter(entry -> entry.name().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

    /**
     * @deprecated Will be removed in MC1.22. Use getCategory(category).containsKey(key) instead!
     */
    @Deprecated(forRemoval = true)
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
        monitor.wrap(() -> adapter.save(this, path));
    }

    public void load() {
        monitor.wrap(() -> adapter.load(this, path));
    }

    public interface Adapter {

        default Stream<Path> getAlternatives(Path file) {
            return Stream.of(file);
        }

        void load(Config config, Path file);

        void save(Config config, Path file);
    }
}
