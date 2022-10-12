package com.minelittlepony.common.util.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.function.Consumer;

/**
 * Any value that can be stored in this config file.
 */
class Value<T> implements Setting<T> {
    private T value;

    private transient final T def;
    private transient final String name;

    private transient List<String> comment = new ArrayList<>();

    private transient final List<Consumer<T>> listeners = new ArrayList<>();

    public Value(String name, T def) {
        this.name = name;
        this.def = Preconditions.checkNotNull(def);
        this.value = def;
    }

    @Override
    public void onChanged(Consumer<T> listener) {
        this.listeners.add(listener);
    }

    @Override
    @NotNull
    public T get() {
        if (value == null) {
            return set(getDefault());
        }

        return value;
    }

    @Override
    public T set(@Nullable T value) {
        this.value = value == null ? getDefault() : value;
        listeners.forEach(l -> l.accept(this.value));
        return this.value;
    }

    @Override
    public String name() {
        return name;
    }

    @NotNull
    @Override
    public T getDefault() {
        return def;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public List<String> getComments() {
        return comment;
    }

    @Override
    public Setting<T> addComment(String comment) {
        this.comment.add(comment);
        return this;
    }
}