package com.minelittlepony.common.util.settings;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public final class ToStringAdapter<T> extends TypeAdapter<T> {

    private final Function<String, T> converter;

    public ToStringAdapter(Function<String, T> converter) {
        this.converter = converter;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        out.value(value == null ? null : Objects.toString(value));
    }

    @Override
    public T read(JsonReader in) throws IOException {
        String value = in.nextString();
        return value == null ? null : converter.apply(value);
    }
}
