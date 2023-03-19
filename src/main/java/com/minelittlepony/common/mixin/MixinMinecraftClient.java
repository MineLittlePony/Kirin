package com.minelittlepony.common.mixin;

import com.minelittlepony.common.client.gui.IViewRoot;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.event.ScreenInitCallback;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
abstract class MixinMinecraftClient {
    @Inject(method = "onResolutionChanged()V", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/gui/screen/Screen.resize(Lnet/minecraft/client/MinecraftClient;II)V",
            shift = Shift.AFTER
        )
    )
    private void onOnResolutionChanged(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Bounds bounds = ((IViewRoot)client.currentScreen).getBounds();
        bounds.width = client.getWindow().getScaledWidth();
        bounds.height = client.getWindow().getScaledHeight();
        ScreenInitCallback.EVENT.invoker().init(client.currentScreen, (ScreenInitCallback.ButtonList)client.currentScreen);
    }
}
