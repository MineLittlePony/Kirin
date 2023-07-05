package com.minelittlepony.common.client.gui.element;

import org.lwjgl.glfw.GLFW;

import com.minelittlepony.common.client.gui.GameGui;
import com.minelittlepony.common.client.gui.IViewRoot;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;
import com.minelittlepony.common.client.gui.scrollable.ScrollOrientation;
import com.minelittlepony.common.client.gui.scrollable.ScrollbarScrubber;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.sound.SoundEvents;

/**
 * A scrollbar for interacting with scrollable UI elements.
 * <p>
 * Refer to {@code ScrollContainer} for an example of how this can be used.
 *
 * @author     Sollace
 */
public class Scrollbar implements Element, Drawable, IBounded {

    public static final int SCROLLBAR_THICKNESS = 6;

    private boolean dragging;
    private boolean touching;
    private boolean focused;

    private final ScrollbarScrubber scrubber;
    private final ScrollOrientation orientation;

    private final IViewRoot rootView;
    private final Bounds bounds;
    private Bounds containerBounds;
    private Bounds contentBounds;

    private double prevMousePosition;

    /**
     * Whether the scrollbar must position itself at the far right of its assigned container rather than the right-most edge of the content.
     */
    public boolean layoutToEnd;

    @Deprecated
    public Scrollbar(IViewRoot rootView) {
        this(rootView, ScrollOrientation.VERTICAL);
    }

    public Scrollbar(IViewRoot rootView, ScrollOrientation orientation) {
        this.rootView = rootView;
        this.orientation = orientation;
        this.bounds = new Bounds(0, 0, orientation.pick(0, SCROLLBAR_THICKNESS), orientation.pick(SCROLLBAR_THICKNESS, 0));
        this.contentBounds = bounds;
        this.containerBounds = rootView.getBounds();
        this.scrubber = new ScrollbarScrubber(orientation);
    }

    /**
     * Sets up this scrollbar's position based on content position and size, and viewport element size.
     */
    public void reposition() {
        contentBounds = rootView.getContentBounds().offset(rootView.getContentPadding());
        containerBounds = rootView.getBounds();

        int end = layoutToEnd ? orientation.getWidth(rootView.getBounds()) - SCROLLBAR_THICKNESS : orientation.pick(contentBounds.bottom(), contentBounds.right());

        bounds.left = orientation.pick(0, end);
        bounds.top = orientation.pick(end, 0);
        bounds.height = orientation.pick(SCROLLBAR_THICKNESS, rootView.getBounds().height);
        bounds.width = orientation.pick(rootView.getBounds().width, SCROLLBAR_THICKNESS);

        scrubber.reposition(containerBounds, contentBounds);
    }

    /**
     * Gets the vertical scroll amount.
     */
    @Deprecated
    public int getVerticalScrollAmount() {
        return orientation == ScrollOrientation.VERTICAL ? scrubber.getPosition() : 0;
    }

    public ScrollbarScrubber getScrubber() {
        return scrubber;
    }

    /**
     * Gets the vertical scroll amount.
     */
    @Deprecated
    public int getHorizontalScrollAmount() {
        return orientation == ScrollOrientation.HORIZONTAL ? scrubber.getPosition() : 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if (scrubber.getMaximum() <= 0) {
            return;
        }

        scrubber.update(rootView.getBounds(), contentBounds, mouseX, mouseY, partialTicks, touching || dragging);
        renderScrubber(scrubber, orientation, context);
    }

    private void renderScrubber(ScrollbarScrubber scrubber, ScrollOrientation orientation, DrawContext context) {
        int scrubberStart = scrubber.getStart();
        int scrubberEnd = scrubberStart + scrubber.getLength();

        renderBackground(context, bounds.top, bounds.left, bounds.bottom(), bounds.right());
        renderBar(context,
            orientation.pick(scrubberStart, bounds.left), orientation.pick(scrubberEnd, bounds.right()),
            orientation.pick(bounds.top, scrubberStart), orientation.pick(bounds.bottom(), scrubberEnd)
        );
    }

    private void renderBackground(DrawContext context, int top, int left, int bottom, int right) {
        context.fill(left, top, right, bottom, 0x96000000);
    }

    private void renderBar(DrawContext context, int left, int right, int top, int bottom) {
        context.fill(left, top, right,     bottom,     dragging ? 0xFF80808A : 0xFF808080);
        context.fill(left, top, right - 1, bottom - 1, dragging ? 0xFFC0C0FC : 0xFFC0C0C0);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseY = calculateInternalYPosition(mouseY);
        mouseX = calculateInternalXPosition(mouseX);

        double mousePosition = orientation.pick(mouseX, mouseY);

        touching = dragging = false;

        if (!isMouseOver(mouseX, mouseY)) {
            touching = true;
            return isMouseOver(mouseX, mouseY);
        }

        float grabPosition = scrubber.getGrabPosition(mousePosition);

        if (grabPosition < 0 || grabPosition > 1) {
            scrubber.scrollBy((int)Math.signum(grabPosition) * 50, true);
        } else {
            GameGui.playSound(SoundEvents.UI_BUTTON_CLICK);
            dragging = true;
        }
        prevMousePosition = mousePosition;

        return isMouseOver(mouseX, mouseY);
    }

    private double calculateInternalYPosition(double mouseY) {
        return mouseY + rootView.getScrollY() + rootView.getContentPadding().top;
    }

    private double calculateInternalXPosition(double mouseX) {
        return mouseX + rootView.getScrollX() + rootView.getContentPadding().left;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double differX, double differY) {
        mouseY = calculateInternalYPosition(mouseY);
        mouseX = calculateInternalXPosition(mouseX);

        double mousePosition = orientation.pick(mouseX, mouseY);

        if (dragging) {
            scrubber.scrollBy(-(int)(prevMousePosition - mousePosition), false);
        } else if (touching) {
            scrubber.scrollBy((int)(mousePosition - prevMousePosition) / 16, true);
            scrubber.setMomentum((int)(mousePosition - prevMousePosition));
        }

        prevMousePosition = mousePosition;

        return isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = touching = false;

        return isMouseOver(
            calculateInternalXPosition(mouseX),
            calculateInternalYPosition(mouseY)
        );
    }

    /**
     * Scrolls this bar by the given amount.
     */
    public void scrollBy(double amount) {
        scrubber.scrollBy(-amount, true);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
       return scrubber.getMaximum() > 0 && getBounds().contains(mouseX, mouseY);
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(Bounds bounds) {

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isFocused()) {
            if (keyCode == orientation.pick(GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_UP)) {
                scrubber.scrollBy(-10, true);
                return true;
            }
            if (keyCode == orientation.pick(GLFW.GLFW_KEY_RIGHT, GLFW.GLFW_KEY_DOWN)) {
                scrubber.scrollBy(10, true);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_END) {
                scrubber.scrollToEnd(true);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_HOME) {
                scrubber.scrollToBeginning(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused || dragging;
    }
}
