package com.minelittlepony.common.util.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonWriter;
import com.mojang.util.UUIDTypeAdapter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeirarchicalJsonConfigAdapter implements Config.Adapter {
    private static final Logger logger = LogManager.getLogger();

    private final Gson gson;

    public static final HeirarchicalJsonConfigAdapter DEFAULT = new HeirarchicalJsonConfigAdapter(new GsonBuilder());

    public HeirarchicalJsonConfigAdapter(GsonBuilder builder) {
        this.gson = builder
                .setPrettyPrinting()
                .registerTypeHierarchyAdapter(Path.class, new ToStringAdapter<>(Paths::get))
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeHierarchyAdapter(Setting.class, new SettingSerializer())
                .create();
    }

    @Override
    public void save(Config config, Path file) {
        try (BufferedWriter buffer = Files.newBufferedWriter(file);
             JsonWriter writer = gson.newJsonWriter(buffer)) {
           writer.beginObject();
           for (var category : config.categoryNames()) {
               writer.name(category);
               writer.beginObject();
               boolean second = false;
               for (var setting : config.getCategory(category)) {
                   for (var comment : setting.getValue().getComments()) {
                       if (second) {
                           buffer.write(',');
                       }
                       buffer.write(System.lineSeparator());
                       buffer.write("    // " + comment);
                   }
                   writer.name(setting.getKey());
                   gson.toJson(setting.getValue(), Setting.class, writer);
                   second = true;
               }
               writer.endObject();
           }
           writer.endObject();
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
                        config.getCategoryOrEmpty(entry.getKey()).ifPresent(category -> {
                            entry.getValue().getAsJsonObject().entrySet().forEach(tuple -> {
                                category.getOrEmpty(tuple.getKey().toLowerCase()).ifPresent(setting -> {
                                    setting.set(
                                        gson.getAdapter(setting.getType().token()).fromJsonTree(tuple.getValue())
                                    );
                                });
                            });
                        });
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
