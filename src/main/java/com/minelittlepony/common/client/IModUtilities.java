package com.minelittlepony.common.client;

import java.nio.file.Path;
import java.util.function.Function;

import com.minelittlepony.common.mixin.MixinMinecraftClient;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;

/**
 * Common interface to the underlying loader.
 *
 */
public interface IModUtilities {

    /**
     * Registers a custom block entity renderer.
     *
     * Only safe to call after normal game initialization. Use this if you need an immediate change.
     */
    <T extends BlockEntity> void addRenderer(Class<T> type, BlockEntityRenderer<T> renderer);

    /**
     * Registers a custom entity renderer.
     *
     * Only safe to call after normal game initialization. Use this if you need an immediate change.
     */
    <T extends Entity> void addRenderer(Class<T> type, Function<EntityRenderDispatcher, EntityRenderer<T>> renderer);

    /**
     * Registers a custom entity renderer.
     *
     * Only safe to call after normal game initialization. Use this if you need an immediate change.
     */
    <T extends Entity> void addRenderer(String modelType, PlayerEntityRenderer renderer);

    /**
     * Returns true if the current environment has forge classes.
     */
    default boolean hasFml() {
        return false;
    }

    /**
     * Creates and registers a new keybinding.
     *
     * @param category  The category.
     * @param key       The default keycode for this binding.
     * @param bindName  Translation string of the binding's name.
     *
     * @return A new KeyBinding instance.
     */
    KeyBinding registerKeybind(String category, String bindName, int key);

    /**
     * Gets the platform configuration directory.
     */
    Path getConfigDirectory();

    /**
     * Gets the platform assets directory.
     */
    default Path getAssetsDirectory() {
        return ((MixinMinecraftClient)MinecraftClient.getInstance()).getAssetsDirectory().toPath();
    }
}
