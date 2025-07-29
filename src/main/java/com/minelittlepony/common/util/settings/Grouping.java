package com.minelittlepony.common.util.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Grouping extends Iterable<Map.Entry<String, Setting<?>>> {
    Grouping EMPTY = ofMap(Map.of());

    /**
     * Gets the named setting
     */
    <T> Setting<T> get(String key);

    /**
     * Checks whether a setting exists for the given key
     */
    boolean containsKey(String key);

    /**
     * Gets an iterator of all this group's settings.
     */
    Iterable<Setting<?>> entries();

    /**
     * Optionally gets a named setting
     */
    default <T> Optional<Setting<T>> getOrEmpty(String key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Gets a stream of all of the settings belonging to this grouping.
     */
    default Stream<Setting<?>> stream() {
        return StreamSupport.stream(entries().spliterator(), false);
    }

    /**
     * An optional comment to include alongside this grouping.
     */
    List<String> getComments();

    /**
     * Adds a comment to this group. If a comment already exists, will append as another line.
     */
    Grouping addComment(String comment);

    /**
     * Creates a grouping from a map.
     */
    static Grouping ofMap(Map<String, Setting<?>> map) {
        return new MapGrouping(map, new ArrayList<>());
    }
}
