package com.minelittlepony.common.mixin;

import com.minelittlepony.common.client.gui.ITickableElement;
import com.minelittlepony.common.client.gui.IViewRoot;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.event.ScreenInitCallback;
import com.minelittlepony.common.util.GamePaths.AssetsDirProvider;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;

import java.nio.file.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
abstract class MixinMinecraftClient implements AssetsDirProvider {
    private Path assetsDirectory;

    @Inject(method = "<init>", at = @At(
        value = "FIELD",
        target = "net/minecraft/client/MinecraftClient.instance:Lnet/minecraft/client/MinecraftClient;"
    ))
    private void onInit(RunArgs args, CallbackInfo info) {
        assetsDirectory = args.directories.assetDir.toPath();
    }

    @Override
    public Path getAssetsDirectory() {
        return assetsDirectory;
    }

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
            ScreenInitCallback.EVENT.invoker().init(client.currentScreen, (ScreenInitCallback.ButtonList)client.currentScreen);
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
