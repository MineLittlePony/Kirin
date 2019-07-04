package com.minelittlepony.common.mixin;

import com.minelittlepony.common.client.skin.SkinAvailable;
import net.minecraft.client.texture.ImageFilter;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.texture.PlayerSkinProvider.SkinTextureAvailableCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerSkinProvider.class)
public abstract class MixinPlayerSkinProvider implements ImageFilter {

    @ModifyVariable(
            method = "loadSkin(Lcom/mojang/authlib/minecraft/MinecraftProfileTexture;Lcom/mojang/authlib/minecraft/MinecraftProfileTexture$Type;Lnet/minecraft/client/texture/PlayerSkinProvider$SkinTextureAvailableCallback;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"),
            argsOnly = true,
            index = 3)
    private SkinTextureAvailableCallback onSkinLoaded(SkinTextureAvailableCallback callback) {
        return new SkinAvailable(callback);
    }
}
