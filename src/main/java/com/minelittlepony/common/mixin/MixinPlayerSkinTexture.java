package com.minelittlepony.common.mixin;

import com.minelittlepony.common.event.SkinFilterCallback;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.texture.ResourceTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkinTexture.class)
public abstract class MixinPlayerSkinTexture extends ResourceTexture {

    private MixinPlayerSkinTexture() { super(null); }

    private static final String FILTER_IMAGE = "remapTexture(Lnet/minecraft/client/texture/NativeImage;)Lnet/minecraft/client/texture/NativeImage;";

    private static final String STRIP_COLOR = "stripColor(Lnet/minecraft/client/texture/NativeImage;IIII)V";
    private static final String STRIP_ALPHA = "stripAlpha(Lnet/minecraft/client/texture/NativeImage;IIII)V";

    private static boolean isLegacy;

    @Inject(method = FILTER_IMAGE, at = @At("HEAD"))
    private void beforeUpdate(NativeImage image, CallbackInfoReturnable<NativeImage> info) {
        isLegacy = image.getHeight() == 32;
    }

    @Inject(method = FILTER_IMAGE, at = @At("RETURN"))
    private void update(NativeImage image, CallbackInfoReturnable<NativeImage> ci) {
        // convert skins from mojang server
        SkinFilterCallback.EVENT.invoker().processImage(ci.getReturnValue(), isLegacy);
    }

    // Sorry, Mahjon. Input validation is good 'n all, but this interferes with our other mods.
    @Inject(method = STRIP_ALPHA, at = @At("HEAD"), cancellable = true)
    private static void cancelAlphaStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        if (SkinFilterCallback.EVENT.invoker().shouldAllowTransparency(image, isLegacy)) {
            info.cancel();
        }
    }

    @Inject(method = STRIP_COLOR, at = @At("HEAD"), cancellable = true)
    private static void cancelColorStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        if (SkinFilterCallback.EVENT.invoker().shouldAllowTransparency(image, isLegacy)) {
            info.cancel();
        }
    }
    // -
}
