package com.minelittlepony.common.client.gui.element;

import com.minelittlepony.common.client.gui.GameGui;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class Scrollbar extends DrawableHelper implements Element, IBounded {

    private boolean dragging = false;
    private boolean touching = false;

    private int scrollY = 0;

    private double scrollMomentum = 0;
    private float scrollFactor = 0;

    private final Bounds bounds = new Bounds(0, 0, 6, 0);

    private int maxScrollY = 0;
    private int shiftFactor = 0;

    private int contentHeight;

    private double initialMouseY;

    public Scrollbar() {

    }

    public void reposition(int x, int y, int elementHeight, int contentHeight) {
        bounds.left = x;
        bounds.top = y;
        bounds.height = elementHeight;
        this.contentHeight = contentHeight;

        maxScrollY = contentHeight - elementHeight;
        if (maxScrollY < 0) {
            maxScrollY = 0;
        }
        scrollFactor = elementHeight == 0 ? 1 : contentHeight / elementHeight;

        scrollBy(0);
    }

    public int getScrollAmount() {
        return scrollY;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        if (!touching && !dragging) {
            scrollMomentum *= partialTicks;
            if (scrollMomentum > 0) {
                scrollBy(scrollMomentum);
            }

            if (shiftFactor != 0) {
                scrollBy(shiftFactor * scrollFactor);
                shiftFactor = computeShiftFactor(mouseX, mouseY);
            }
        }

        if (maxScrollY <= 0) {
            return;
        }

        renderVertical();
    }

    protected void renderVertical() {
        int scrollbarHeight = getScrubberLength(bounds.height, contentHeight);
        int scrollbarTop = getScrubberStart(scrollbarHeight, bounds.height, contentHeight);

        renderBackground(bounds.top, bounds.left, bounds.top + bounds.height, bounds.left + bounds.width);
        renderBar(bounds.left, bounds.left + bounds.width, scrollbarTop, scrollbarTop + scrollbarHeight);
    }

    protected int getScrubberStart(int scrollbarHeight, int elementHeight, int contentHeight) {
        if (maxScrollY <= 0) {
            return 0;
        }

        int scrollbarTop = bounds.top + getScrollAmount() * (elementHeight - scrollbarHeight) / maxScrollY;
        if (scrollbarTop < 0) {
            return 0;
        }
        return scrollbarTop;
    }

    protected int getScrubberLength(int elementL, int contentL) {
        return MathHelper.clamp(elementL * elementL / contentL, 32, elementL - 8);
    }

    private void renderBackground(int top, int left, int bottom, int right) {
        fill(left, top, right, bottom, 0x96000000);
    }

    private void renderBar(int left, int right, int top, int bottom) {
        fill(left, top, right,     bottom,     dragging ? 0xFF80808A : 0xFF808080);
        fill(left, top, right - 1, bottom - 1, dragging ? 0xFFC0C0FC : 0xFFC0C0C0);
    }

    private int computeShiftFactor(double mouseX, double mouseY) {
        double pos = mouseY;

        int scrubberLength = getScrubberLength(bounds.height, contentHeight);
        int scrubberStart = getScrubberStart(scrubberLength, bounds.height, contentHeight);

        if (pos < scrubberStart) {
            return 1;
        } else if (pos > scrubberStart + scrubberLength) {
            return -1;
        }

        return 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseY -= getScrollAmount();

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

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int int_1, double differX, double differY) {
        mouseY -= getScrollAmount();

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

        return isMouseOver(mouseX, mouseY - getScrollAmount());
    }

    public void scrollBy(double y) {
        scrollY = MathHelper.clamp((int)Math.floor(scrollY - y * scrollFactor), 0, maxScrollY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
       return getBounds().contains(mouseX, mouseY);
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(Bounds bounds) {

    }

}
