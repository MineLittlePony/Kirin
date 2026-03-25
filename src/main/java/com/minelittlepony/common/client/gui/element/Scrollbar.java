package com.minelittlepony.common.client.gui.element;

import com.minelittlepony.common.client.gui.GameGui;
import com.minelittlepony.common.client.gui.IViewRoot;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;
import com.minelittlepony.common.client.gui.scrollable.ScrollOrientation;
import com.minelittlepony.common.client.gui.scrollable.ScrollbarScrubber;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.cursor.CursorTypes;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.sounds.SoundEvents;

/**
 * A scrollbar for interacting with scrollable UI elements.
 * <p>
 * Refer to {@code ScrollContainer} for an example of how this can be used.
 *
 * @author     Sollace
 */
public class Scrollbar implements Renderable, GuiEventListener, IBounded {

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

    /**
     * Whether the scrollbar must position itself at the far right of its assigned container rather than the right-most edge of the content.
     */
    public boolean layoutToEnd;

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

        int end = orientation.getWidth(rootView.getBounds()) - SCROLLBAR_THICKNESS;
        if (!layoutToEnd) {
            end = Math.min(end, orientation.pick(contentBounds.bottom(), contentBounds.right()));
        }

        bounds.left = orientation.pick(0, end);
        bounds.top = orientation.pick(end, 0);
        bounds.height = orientation.pick(SCROLLBAR_THICKNESS, rootView.getBounds().height);
        bounds.width = orientation.pick(rootView.getBounds().width, SCROLLBAR_THICKNESS);

        scrubber.reposition(containerBounds, contentBounds);
    }

    public ScrollOrientation getOrientation() {
        return orientation;
    }

    public ScrollbarScrubber getScrubber() {
        return scrubber;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
        if (scrubber.getMaximum() <= 0) {
            return;
        }

        scrubber.update(rootView.getBounds(), contentBounds, mouseX, mouseY, partialTicks, touching || dragging);
        renderScrubber(scrubber, orientation, context);

        if (getBounds().contains(mouseX, mouseY)) {
            float position = scrubber.getGrabPosition(orientation.pick(mouseX, mouseY));
            if (position >= 0 && position <= 1) {
                context.requestCursor(orientation == ScrollOrientation.VERTICAL ? CursorTypes.RESIZE_NS : CursorTypes.RESIZE_EW);
            }
        }
    }

    private void renderScrubber(ScrollbarScrubber scrubber, ScrollOrientation orientation, GuiGraphicsExtractor context) {
        int scrubberStart = scrubber.getStart();
        int scrubberEnd = scrubberStart + scrubber.getLength();

        renderBackground(context, bounds.top, bounds.left, bounds.bottom(), bounds.right());
        renderBar(context,
            orientation.pick(scrubberStart, bounds.left), orientation.pick(scrubberEnd, bounds.right()),
            orientation.pick(bounds.top, scrubberStart), orientation.pick(bounds.bottom(), scrubberEnd)
        );
    }

    private void renderBackground(GuiGraphicsExtractor context, int top, int left, int bottom, int right) {
        context.fill(left, top, right, bottom, 0x96000000);
    }

    private void renderBar(GuiGraphicsExtractor context, int left, int right, int top, int bottom) {
        context.fill(left, top, right,     bottom,     dragging ? 0xFF80808A : 0xFF808080);
        context.fill(left, top, right - 1, bottom - 1, dragging ? 0xFFC0C0FC : 0xFFC0C0C0);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        double internalMouseY = calculateInternalYPosition(click.y());
        double internalMouseX = calculateInternalXPosition(click.x());

        double mousePosition = orientation.pick(internalMouseX, internalMouseY);

        touching = dragging = false;

        if (!isMouseOver(click)) {
            touching = true;
            return false;
        }

        float grabPosition = scrubber.getGrabPosition(mousePosition);

        if (grabPosition < 0 || grabPosition > 1) {
            scrubber.scrollBy((int)Math.signum(grabPosition) * 50, true);
        } else {
            GameGui.playSound(SoundEvents.UI_BUTTON_CLICK);
            dragging = true;
        }

        return isMouseOver(click);
    }

    private double calculateInternalYPosition(double mouseY) {
        return mouseY + rootView.getScrollY() + rootView.getContentPadding().top;
    }

    private double calculateInternalXPosition(double mouseX) {
        return mouseX + rootView.getScrollX() + rootView.getContentPadding().left;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double differX, double differY) {
        double change = -orientation.pick(differX, differY);

        if (dragging) {
            scrubber.scrollBy(-(int)change, false);
        } else if (touching) {
            scrubber.scrollBy(-(int)change * 16, true);
            scrubber.setMomentum(-(int)change);
        }

        return isMouseOver(click);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        dragging = touching = false;

        return isMouseOver(click);
    }

    /**
     * Scrolls this bar by the given amount.
     */
    public void scrollBy(double amount) {
        scrubber.scrollBy(-amount, true);
    }

    public boolean isMouseOver(MouseButtonEvent click) {
        double mouseX = calculateInternalXPosition(click.x());
        double mouseY = calculateInternalYPosition(click.y());
        return scrubber.getMaximum() > 0 && getBounds().contains(mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        mouseY = calculateInternalYPosition(mouseY);
        mouseX = calculateInternalXPosition(mouseX);
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
    public boolean keyPressed(KeyEvent input) {
        if (isFocused()) {
            if (input.input() == orientation.pick(InputConstants.KEY_LEFT, InputConstants.KEY_UP)) {
                scrubber.scrollBy(-10, true);
                return true;
            }
            if (input.input() == orientation.pick(InputConstants.KEY_RIGHT, InputConstants.KEY_DOWN)) {
                scrubber.scrollBy(10, true);
                return true;
            }
            if (input.input() == InputConstants.KEY_END) {
                scrubber.scrollToEnd(true);
                return true;
            }
            if (input.input() == InputConstants.KEY_HOME) {
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
