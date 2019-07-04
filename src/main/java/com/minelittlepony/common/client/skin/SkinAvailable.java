package com.minelittlepony.common.client.skin;

import com.minelittlepony.common.event.SkinAvailableCallback;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.PlayerSkinProvider.SkinTextureAvailableCallback;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public class SkinAvailable implements SkinTextureAvailableCallback {

    private final SkinTextureAvailableCallback callback;

    public SkinAvailable(@Nullable SkinTextureAvailableCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSkinTextureAvailable(Type type, Identifier id, MinecraftProfileTexture texture) {
        MinecraftClient.getInstance().method_18858(() -> {
            // This is called too early, so do it later instead.
            SkinAvailableCallback.EVENT.invoker().onSkinAvailable(type, id, texture);
            if (callback != null) {
                callback.onSkinTextureAvailable(type, id, texture);
            }
        });
    }
}
