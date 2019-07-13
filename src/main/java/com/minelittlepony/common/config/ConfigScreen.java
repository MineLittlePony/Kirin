package com.minelittlepony.common.config;

import com.minelittlepony.common.client.gui.GameGui;

import java.util.Map;

/**
 * TODO
 */
public class ConfigScreen extends GameGui {

    private Map<String, Map<String, ValueSignature>> configValues;

    public ConfigScreen(JsonConfig config) {
        super(null);

    }


    @Override
    protected void init() {
    }
}
