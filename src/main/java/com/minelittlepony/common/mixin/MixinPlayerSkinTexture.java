package com.minelittlepony.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.minelittlepony.common.event.SkinFilterCallback;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTextureDownloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkinTextureDownloader.class)
abstract class MixinPlayerSkinTexture {
    private static final String FILTER_IMAGE = "remapTexture(Lnet/minecraft/client/texture/NativeImage;Ljava/lang/String;)Lnet/minecraft/client/texture/NativeImage;";
    private static final String STRIP_COLOR = "net/minecraft/client/texture/PlayerSkinTextureDownloader.stripColor(Lnet/minecraft/client/texture/NativeImage;IIII)V";
    private static final String STRIP_ALPHA = "net/minecraft/client/texture/PlayerSkinTextureDownloader.stripAlpha(Lnet/minecraft/client/texture/NativeImage;IIII)V";

    @Inject(method = FILTER_IMAGE, at = @At("HEAD"))
    private static void beforeUpdate(NativeImage image, String url,
            CallbackInfoReturnable<NativeImage> ci,
            @Share(value = "kirinmlp_initialWidth") LocalIntRef initialWidth,
            @Share(value = "kirinmlp_initialHeight") LocalIntRef initialHeight) {
        initialWidth.set(image.getWidth());
        initialHeight.set(image.getHeight());
    }

    @ModifyReturnValue(method = FILTER_IMAGE, at = @At("RETURN"))
    private static NativeImage update(NativeImage image,
            @Share(value = "kirinmlp_initialWidth") LocalIntRef initialWidth,
            @Share(value = "kirinmlp_initialHeight") LocalIntRef initialHeight) {
        // convert skins from mojang server
        return SkinFilterCallback.EVENT.invoker().processImage(image, initialWidth.get(), initialHeight.get());
    }

    // Sorry, Mahjon. Input validation is good 'n all, but this interferes with our other mods.
    @WrapOperation(method = FILTER_IMAGE, at = {
            @At(value = "INVOKE", target = STRIP_ALPHA),
            @At(value = "INVOKE", target = STRIP_COLOR)
    })
    private static void cancelAlphaStrip(NativeImage image, int x1, int y1, int x2, int y2,
            Operation<Void> operation,
            @Share(value = "kirinmlp_initialWidth") LocalIntRef initialWidth,
            @Share(value = "kirinmlp_initialHeight") LocalIntRef initialHeight) {
        if (!SkinFilterCallback.EVENT.invoker().shouldAllowTransparency(image, initialWidth.get(), initialHeight.get())) {
            operation.call(image, x1, y1, x2, y2);
        }
    }
}
