package com.minelittlepony.common.util.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.predicate.NumberRange.DoubleRange;
import net.minecraft.predicate.NumberRange.IntRange;
import net.minecraft.util.Identifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeirarchicalJsonConfigAdapter implements Config.Adapter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String[] JSON_FORMATS = new String[] {"json5", "json"};

    private final Gson gson;

    public static final HeirarchicalJsonConfigAdapter DEFAULT = new HeirarchicalJsonConfigAdapter(new GsonBuilder());

    public HeirarchicalJsonConfigAdapter(GsonBuilder builder) {
        this.gson = builder
                .setStrictness(Strictness.LENIENT)
                .setPrettyPrinting()
                .registerTypeHierarchyAdapter(Path.class, new ToStringAdapter<>(Paths::get))
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeHierarchyAdapter(Setting.class, new SettingSerializer())
                .registerTypeAdapter(Identifier.class, new ToStringAdapter<>(Identifier::of))
                .registerTypeAdapter(IntRange.class, new CodecTypeAdapter<>(IntRange.CODEC))
                .registerTypeAdapter(DoubleRange.class, new CodecTypeAdapter<>(DoubleRange.CODEC))
                .create();
    }

    @Override
    public Stream<Path> getAlternatives(Path file) {
        return FileUtils.getAlternatives(file, JSON_FORMATS);
    }

    @Override
    public void save(Config config, Path file) {
        try (Json5Writer writer = new Json5Writer(Files.newBufferedWriter(correctExtension(file).orElse(FileUtils.changeExtension(file, "json5"))), gson)) {
           writer.beginObject();
           for (var category : config.categoryNames()) {
               var cat = config.getCategory(category);
               for (var comment : cat.getComments()) {
                   writer.comment(comment);
               }
               writer.name(category);
               writer.beginObject();
               for (var setting : cat) {
                   for (var comment : setting.getValue().getComments()) {
                       writer.comment(comment);
                   }
                   writer.name(setting.getValue().name());
                   Streams.write(setting.getValue().getType().write(setting.getValue(), gson), writer);
               }
               writer.endObject();
           }
           writer.endObject();
       } catch (Throwable e) {
           LOGGER.warn("Error whilst saving Json config", e);
       }
    }

    @Override
    public void load(Config config, Path file) {
        try {
            correctExtension(file).ifPresent(f -> {
                try (JsonReader reader = gson.newJsonReader(Files.newBufferedReader(f))) {
                    Streams.parse(reader).getAsJsonObject().entrySet().forEach(entry -> {
                        config.getCategoryOrEmpty(entry.getKey()).ifPresent(category -> {
                            entry.getValue().getAsJsonObject().entrySet().forEach(tuple -> {
                                category.getOrEmpty(tuple.getKey().toLowerCase()).ifPresent(setting -> {
                                    setting.set(setting.getType().read(setting, tuple.getValue(), gson));
                                });
                            });
                        });
                    });
                } catch (Throwable e) {
                    LOGGER.warn("Erorr whilst loading json config", e);
                }
            });
        } finally {
            save(config, file);
        }
    }

    private Optional<Path> correctExtension(Path file) {
        return getAlternatives(file).filter(Files::isReadable).findFirst();
    }
}
