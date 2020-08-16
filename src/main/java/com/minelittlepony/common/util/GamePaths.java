package com.minelittlepony.common.util;

import com.minelittlepony.common.mixin.MixinPlayerSkinProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.nio.file.Path;

/**
 * Provides access to all of the basic paths needed to interact with the game.
 * <p>
 * Some of these are not properly provided by FabricLoader, or are provided, but use
 * discouraged methods of interacting with the filesystem.
 *
 * @author     Sollace
 */
public class GamePaths {

    private GamePaths() {}

    /**
     * Gets the current game (root) direction as a Path.
     */
    public static Path getGameDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }

    /**
     * Gets the current game config direction as a Path.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    /**
     * Gets the current game's assets direction as a Path.
     */
    public static Path getAssetsDirectory() {
        return ((MixinPlayerSkinProvider) MinecraftClient.getInstance().getSkinProvider()).getSkinCacheDirectory().getParentFile().toPath();
    }
}
