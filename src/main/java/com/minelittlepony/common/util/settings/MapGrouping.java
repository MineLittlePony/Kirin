package com.minelittlepony.common.util.settings;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

record MapGrouping(Map<String, Setting<?>> map) implements Grouping {
    @Override
    public Iterator<Entry<String, Setting<?>>> iterator() {
        return map.entrySet().iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Setting<T> get(String key) {
        return (Setting<T>)map.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public Iterable<Setting<?>> entries() {
        return map.values();
    }
}
