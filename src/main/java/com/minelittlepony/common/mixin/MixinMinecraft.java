package com.minelittlepony.common.mixin;

import com.minelittlepony.common.client.gui.ITickableElement;
import com.minelittlepony.common.client.gui.IViewRoot;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.event.ScreenInitCallback;
import com.minelittlepony.common.util.GamePaths.AssetsDirProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;

import java.nio.file.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
abstract class MixinMinecraft implements AssetsDirProvider {
    @Unique
    private Path assetsDirectory;

    @Inject(method = "<init>", at = @At(
        value = "FIELD",
        target = "net/minecraft/client/Minecraft.instance:Lnet/minecraft/client/Minecraft;"
    ))
    private void onInit(GameConfig args, CallbackInfo info) {
        assetsDirectory = args.location.assetDirectory.toPath();
    }

    @Override
    public Path getAssetsDirectory() {
        return assetsDirectory;
    }

    @Inject(method = "resizeGui()V", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/gui/screens/Screen.resize(II)V",
            shift = Shift.AFTER
        )
    )
    private void onResizeGui(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        Screen screen = client.gui.screen();
        if (screen instanceof IViewRoot root) {
            Bounds bounds = root.getBounds();
            bounds.width = client.getWindow().getGuiScaledWidth();
            bounds.height = client.getWindow().getGuiScaledHeight();
            ScreenInitCallback.EVENT.invoker().init(screen, screen);
        }
    }

    @Inject(method = "runTick(Z)V", at = @At("HEAD"))
    public void onTick(CallbackInfo info) {
        Minecraft client = Minecraft.getInstance();
        if (client.gui.screen() instanceof IViewRoot root) {
            root.getChildElements().forEach(element -> {
                if (element instanceof ITickableElement t) {
                    t.tick();
                }
            });
        }
    }
}
