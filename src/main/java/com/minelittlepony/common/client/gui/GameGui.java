package com.minelittlepony.common.client.gui;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;

/**
 * Optional root element for a screen using Kirin functionality.
 * <p>
 * This class implements some QOL features, such as bounds, text utilities, etc of Kirin UI elements.
 *
 * @author     Sollace
 *
 */
public class GameGui extends Screen {
    /**
     * The parent screen that existed prior to opening this Screen.
     * If present, this screen will replace this one upon closing.
     */
    @Nullable
    protected final Screen parent;

    public static boolean drawAllDebugBounds;

    public boolean drawDebugBounds;

    /**
     * Creates a new GameGui with the given title, and parent as the screen currently displayed.
     *
     * @param title The screen's title
     */
    protected GameGui(Component title) {
        this(title, Minecraft.getInstance().screen);
    }

    /**
     * Creates a new GameGui with the given title, and parent as the screen currently displayed.
     *
     * @param title The screen's title.
     * @param parent The parent screen.
     */
    protected GameGui(Component title, @Nullable Screen parent) {
        super(title);

        this.parent = parent;
    }

    /**
     * Plays a sound event.
     *
     * @param event The sound event to play.
     */
    public static void playSound(SoundEvent event) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(event, 1));
    }

    /**
     * Plays a sound event.
     *
     * @param event The sound event to play.
     */
    public static void playSound(Holder.Reference<SoundEvent> event) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(event, 1));
    }

    /**
     * Determines whether the a key is currently pressed.
     *
     * @param key The GLFW keyCode to check
     * @return True if the key is pressed.
     */
    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key);
    }

    /**
     * Creates a supplier for checking a specific key.
     *
     * @param key The GLFW keyCode to check
     * @return A supplier that returns True if the key is pressed.
     */
    public static Supplier<Boolean> keyCheck(int key) {
        return () -> isKeyDown(key);
    }

    /**
     * Closes this screen and returns to the parent.
     *
     * Implementors should explicitly call this method when they want this behavior.
     */
    public void finish() {
        onClose();
        minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
        drawDebugOverlays(context, mouseX, mouseY);
    }

    private void drawDebugOverlays(GuiGraphics context, int mouseX, int mouseY) {
        if (drawDebugBounds || drawAllDebugBounds) {
            context.pose().pushMatrix();
            Padding padding = getContentPadding();
            Padding scrollOffset = new Padding(-getScrollY() - padding.top, -getScrollX() - padding.left, 0, 0);

            for (Bounds bound : getAllBounds()) {
                int color = 0xAA000000 | bound.hashCode() << 6;
                if (!isUnFixedPosition(bound)) {
                    bound = bound.offset(scrollOffset);
                }
                if (bound.contains(mouseX, mouseY)) {
                    bound.debugMeasure(context);
                }
                bound.draw(context, color);
            }

            context.pose().popMatrix();
        }
    }

    protected boolean isUnFixedPosition(Bounds bound) {
        return true;
    }
}
