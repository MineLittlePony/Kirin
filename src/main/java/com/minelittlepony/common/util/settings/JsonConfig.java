package com.minelittlepony.common.util.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonWriter;
import com.mojang.util.UUIDTypeAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A specialised configuration container that loads from a json file.
 */
public class JsonConfig extends Config {

    /**
     * Loads a new JsonConfig instance from the given file and
     * supplied by the provided constructor.
     *
     * @param <T>     The type to instantiate
     * @param file    The file to read from
     * @param creator The instantiator to use
     * @return A new instance.
     */
    public static <T extends JsonConfig> T of(Path file, Supplier<T> creator) {
        return creator.get().load(file);
    }

    static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeHierarchyAdapter(Path.class, new ToStringAdapter<>(Paths::get))
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .create();

    private Path configFile;

    @Override
    public void save() {
        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(configFile))) {
            writer.setIndent("    ");

            gson.toJson(entries, HashMap.class, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends JsonConfig> T load(Path file) {
        try {
            configFile = file;

            if (Files.exists(file)) {
                try (BufferedReader s = Files.newBufferedReader(file)) {
                    gson.fromJson(s, JsonObject.class).entrySet().forEach(entry -> {
                        String key = entry.getKey().toLowerCase();

                        if (entries.containsKey(key)) {
                            Object value = gson.getAdapter(entries.get(key).getClass()).fromJsonTree(entry.getValue());

                            entries.put(key, value);
                        }
                    });
                } catch (IOException | JsonParseException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            save();
        }

        return (T)this;
    }
}
