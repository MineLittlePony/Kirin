package com.minelittlepony.common.util.registry;

import java.io.IOException;
import java.util.Locale;
import java.util.function.BiFunction;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegistryTypeAdapter<T> extends TypeAdapter<T> {

    private final Registry<T> registry;

    public static <T> TypeAdapter<T> of(Registry<T> registry) {
        return new RegistryTypeAdapter<>(registry);
    }

    public static <T> TypeAdapter<T> of(Registry<T> registry, BiFunction<String, Registry<T>, T> defaultValue) {
        return new RegistryTypeAdapter<>(registry) {
            @Override
            public T read(JsonReader in) throws IOException {
                String s = in.nextString();
                if (s == null) {
                    return null;
                }
                String ls = s.toLowerCase(Locale.US);
                return registry.getOrEmpty(new Identifier(ls)).orElseGet(() -> defaultValue.apply(ls, registry));
            }
        };
    }

    protected RegistryTypeAdapter(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        if (value != null) {
            Identifier id = registry.getId(value);
            if (id != null) {
                out.value(id.toString());
                return;
            }
        }
        out.nullValue();
    }

    @Override
    public T read(JsonReader in) throws IOException {
        String s = in.nextString();
        return s == null ? null : registry.get(new Identifier(s.toLowerCase(Locale.ROOT)));
    }
}
