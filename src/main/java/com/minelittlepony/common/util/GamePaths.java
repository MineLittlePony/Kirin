package com.minelittlepony.common.util;

import com.minelittlepony.common.mixin.MixinPlayerSkinProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.nio.file.Path;

public class GamePaths {

    private GamePaths() {}

    public static Path getGameDirectory() {
        return FabricLoader.getInstance().getGameDirectory().toPath();
    }

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDirectory().toPath();
    }

    public static Path getAssetsDirectory() {
        return ((MixinPlayerSkinProvider) MinecraftClient.getInstance().getSkinProvider()).getSkinCacheDirectory().getParentFile().toPath();
    }
}
