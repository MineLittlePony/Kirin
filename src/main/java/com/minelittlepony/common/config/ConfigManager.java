package com.minelittlepony.common.config;

import com.google.common.base.Preconditions;
import com.minelittlepony.common.util.GamePaths;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigManager {

    private static Set<String> configPaths = new HashSet<>();
    private static Map<Object, ConfigInstance> configs = new HashMap<>();

    public static <T extends JsonConfig> void register(String path, T config) {
        Preconditions.checkState(!configPaths.contains(path), "Duplicate configuration file!");
        Preconditions.checkState(!configs.containsKey(config), "Duplicate config instance!");

        Path configPath = GamePaths.getConfigDirectory().resolve(path);

        configPaths.add(path);
        configs.put(config, new ConfigInstance<>(configPath, config));

        try {
            load(config);
        } catch (IOException e) {
            LogManager.getLogger().warn("Unable to load config {}", config, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends JsonConfig> ConfigInstance<T> getInstance(T config) {
        return (ConfigInstance<T>) configs.get(config);
    }

    public static <T extends JsonConfig> Map<String, ValueSignature<?>> getCatagory(T config, String category) {
        return getInstance(config).getCatagory(category);
    }

    public static <T extends JsonConfig> void load(T config) throws IOException {
        getInstance(config).load();
    }

    public static <T extends JsonConfig> void save(T config) throws IOException {
        getInstance(config).save();
    }

}
