package com.minelittlepony.common.util.settings;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public final class ToStringAdapter<T> extends TypeAdapter<T> {

    private final Function<T, String> toString;
    private final Function<String, T> fromString;

    public ToStringAdapter(Function<T, String> toString, Function<String, T> fromString) {
        this.toString = toString;
        this.fromString = fromString;
    }

    public ToStringAdapter(Function<String, T> converter) {
        this(Objects::toString, converter);
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        out.value(value == null ? null : toString.apply(value));
    }

    @Override
    public T read(JsonReader in) throws IOException {
        String value = in.nextString();
        return value == null ? null : fromString.apply(value);
    }
}
