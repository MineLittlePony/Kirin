package com.minelittlepony.common.mixin;

import com.minelittlepony.common.event.ScreenInitCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @Shadow
    protected abstract <T extends AbstractButtonWidget> T addButton(T abstractButtonWidget_1);

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
    private void onInit(MinecraftClient minecraftClient_1, int w, int h, CallbackInfo ci) {
        ScreenInitCallback.EVENT.invoker().init((Screen) (Object) this, this::addButton);
    }
}
