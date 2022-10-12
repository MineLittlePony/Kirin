package com.minelittlepony.common.util.settings;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Grouping extends Iterable<Map.Entry<String, Setting<?>>> {
    Grouping EMPTY = ofMap(Map.of());

    <T> Setting<T> get(String key);

    boolean containsKey(String key);

    Iterable<Setting<?>> entries();

    default <T> Optional<Setting<T>> getOrEmpty(String key) {
        return Optional.ofNullable(get(key));
    }

    default Stream<Setting<?>> stream() {
        return StreamSupport.stream(entries().spliterator(), false);
    }

    static Grouping ofMap(Map<String, Setting<?>> map) {
        return new MapGrouping(map);
    }
}
