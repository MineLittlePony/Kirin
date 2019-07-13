package com.minelittlepony.common.config;

import com.google.gson.GsonBuilder;

public interface JsonConfig {

    default GsonBuilder createGson() {
        return new GsonBuilder();
    }
}
