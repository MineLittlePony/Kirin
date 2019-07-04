package com.minelittlepony.common.mixin;

import com.minelittlepony.common.event.SkinFilterCallback;
import net.minecraft.client.texture.ImageFilter;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SkinRemappingImageFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SkinRemappingImageFilter.class)
public abstract class MixinSkinRemappingImageFilter implements ImageFilter {

    @Inject(method = "filterImage(Lnet/minecraft/client/texture/NativeImage;)Lnet/minecraft/client/texture/NativeImage;",
            at = @At("RETURN"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void update(NativeImage image, CallbackInfoReturnable<NativeImage> ci, boolean legacy) {
        // convert skins from mojang server
        SkinFilterCallback.EVENT.invoker().processImage(ci.getReturnValue(), legacy);
    }
}
