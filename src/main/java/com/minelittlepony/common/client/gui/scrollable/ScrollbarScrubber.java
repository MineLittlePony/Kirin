package com.minelittlepony.common.client.gui.scrollable;

import com.minelittlepony.common.client.gui.dimension.Bounds;

import net.minecraft.util.math.MathHelper;

public class ScrollbarScrubber {

    private int currentPosition;
    private int targetPosition;
    private int momentum;

    private int maximumPosition;

    private int shiftDirection;
    private int scrollFactor;

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

    public void scrollBy(int amount) {
        scrollTo(currentPosition + (amount * scrollFactor), true);
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

    public void setShiftDirection(int shiftDirection) {
        this.shiftDirection = shiftDirection;
    }

    public void reposition(Bounds containerBounds, Bounds contentBounds) {
        int containerLength = orientation.getLength(containerBounds);
        int contentLength = orientation.getLength(contentBounds);

        maximumPosition = Math.max(0, containerLength - contentLength);
        scrollFactor = contentLength == 0 ? 1 : containerLength / contentLength;

        scrollTo(targetPosition, false);
    }

    public void update(Bounds containerBounds, Bounds contentBounds, double mouseX, double mouseY, float partialTicks, boolean grabbed) {
        if (currentPosition < targetPosition) {
            currentPosition++;
        }
        if (currentPosition > targetPosition) {
            currentPosition--;
        }

        if (!grabbed) {
            momentum *= 0.6;
            if (momentum > 0) {
                scrollBy(momentum);
            }

            if (shiftDirection != 0) {
                scrollBy(shiftDirection);
                shiftDirection = getShiftDirection(containerBounds, contentBounds, orientation.pick(mouseX, mouseY));
            }
        }
    }

    public int getShiftDirection(Bounds containerBounds, Bounds contentBounds, double coord) {
        int scrubberLength = getLength(containerBounds, contentBounds);
        int scrubberStart = getStart(scrubberLength, containerBounds);

        if (coord < scrubberStart) {
            return 1;
        }

        if (coord > scrubberStart + scrubberLength) {
            return -1;
        }

        return 0;
    }

    public int getStart(int scrubberLength, Bounds elementBounds) {
        if (maximumPosition <= 0) {
            return 0;
        }

        int elementLength = orientation.getLength(elementBounds);
        float position = (getPosition() / maximumPosition) * elementLength;

        return (int)MathHelper.clamp(position - (scrubberLength / 2F),
                0,
                elementLength - scrubberLength
        );
    }

    public int getLength(Bounds elementBounds, Bounds contentBounds) {
        int elementLength = orientation.getLength(elementBounds);
        int contentLength = orientation.getLength(contentBounds);

        return MathHelper.clamp(elementLength * (elementLength / contentLength),
                32,
                elementLength - 8
        );
    }

}
