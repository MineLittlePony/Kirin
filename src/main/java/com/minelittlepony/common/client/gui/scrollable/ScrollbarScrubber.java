package com.minelittlepony.common.client.gui.scrollable;

import com.minelittlepony.common.client.gui.dimension.Bounds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class ScrollbarScrubber {

    private int currentPosition;
    private int targetPosition;
    private int momentum;

    private int maximumPosition;

    private float scrollSpeed;

    private int scrubberLength;
    private int gapLength;

    private final ScrollOrientation orientation;

    public ScrollbarScrubber(ScrollOrientation orientation) {
        this.orientation = orientation;
    }

    public int getPosition() {
        return currentPosition;
    }

    public int getMaximum() {
        return maximumPosition;
    }

    public int getStart() {
        return maximumPosition <= 0 ? 0 : (int)Math.max(0, (getPosition() / (float)maximumPosition) * gapLength);
    }

    public int getLength() {
        return scrubberLength;
    }

    public void scrollBy(double amount, boolean animate) {
        scrollTo(currentPosition + (int)(amount * scrollSpeed), animate);
    }

    /**
     * Scrolls the bar to the end of the page.
     */
    public void scrollToEnd(boolean animate) {
        scrollTo(maximumPosition, animate);
    }

    /**
     * Resets the scroll position to it's initial value (0).
     */
    public void scrollToBeginning(boolean animate) {
        scrollTo(0, animate);
    }

    public void scrollTo(int position, boolean animate) {
        targetPosition = MathHelper.clamp(position, 0, maximumPosition);
        if (!animate) {
            currentPosition = targetPosition;
        }
        setMomentum(0);
    }

    public void setMomentum(int momentum) {
        this.momentum = momentum;
    }

    public void reposition(Bounds containerBounds, Bounds contentBounds) {
        float containerLength = orientation.getLength(containerBounds);
        float contentLength = orientation.getLength(contentBounds);

        maximumPosition = (int)Math.max(0, contentLength - containerLength);
        scrollSpeed = containerLength == 0 ? 1 : contentLength / containerLength;
        scrollSpeed *= (float)MinecraftClient.getInstance().getWindow().getScaleFactor() / 3F;

        scrubberLength = (int)MathHelper.clamp(containerLength - (float)Math.sqrt(maximumPosition), 15, containerLength / 2);
        gapLength = (int)containerLength - scrubberLength;

        scrollTo(targetPosition, false);
    }

    public void update(Bounds containerBounds, Bounds contentBounds, double mouseX, double mouseY, float partialTicks, boolean grabbed) {
        if (currentPosition < targetPosition) {
            currentPosition += Math.max(1, (targetPosition - currentPosition) / 2);
        }
        if (currentPosition > targetPosition) {
            currentPosition -= Math.max(1, (currentPosition - targetPosition) / 2);
        }

        if (!grabbed && (momentum *= 0.6) > 0) {
            scrollBy(momentum, false);
        }
    }

    public float getGrabPosition(double coord) {
        int scrubberStart = getStart();

        if (coord < scrubberStart) {
            return -1;
        }

        if (coord > (scrubberStart + scrubberLength)) {
            return 2;
        }

        return MathHelper.clamp((float)(coord - scrubberStart) / scrubberLength, 0, 1);
    }
}
