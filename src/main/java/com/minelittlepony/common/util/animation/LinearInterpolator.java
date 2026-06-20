package com.minelittlepony.common.util.animation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LinearInterpolator implements Interpolator {
    static LoadingCache<UUID, LinearInterpolator> instanceCache = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofSeconds(30))
        .build(CacheLoader.from(LinearInterpolator::new));

    private final Map<String, Float> properties = new HashMap<>();

    @Override
    public float interpolate(String key, float to, float animationSpeed) {
        float from = get(key, to);

        from += (to - from) / animationSpeed;

        if (Float.isNaN(from) || Float.isInfinite(from)) {
            System.err.println("Error: Animation frame for " + key + " is NaN or Infinite.");
            from = to;
        }

        properties.put(key, from);

        return from;
    }

    @Override
    public float get(String key, float fallback) {
        return properties.getOrDefault(key, fallback);
    }
}
