package com.minelittlepony.common.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.minelittlepony.common.event.SkinFilterCallback;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.texture.ResourceTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkinTexture.class)
public abstract class MixinPlayerSkinTexture extends ResourceTexture {

    private MixinPlayerSkinTexture() { super(null); }

    private static final String FILTER_IMAGE = "remapTexture(Lnet/minecraft/client/texture/NativeImage;)Lnet/minecraft/client/texture/NativeImage;";

    private static final String STRIP_COLOR = "net/minecraft/client/texture/PlayerSkinTexture.stripColor(Lnet/minecraft/client/texture/NativeImage;IIII)V";
    private static final String STRIP_ALPHA = "net/minecraft/client/texture/PlayerSkinTexture.stripAlpha(Lnet/minecraft/client/texture/NativeImage;IIII)V";

    @Inject(method = FILTER_IMAGE, at = @At("HEAD"))
    private void beforeUpdate(NativeImage image,
            CallbackInfoReturnable<NativeImage> ci,
            @Share(value = "kirinmlp_initialWidth") LocalIntRef initialWidth,
            @Share(value = "kirinmlp_initialHeight") LocalIntRef initialHeight) {
        initialWidth.set(image.getWidth());
        initialHeight.set(image.getHeight());
    }

    @Inject(method = FILTER_IMAGE, at = @At("RETURN"), cancellable = true)
    private void update(NativeImage image,
            CallbackInfoReturnable<NativeImage> ci,
            @Share(value = "kirinmlp_initialWidth") LocalIntRef initialWidth,
            @Share(value = "kirinmlp_initialHeight") LocalIntRef initialHeight) {
        // convert skins from mojang server
        ci.setReturnValue(SkinFilterCallback.EVENT.invoker().processImage(ci.getReturnValue(), initialWidth.get(), initialHeight.get()));
    }

    // Sorry, Mahjon. Input validation is good 'n all, but this interferes with our other mods.
    @Inject(method = FILTER_IMAGE, at = {
            @At(value = "INVOKE", target = STRIP_ALPHA),
            @At(value = "INVOKE", target = STRIP_COLOR)
    }, cancellable = true)
    private void cancelAlphaStrip(NativeImage image, CallbackInfoReturnable<NativeImage> info,
            @Share(value = "kirinmlp_initialWidth") LocalIntRef initialWidth,
            @Share(value = "kirinmlp_initialHeight") LocalIntRef initialHeight) {
        if (SkinFilterCallback.EVENT.invoker().shouldAllowTransparency(image, initialWidth.get(), initialHeight.get())) {
            info.setReturnValue(SkinFilterCallback.EVENT.invoker().processImage(image, initialWidth.get(), initialHeight.get()));
        }
    }
    // -
}
