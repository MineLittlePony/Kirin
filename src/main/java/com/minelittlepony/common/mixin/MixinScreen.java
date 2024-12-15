package com.minelittlepony.common.mixin;

import com.minelittlepony.common.client.gui.IViewRootDefaultImpl;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.minelittlepony.common.event.ScreenInitCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
abstract class MixinScreen extends AbstractParentElement implements Drawable, IViewRootDefaultImpl {
    private final Bounds bounds = new Bounds(0, 0, 0, 0);
    private final Padding padding = new Padding(0, 0, 0, 0);

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public Padding getContentPadding() {
        return padding;
    }

    @Override
    @Accessor("selectables")
    public abstract List<Selectable> buttons();

    @Invoker("addDrawableChild")
    @Override
    public abstract <T extends Element & Drawable & Selectable> T addButton(T button);

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
    private void onInit(MinecraftClient client, int w, int h, CallbackInfo ci) {
        bounds.width = w;
        bounds.height = h;
        ScreenInitCallback.EVENT.invoker().init((Screen) (Object) this, this);
    }
}
