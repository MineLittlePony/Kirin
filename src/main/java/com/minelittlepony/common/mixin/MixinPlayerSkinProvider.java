package com.minelittlepony.common.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.texture.PlayerSkinProvider;

@Mixin(PlayerSkinProvider.class)
public interface MixinPlayerSkinProvider {
    @Accessor("skinCacheDir")
    File getSkinCacheDirectory();
}
