package com.minelittlepony.common.client.gui;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;
import com.minelittlepony.common.client.gui.dimension.Padding;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

/**
 * Optional root element for a screen using Kirin functionality.
 * <p>
 * This class implements some QOL features, such as bounds, text utilities, etc of Kirin UI elements.
 *
 * @author     Sollace
 *
 */
public class GameGui extends Screen implements IViewRoot, IBounded, ITextContext, IViewRootDefaultImpl {
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
    protected GameGui(Text title) {
        this(title, MinecraftClient.getInstance().currentScreen);
    }

    /**
     * Creates a new GameGui with the given title, and parent as the screen currently displayed.
     *
     * @param title The screen's title.
     * @param parent The parent screen.
     */
    protected GameGui(Text title, @Nullable Screen parent) {
        super(title);

        this.parent = parent;
    }

    /**
     * Plays a sound event.
     *
     * @param event The sound event to play.
     */
    public static void playSound(SoundEvent event) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(event, 1));
    }

    /**
     * Plays a sound event.
     *
     * @param event The sound event to play.
     */
    public static void playSound(RegistryEntry.Reference<SoundEvent> event) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(event, 1));
    }

    /**
     * Determines whether the a key is currently pressed.
     *
     * @param key The GLFW keyCode to check
     * @return True if the key is pressed.
     */
    public static boolean isKeyDown(int key) {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), key);
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
        removed();
        client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
        drawDebugOverlays(context, mouseX, mouseY);
    }

    private void drawDebugOverlays(DrawContext context, int mouseX, int mouseY) {
        if (drawDebugBounds || drawAllDebugBounds) {
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, -90);
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

            context.getMatrices().pop();
        }
    }

    protected boolean isUnFixedPosition(Bounds bound) {
        return true;
    }
}
