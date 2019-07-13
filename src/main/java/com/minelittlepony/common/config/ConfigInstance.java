package com.minelittlepony.common.config;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

class ConfigInstance<T extends JsonConfig> {
    private final Gson gson;
    private final Path configPath;
    private final T config;

    private final Map<String, Map<String, ValueSignature<?>>> valueCache;

    ConfigInstance(Path configPath, T config) {
        this.configPath = configPath;
        this.config = config;

        this.valueCache = cacheValueObjects();
        System.out.println(valueCache);

        this.gson = config.createGson()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(config.getClass(), new SelfAdapter())
                .create();
    }

    /**
     * Cache all the values from the config
     */
    @SuppressWarnings("unchecked")
    private Map<String, Map<String, ValueSignature<?>>> cacheValueObjects() {
        try {
            Map<String, Map<String, ValueSignature<?>>> categories = new TreeMap<>();
            Map<String, ValueSignature<?>> root = categories.computeIfAbsent("", k -> new TreeMap<>());
            for (Field f : config.getClass().getFields()) {
                if (f.getType() == Value.class) {
                    Setting setting = f.getAnnotation(Setting.class);
                    String cat = "";
                    String name = "";
                    if (setting != null) {
                        cat = setting.category();
                        name = setting.name();
                    }
                    if (name.isEmpty()) {
                        name = f.getName();
                    }

                    if (root.containsKey(cat) || categories.containsKey(name)) {
                        throw new IllegalArgumentException("Duplicate key with category name");
                    }

                    // get the category
                    Map<String, ValueSignature<?>> map = categories.computeIfAbsent(cat, c -> new TreeMap<>());

                    if (map.containsKey(name)) {
                        throw new IllegalArgumentException("Duplicate key in category");
                    }

                    Type type = ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];

                    Value<Object> value = (Value<Object>) f.get(config);

                    map.put(name, new ValueSignature(type, value));
                }
            }

            return ImmutableMap.copyOf(categories);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    void load() throws IOException {
        try (Reader r = Files.newBufferedReader(configPath)) {
            gson.fromJson(r, config.getClass());
        } catch (NoSuchFileException e) {
            save();
        }
    }

    void save() throws IOException {
        Files.createDirectories(configPath.getParent());
        try (Writer w = Files.newBufferedWriter(configPath)) {
            gson.toJson(config, w);
        }
    }

    Map<String, ValueSignature<?>> getCatagory(String category) {
        return ImmutableMap.copyOf(valueCache.get(Strings.nullToEmpty(category)));
    }

    @SuppressWarnings("unchecked")
    private class SelfAdapter extends TypeAdapter<T> {
        @Override
        public void write(JsonWriter out, T value) throws IOException {
            out.beginObject();
            for (String category : valueCache.keySet()) {
                if (!category.isEmpty()) {
                    out.name(category);
                    out.beginObject();
                }
                // write all the fields in this category.
                for (Map.Entry<String, ValueSignature<?>> entry : valueCache.get(category).entrySet()) {
                    out.name(entry.getKey());
                    Value<?> val = entry.getValue().value;
                    Type type = entry.getValue().type;
                    gson.toJson(val.get(), type, out);
                }
                if (!category.isEmpty()) {
                    out.endObject();
                }
            }
            out.endObject();
        }

        @Override
        public T read(JsonReader in) throws IOException {
            Map<String, ValueSignature<?>> root = valueCache.get("");
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                if (root.containsKey(name)) {
                    // no category
                    ValueSignature value = root.get(name);
                    if (value == null) {
                        in.skipValue();
                    } else {
                        value.value.set(gson.fromJson(in, value.type));
                    }
                } else {
                    // yes category
                    Map<String, ValueSignature<?>> category = valueCache.get(name);
                    if (category == null) {
                        in.skipValue();
                    } else {
                        readJsonCategory(in, category);
                    }
                }
            }
            in.endObject();
            return config;
        }

        private void readJsonCategory(JsonReader in, Map<String, ValueSignature<?>> category) throws IOException {
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                ValueSignature value = category.get(name);
                if (value == null) {
                    in.skipValue();
                } else {
                    value.value.set(gson.fromJson(in, value.type));
                }
            }

            in.endObject();
        }
    }

}
