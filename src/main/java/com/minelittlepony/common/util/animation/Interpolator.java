package com.minelittlepony.common.util.animation;

import java.util.UUID;

/**
 * Interpolation function for handling transitions between animation states.
 * <p>
 * Remembers the previous state and will generate a value interpolated
 * between the previous and current states.
 */
@FunctionalInterface
public interface Interpolator {
    /**
     * Interpolates a value between the requested final destination and what it was last.
     *
     * @param key           Identifier to track previous values
     * @param state         The new values
     * @param animationSpeed Scaling factor to control how quickly values change
     */
    float interpolate(String key, float state, float animationSpeed);

    /**
     * Gets the last calculated interpolated value without changing it.
     *
     * @param key           Identifier to track previous values
     * @param fallback      The fallback to return if no value is available for the requested key
     */
    default float get(String key, float fallback) {
        return fallback;
    }

    /**
     * Gets or creates a new linear interpolation function for the provided id.
     */
    static Interpolator linear(UUID id) {
        return LinearInterpolator.instanceCache.getUnchecked(id);
    }

}
