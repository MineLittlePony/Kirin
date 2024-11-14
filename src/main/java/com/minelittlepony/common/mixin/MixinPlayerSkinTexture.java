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

    private static int initialWidth;
    private static int initialHeight;

    @Inject(method = "remapTexture", at = @At("HEAD"))
    private void beforeUpdate(NativeImage image, CallbackInfoReturnable<NativeImage> info) {
        initialWidth = image.getWidth();
        initialHeight = image.getHeight();
    }

    @Inject(method = "remapTexture", at = @At("RETURN"))
    private void update(NativeImage image, CallbackInfoReturnable<NativeImage> ci) {
        // convert skins from mojang server
        ci.setReturnValue(SkinFilterCallback.EVENT.invoker().processImage(ci.getReturnValue(), initialWidth, initialHeight));
    }

    // Sorry, Mahjon. Input validation is good 'n all, but this interferes with our other mods.
    @Inject(method = "stripAlpha", at = @At("HEAD"), cancellable = true)
    private static void cancelAlphaStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        if (SkinFilterCallback.EVENT.invoker().shouldAllowTransparency(image, initialWidth, initialHeight)) {
            info.cancel();
        }
    }

    @Inject(method = "stripColor", at = @At("HEAD"), cancellable = true)
    private static void cancelColorStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        if (SkinFilterCallback.EVENT.invoker().shouldAllowTransparency(image, initialWidth, initialHeight)) {
            info.cancel();
        }
    }
    // -
}
