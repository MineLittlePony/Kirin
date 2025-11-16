package com.minelittlepony.common.util.render;

import java.util.Optional;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;


public interface RenderLayerUtil {
    /**
     * Gets the texture from a render layer (if one is available)
     */
    static Optional<Identifier> getTexture(RenderType layer) {
        return Optional.ofNullable(layer.state.textures.get("Sampler0")).map(i -> i.location());
    }
}
