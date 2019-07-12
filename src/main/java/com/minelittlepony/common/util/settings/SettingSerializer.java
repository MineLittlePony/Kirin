package com.minelittlepony.common.util.settings;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class SettingSerializer implements JsonSerializer<Setting<?>> {
    @Override
    public JsonElement serialize(Setting<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.get());
    }
}