package com.minelittlepony.common.mixin;

import com.minelittlepony.common.client.gui.IViewRootDefaultImpl;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.minelittlepony.common.event.ScreenInitCallback;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
abstract class MixinScreen extends AbstractContainerEventHandler implements Renderable, IViewRootDefaultImpl {
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
    @Accessor("narratables")
    public abstract List<NarratableEntry> buttons();

    @Invoker("addRenderableWidget")
    @Override
    public abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addButton(T button);

    @Inject(method = "init(II)V", at = @At("RETURN"))
    private void onInit(int w, int h, CallbackInfo ci) {
        bounds.width = w;
        bounds.height = h;
        ScreenInitCallback.EVENT.invoker().init((Screen) (Object) this, this);
    }
}
