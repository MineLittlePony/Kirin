package com.minelittlepony.common.mixin;

import com.minelittlepony.common.client.gui.ITextContext;
import com.minelittlepony.common.client.gui.ITooltipped;
import com.minelittlepony.common.client.gui.IViewRoot;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.minelittlepony.common.event.ScreenInitCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
abstract class MixinScreen extends AbstractParentElement implements Drawable, IViewRoot, ITextContext {

    private final Bounds bounds = new Bounds(0, 0, 0, 0);

    private final Padding padding = new Padding(0, 0, 0, 0);

    @Shadow
    private @Final List<Drawable> drawables;
    @Shadow
    private @Final List<Selectable> selectables;

    @Shadow
    protected abstract <T extends Element & Drawable & Selectable> T addDrawableChild(T element);

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(Bounds bounds) { }

    @SuppressWarnings("unchecked")
    @Override
    public List<Element> getChildElements() {
        return (List<Element>) ((Screen) (Object)this).children();
    }

    @Override
    public Padding getContentPadding() {
        return padding;
    }

    @Override
    public List<Selectable> buttons() {
        return selectables;
    }

    @Override
    public <T extends Element & Drawable & Selectable> T addButton(T button) {
        return addDrawableChild(button);
    }

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
    private void onInit(MinecraftClient minecraftClient_1, int w, int h, CallbackInfo ci) {
        bounds.width = w;
        bounds.height = h;
        ScreenInitCallback.EVENT.invoker().init((Screen) (Object) this, this::addDrawableChild);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        drawables.forEach(button -> {
            if (button instanceof ITooltipped && button instanceof ClickableWidget && ((ClickableWidget)button).isHovered()) {
                ((ITooltipped<?>)button).renderToolTip(matrices, (Screen)(Object)this, mouseX, mouseY);
            }
        });
    }
}
