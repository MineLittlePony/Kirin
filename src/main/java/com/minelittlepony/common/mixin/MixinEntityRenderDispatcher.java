package com.minelittlepony.common.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.EntityType;

@Mixin(EntityRenderDispatcher.class)
public interface MixinEntityRenderDispatcher {

    @Accessor("modelRenderers")
    Map<String, PlayerEntityRenderer> getPlayerRenderers();

    @Accessor("renderers")
    Map<EntityType<?>, EntityRenderer<?>> getEntityRenderers();
}
