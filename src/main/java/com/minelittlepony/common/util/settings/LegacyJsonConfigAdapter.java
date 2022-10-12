package com.minelittlepony.common.util.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.util.UUIDTypeAdapter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Deprecated
public class LegacyJsonConfigAdapter implements Config.Adapter {
    private static final Logger logger = LogManager.getLogger();

    private final Gson gson;

    public static final LegacyJsonConfigAdapter DEFAULT = new LegacyJsonConfigAdapter(new GsonBuilder());

    public LegacyJsonConfigAdapter(GsonBuilder builder) {
        this.gson = builder
                .setPrettyPrinting()
                .registerTypeHierarchyAdapter(Path.class, new ToStringAdapter<>(Paths::get))
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeHierarchyAdapter(Setting.class, new SettingSerializer())
                .create();
    }

    @Override
    public void save(Config config, Path file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            gson.toJson(config.categories().map(Map.Entry::getValue).flatMap(Grouping::stream).collect(Collectors.toMap(
                    Setting::name,
                    s -> s
            )), HashMap.class, writer);
        } catch (IOException e) {
            logger.warn("Error whilst saving Json config", e);
        }
    }

    @Override
    public void load(Config config, Path file) {
        try {
            if (Files.isReadable(file)) {
                try (BufferedReader s = Files.newBufferedReader(file)) {
                    gson.fromJson(s, JsonObject.class).entrySet().forEach(entry -> {
                        String key = entry.getKey().toLowerCase();

                        Setting<Object> setting = config.get(key);
                        if (setting != null) {
                            setting.set(gson.getAdapter(setting.getDefault().getClass()).fromJsonTree(entry.getValue()));
                        }
                    });
                } catch (IOException | JsonParseException e) {
                    logger.warn("Erorr whilst loading json config", e);
                }
            }
        } finally {
            save(config, file);
        }
    }
}
