package com.minelittlepony.common.mixin;

import com.minelittlepony.common.client.gui.ITickableElement;
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
        if (client.currentScreen instanceof IViewRoot root) {
            Bounds bounds = root.getBounds();
            bounds.width = client.getWindow().getScaledWidth();
            bounds.height = client.getWindow().getScaledHeight();
        }
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void onTick(CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof IViewRoot root) {
            root.getChildElements().forEach(element -> {
                if (element instanceof ITickableElement t) {
                    t.tick();
                }
            });
        }
    }
}
