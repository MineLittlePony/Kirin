package com.minelittlepony.common.util.render;

import java.util.Optional;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.util.Identifier;

public interface RenderLayerUtil {
    static RenderLayer.MultiPhase layerOf(
            Identifier name,
            VertexFormat vertexFormat,
            DrawMode drawMode,
            int expectedBufferSize,
            RenderLayer.MultiPhaseParameters phases) {
        return RenderLayer.of(name.toString(), vertexFormat, drawMode, expectedBufferSize, phases);
    }

    static RenderLayer.MultiPhase layerOf(
            Identifier name,
            VertexFormat vertexFormat,
            DrawMode drawMode,
            int expectedBufferSize,
            boolean crumbling,
            boolean translucent,
            RenderLayer.MultiPhaseParameters phases
        ) {
        return RenderLayer.of(name.toString(), vertexFormat, drawMode, expectedBufferSize, crumbling, translucent, phases);
    }

    static Optional<Identifier> getTexture(RenderLayer layer) {
        if (layer instanceof RenderLayer.MultiPhase multiphase) {
            return multiphase.getPhases().texture.getId();
        }
        return Optional.empty();
    }
}
