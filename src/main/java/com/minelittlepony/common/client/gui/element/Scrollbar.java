package com.minelittlepony.common.client.gui.element;

import com.minelittlepony.common.client.gui.GameGui;
import com.minelittlepony.common.client.gui.IViewRoot;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;
import com.minelittlepony.common.client.gui.dimension.Padding;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

/**
 * A scrollbar for interacting with scrollable UI elements.
 * <p>
 * Refer to {@code ScrollContainer} for an example of how this can be used.
 *
 * @author     Sollace
 */
public class Scrollbar extends DrawableHelper implements Element, IBounded {

    private boolean dragging = false;
    private boolean touching = false;

    private int scrollY = 0;

    private double scrollMomentum = 0;
    private float scrollFactor = 0;

    private final IViewRoot rootView;
    private final Bounds bounds = new Bounds(0, 0, 6, 0);

    private Bounds contentBounds = bounds;
    private Padding contentPadding = new Padding(0, 0, 0, 0);

    private int maxScrollY = 0;
    private int shiftFactor = 0;

    private double initialMouseY;

    public Scrollbar(IViewRoot rootView) {
        this.rootView = rootView;
    }

    /**
     * Sets up this scrollbar's position based on content position and size, and viewport element size.
     *
     * @param x The left X position (in pixels) of this scrollbar
     * @param y The top Y position (in pixels) of this scrollbar
     */
    public void reposition() {
        contentBounds = rootView.getContentBounds();
        contentPadding = rootView.getContentPadding();

        bounds.left = contentBounds.left + contentBounds.width;
        bounds.top = 0;
        bounds.height = rootView.getBounds().height;

        maxScrollY = contentBounds.height - bounds.height;
        if (maxScrollY < 0) {
            maxScrollY = 0;
        }
        scrollFactor = bounds.height == 0 ? 1 : contentBounds.height / bounds.height;

        scrollBy(0);
    }

    /**
     * Gets the vertical scroll amount.
     */
    public int getVerticalScrollAmount() {
        return scrollY;
    }

    /**
     * Gets the vertical scroll amount.
     */
    public int getHorizontalScrollAmount() {
        return 0;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {

        if (maxScrollY <= 0) {
            return;
        }

        if (!touching && !dragging) {
            scrollMomentum *= partialTicks;
            if (scrollMomentum > 0) {
                scrollBy(scrollMomentum);
            }

            if (shiftFactor != 0) {
                System.out.println("shifting by" + (shiftFactor * scrollFactor));
                scrollBy(shiftFactor * scrollFactor);
                shiftFactor = computeShiftFactor(mouseX, mouseY);
                System.out.println("new shift factor " + shiftFactor);
            }
        }

        renderVertical(matrices);
    }

    protected void renderVertical(MatrixStack matrices) {
        int scrollbarHeight = getScrubberLength(bounds.height, contentBounds.height);
        int scrollbarTop = getScrubberStart(scrollbarHeight, bounds.height, contentBounds.height);

        renderBackground(matrices, bounds.top, bounds.left, bounds.top + bounds.height, bounds.left + bounds.width);
        renderBar(matrices, bounds.left, bounds.left + bounds.width, scrollbarTop, scrollbarTop + scrollbarHeight);
    }

    protected int getScrubberStart(int scrollbarHeight, int elementHeight, int contentHeight) {
        if (maxScrollY <= 0) {
            return 0;
        }

        int scrollbarTop = bounds.top + getVerticalScrollAmount() * (elementHeight - scrollbarHeight) / maxScrollY;
        if (scrollbarTop < 0) {
            return 0;
        }
        return scrollbarTop;
    }

    protected int getScrubberLength(int elementL, int contentL) {
        return MathHelper.clamp(elementL * elementL / contentL, 32, elementL - 8);
    }

    private void renderBackground(MatrixStack matrices, int top, int left, int bottom, int right) {
        fill(matrices, left, top, right, bottom, 0x96000000);
    }

    private void renderBar(MatrixStack matrices, int left, int right, int top, int bottom) {
        fill(matrices, left, top, right,     bottom,     dragging ? 0xFF80808A : 0xFF808080);
        fill(matrices, left, top, right - 1, bottom - 1, dragging ? 0xFFC0C0FC : 0xFFC0C0C0);
    }

    private int computeShiftFactor(double mouseX, double mouseY) {
        double pos = mouseY;

        int scrubberLength = getScrubberLength(bounds.height, contentBounds.height);
        int scrubberStart = getScrubberStart(scrubberLength, bounds.height, contentBounds.height);

        if (pos < scrubberStart) {
            return 1;
        } else if (pos > scrubberStart + scrubberLength) {
            return -1;
        }

        return 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseY = calculateInternalYPosition(mouseY);

        touching = dragging = false;

        if (!isMouseOver(mouseX, mouseY)) {
            touching = true;
            return isMouseOver(mouseX, mouseY);
        }

        shiftFactor = computeShiftFactor(mouseX, mouseY);

        if (shiftFactor == 0) {
            GameGui.playSound(SoundEvents.UI_BUTTON_CLICK);
            dragging = true;
        }

        return isMouseOver(mouseX, mouseY);
    }

    private double calculateInternalYPosition(double mouseY) {
        mouseY += contentPadding.top - getVerticalScrollAmount();
        return Math.max(0, Math.min(mouseY, bounds.height));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int int_1, double differX, double differY) {
        mouseY = calculateInternalYPosition(mouseY);

        if (dragging) {
            scrollBy(initialMouseY - mouseY);
        } else if (touching) {
            scrollMomentum = mouseY - initialMouseY;

            scrollBy((mouseY - initialMouseY) / 4);
        }

        initialMouseY = mouseY;

        return isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = touching = false;
        shiftFactor = 0;
        System.out.println("release");

        return isMouseOver(mouseX, calculateInternalYPosition(mouseY));
    }

    /**
     * Scrolls this bar by the given amount.
     */
    public void scrollBy(double y) {
        scrollY = MathHelper.clamp((int)Math.floor(scrollY - y * scrollFactor), 0, maxScrollY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
       return maxScrollY > 0 && getBounds().contains(mouseX, mouseY);
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(Bounds bounds) {

    }

}
