package com.minelittlepony.common.client.gui;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.minelittlepony.common.client.gui.dimension.IBounded;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
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
        onClose();
        client.openScreen(parent);
    }
}
