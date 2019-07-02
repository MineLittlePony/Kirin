package com.minelittlepony.common.util;

import com.minelittlepony.common.mixin.MixinMinecraftClient;
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
        return ((MixinMinecraftClient) MinecraftClient.getInstance()).getAssetsDirectory().toPath();
    }
}
