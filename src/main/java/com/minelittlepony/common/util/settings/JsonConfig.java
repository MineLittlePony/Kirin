package com.minelittlepony.common.util.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.util.UUIDTypeAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A specialised configuration container that loads from a json file.
 */
public class JsonConfig extends Config {

    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeHierarchyAdapter(Path.class, new ToStringAdapter<>(Paths::get))
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeHierarchyAdapter(Setting.class, new SettingSerializer())
            .create();

    private transient final Path configFile;

    public JsonConfig(Path path) {
        configFile = path;
    }

    @Override
    public void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
            gson.toJson(entries, HashMap.class, writer);
        } catch (IOException e) {
            logger.warn("Error whilst saving Json config", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        try {
            if (Files.isReadable(configFile)) {
                try (BufferedReader s = Files.newBufferedReader(configFile)) {
                    gson.fromJson(s, JsonObject.class).entrySet().forEach(entry -> {
                        String key = entry.getKey().toLowerCase();

                        if (entries.containsKey(key)) {
                            Setting<Object> setting = (Setting<Object>)entries.get(key);

                            setting.set(gson.getAdapter(setting.getDefault().getClass()).fromJsonTree(entry.getValue()));
                        }
                    });
                } catch (IOException | JsonParseException e) {
                    logger.warn("Erorr whilst loading json config", e);
                }
            }
        } finally {
            save();
        }
    }
}
