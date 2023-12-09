package com.minelittlepony.common.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @deprecated Rather use EntityRendererRegistry or replace with the APIs provided by Mson to set player renderers
 */
@Deprecated
@Mixin(EntityRenderDispatcher.class)
public interface MixinEntityRenderDispatcher {
    @Accessor("modelRenderers")
    Map<String, EntityRenderer<? extends PlayerEntity>> getPlayerRenderers();

    @Accessor("renderers")
    Map<EntityType<?>, EntityRenderer<?>> getEntityRenderers();
}
