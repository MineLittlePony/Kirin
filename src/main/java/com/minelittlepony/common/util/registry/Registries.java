package com.minelittlepony.common.util.registry;

import java.util.function.Function;

import com.mojang.serialization.Lifecycle;

import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

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
        return new DefaultedMappedRegistry<>(defaultIdFactory.apply(defaultValue).toString(), ResourceKey.createRegistryKey(id), Lifecycle.stable(), true) {
            {
                Registry.register(this, getDefaultKey(), defaultValue);
            }

            public Holder.Reference<T> register(ResourceKey<T> key, T value, RegistrationInfo info) {
                createIntrusiveHolder(value);
                return super.register(key, value, info);
            }
        };
    }
}
