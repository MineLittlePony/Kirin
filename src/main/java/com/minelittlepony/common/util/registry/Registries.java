package com.minelittlepony.common.util.registry;

import java.util.function.Function;

import com.mojang.serialization.Lifecycle;

import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public interface Registries {
    /**
     * Creates a new registry with the specified id and default value.
     *
     * Values registered to this registry become usable immediately.
     * @param <T> The type of entries this registry will contain
     * @param id The id of the registry
     * @param defaultIdFactory Factory to get the id of the supplied default value
     * @param defaultValue The default value
     * @return A new registry.
     */
    static <T> Registry<T> createDefaulted(Identifier id, Function<T, Identifier> defaultIdFactory, T defaultValue) {
        return new SimpleDefaultedRegistry<>(defaultIdFactory.apply(defaultValue).toString(), RegistryKey.ofRegistry(id), Lifecycle.stable(), true) {
            {
                Registry.register(this, getDefaultId(), defaultValue);
            }

            public RegistryEntry.Reference<T> set(int i, RegistryKey<T> registryKey, T object, Lifecycle lifecycle) {
                createEntry(object);
                return super.set(i, registryKey, object, lifecycle);
            }
        };
    }
}
