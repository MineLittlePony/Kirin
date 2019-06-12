package com.minelittlepony.common.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public interface MixinMinecraftClient {
    @Accessor("assetDirectory")
    File getAssetsDirectory();
}
