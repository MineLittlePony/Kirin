package com.minelittlepony.common.util.render;

import java.util.Optional;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

public interface RenderLayerUtil {
    /**
     * Gets the texture from a render layer (if one is available)
     */
    static Optional<Identifier> getTexture(RenderLayer layer) {
        return Optional.ofNullable(layer.renderSetup.textures.get("Sampler0")).map(i -> i.location());
    }
}
