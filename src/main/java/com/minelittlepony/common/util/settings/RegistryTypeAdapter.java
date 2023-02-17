package com.minelittlepony.common.util.settings;

import java.util.function.BiFunction;

import com.google.gson.TypeAdapter;
import net.minecraft.util.registry.Registry;

/**
 * @deprecated replace with com.minelittlepony.common.util.registry.RegistryTypeAdapter
 */
@Deprecated
public class RegistryTypeAdapter<T> extends com.minelittlepony.common.util.registry.RegistryTypeAdapter<T> {
    public static <T> TypeAdapter<T> of(Registry<T> registry) {
        return com.minelittlepony.common.util.registry.RegistryTypeAdapter.of(registry);
    }

    public static <T> TypeAdapter<T> of(Registry<T> registry, BiFunction<String, Registry<T>, T> defaultValue) {
        return com.minelittlepony.common.util.registry.RegistryTypeAdapter.of(registry, defaultValue);
    }

    protected RegistryTypeAdapter(Registry<T> registry) {
        super(registry);
    }
}
