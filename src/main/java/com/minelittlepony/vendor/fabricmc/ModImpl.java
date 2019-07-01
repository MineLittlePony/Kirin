package com.minelittlepony.vendor.fabricmc;

import java.nio.file.Path;
import java.util.function.Function;

import com.minelittlepony.common.client.IModUtilities;
import com.minelittlepony.common.mixin.MixinEntityRenderDispatcher;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

/**
 * Fabric mod interface implementation.
 */
public interface ModImpl extends IModUtilities {
    @Override
    default <T extends BlockEntity> void addRenderer(Class<T> type, BlockEntityRenderer<T> renderer) {
        BlockEntityRendererRegistry.INSTANCE.register(type, renderer);
    }

    @Override
    default <T extends Entity> void addRenderer(Class<T> type, Function<EntityRenderDispatcher, EntityRenderer<T>> renderer) {
        EntityRendererRegistry.INSTANCE.register(type, (m, c) -> renderer.apply(m));
    }

    @Override
    default <T extends Entity> void addRenderer(String modelType, PlayerEntityRenderer renderer) {
        EntityRenderDispatcher mx = MinecraftClient.getInstance().getEntityRenderManager();
        ((MixinEntityRenderDispatcher)mx).getPlayerRenderers().put(modelType, renderer);
    }

    @Override
    default KeyBinding registerKeybind(String category, String bindName, int key) {
        FabricKeyBinding binding = FabricKeyBinding.Builder.create(new Identifier(bindName), InputUtil.Type.KEYSYM, key, category).build();

        KeyBindingRegistry.INSTANCE.register(binding);
        return binding;
    }

    @Override
    default Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDirectory().toPath();
    }
}
