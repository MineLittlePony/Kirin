package com.minelittlepony.common.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;

@Mixin(EntityRenderDispatcher.class)
public interface MixinEntityRenderDispatcher {

    @Accessor("renderers")
    Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> getRenderers();

    @Accessor("modelRenderers")
    Map<String, PlayerEntityRenderer> getPlayerRenderers();
}
