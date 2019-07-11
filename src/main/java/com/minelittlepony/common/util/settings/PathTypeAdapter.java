package com.minelittlepony.common.util.settings;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.common.base.Strings;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

final class PathTypeAdapter extends TypeAdapter<Path> {
    @Override
    public void write(JsonWriter out, Path value) throws IOException {
        out.value(value == null ? "/" : value.toString());
    }

    @Override
    public Path read(JsonReader in) throws IOException {
        String s = Strings.nullToEmpty(in.nextString());
        return Paths.get(s.isEmpty() ? "/" : s);
    }
}
