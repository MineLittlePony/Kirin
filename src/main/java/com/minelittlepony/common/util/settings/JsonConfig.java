package com.minelittlepony.common.util.settings;

import java.nio.file.Path;

/**
 * A specialised configuration container that loads from a json file.
 *
 * @deprecated Will be removed in MC1.22. Use Config with {@code Config#FLATTENED_JSON_ADAPTER}
 */
@Deprecated(forRemoval = true)
public class JsonConfig extends Config {
    public JsonConfig(Path path) {
        super(LegacyJsonConfigAdapter.DEFAULT, path);
    }
}
