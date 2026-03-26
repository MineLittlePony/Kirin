package com.minelittlepony.common.util.registry;

import com.minelittlepony.common.util.Untyped;

import net.minecraft.core.component.DataComponentPatch;

/**
 * Utilities for working with components
 */
public interface ComponentUtils {
    /**
     * Returns a mutable copy of a data component patch
     */
    static DataComponentPatch.Builder copy(DataComponentPatch patch) {
        return copy(patch, DataComponentPatch.builder());
    }

    /**
     * Applies the contents of a patch onto a builder.
     */
    static DataComponentPatch.Builder copy(DataComponentPatch from, DataComponentPatch.Builder to) {
        from.entrySet().forEach(entry -> {
            if (entry.getValue().isPresent()) {
                to.set(Untyped.cast(entry.getKey()), entry.getValue().get());
            } else {
                to.remove(entry.getKey());
            }
        });
        return to;
    }
}
