package com.minelittlepony.common.client;

import java.nio.file.Path;
import java.util.function.Function;

import com.minelittlepony.common.mixin.MixinBlockEntityRenderDispatcher;
import com.minelittlepony.common.mixin.MixinEntityRenderDispatcher;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

/**
 * Common mod interface to the underlying loader. (Currently Fabric)
 *
 */
public interface IModUtilities {

    /**
     * Registers a custom block entity renderer.
     *
     * Only safe to call after normal game initialization. Use this if you need an immediate change.
     */
    default <T extends BlockEntity> void addRenderer(Class<T> type, BlockEntityRenderer<T> renderer) {
        MixinBlockEntityRenderDispatcher mx = ((MixinBlockEntityRenderDispatcher)BlockEntityRenderDispatcher.INSTANCE);
        mx.getRenderers().put(type, renderer);
        renderer.setRenderManager(BlockEntityRenderDispatcher.INSTANCE);
    }

    /**
     * Registers a custom entity renderer.
     *
     * Only safe to call after normal game initialization. Use this if you need an immediate change.
     */
    default <T extends Entity> void addRenderer(Class<T> type, Function<EntityRenderDispatcher, EntityRenderer<T>> renderer) {
        EntityRenderDispatcher mx = MinecraftClient.getInstance().getEntityRenderManager();
        ((MixinEntityRenderDispatcher)mx).getRenderers().put(type, renderer.apply(mx));
    }

    /**
     * Registers a custom entity renderer.
     *
     * Only safe to call after normal game initialization. Use this if you need an immediate change.
     */
    default <T extends Entity> void addRenderer(String modelType, PlayerEntityRenderer renderer) {
        EntityRenderDispatcher mx = MinecraftClient.getInstance().getEntityRenderManager();
        ((MixinEntityRenderDispatcher)mx).getPlayerRenderers().put(modelType, renderer);
    }

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
    default KeyBinding registerKeybind(String category, String bindName, int key) {
        FabricKeyBinding binding = FabricKeyBinding.Builder.create(new Identifier(bindName), InputUtil.Type.KEYSYM, key, category).build();

        KeyBindingRegistry.INSTANCE.register(binding);
        return binding;
    }

    /**
     * Gets the platform configuration directory.
     */
    default Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDirectory().toPath();
    }

    /**
     * Gets the platform assets directory.
     */
    default Path getAssetsDirectory() {
        return FabricLoader.getInstance().getGameDirectory().toPath().resolve("assets");
    }
}
