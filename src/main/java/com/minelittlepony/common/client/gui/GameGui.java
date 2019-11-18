package com.minelittlepony.common.client.gui;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

public abstract class GameGui extends Screen implements IBounded, ITextContext {

    private final Bounds bounds = new Bounds(0, 0, 0, 0);

    @Nullable
    protected final Screen parent;

    protected GameGui(Text title) {
        this(title, MinecraftClient.getInstance().currentScreen);
    }

    protected GameGui(Text title, @Nullable Screen parent) {
        super(title);

        this.parent = parent;
    }

    public static void playSound(SoundEvent event) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(event, 1));
    }

    public static boolean isKeyDown(int key) {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), key);
    }

    public static Supplier<Boolean> keyCheck(int key) {
        return () -> isKeyDown(key);
    }

    public List<AbstractButtonWidget> buttons() {
        return buttons;
    }

    @Override
    public List<Element> children() {
        return children;
    }

    @Override
    public <T extends AbstractButtonWidget> T addButton(T button) {
        return super.addButton(button);
    }

    @Override
    public void init(MinecraftClient mc, int width, int height) {
        bounds.width = width;
        bounds.height = height;
        super.init(mc, width, height);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        buttons.forEach(button -> {
            if (button instanceof ITooltipped && button.isMouseOver(mouseX, mouseY)) {
                ((ITooltipped<?>)button).renderToolTip(this, mouseX, mouseY);
            }
        });
    }

    /**
     * Closes this screen and returns to the parent.
     *
     * Implementors should explicitly call this method when they want this behavior.
     */
    public void finish() {
        onClose();
        minecraft.openScreen(parent);
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(Bounds bounds) {

    }
}
