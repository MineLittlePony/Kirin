package com.minelittlepony.common.client.gui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiConsumer;

import org.joml.Matrix3x2fStack;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.minelittlepony.common.client.gui.element.Scrollbar;
import com.minelittlepony.common.client.gui.scrollable.ScrollOrientation;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.CommonComponents;

/**
 * A container implementing proper overflow mechanics and smooth scrolling.
 * Scroll amounts are determined dynamically by the bounds of the elements
 * placed inside of it and the outer dimensions of the screen and/or this container.
 * <p>
 * Can serve as your root screen or as an element inside a screen.
 *
 * @author     Sollace
 *
 */
public class ScrollContainer extends GameGui {
    /**
     * The vertical scrollbar for this container.
     */
    public final Scrollbar verticalScrollbar = new Scrollbar(this, ScrollOrientation.VERTICAL);
    /**
     * The horizontal scrollbar for this container.
     */
    public final Scrollbar horizontalScrollbar = new Scrollbar(this, ScrollOrientation.HORIZONTAL);

    /**
     * The external padding around this container. (default: [0,0,0,0])
     */
    public final Padding margin = new Padding(0, 0, 0, 0);

    /**
     * The ARGB colour of the background
     */
    public int backgroundColor = 0x66000000;
    /**
     * The ARGB colour of the fade at the top and bottom of this container.
     */
    public int decorationColor = 0xEE000000;

    private final Deque<Runnable> delayedCalls = new ArrayDeque<>();

    public ScrollContainer() {
        super(CommonComponents.EMPTY);
        horizontalScrollbar.layoutToEnd = true;
    }

    @Override
    public void init() {
        init(() -> {});
    }

    /**
     * Initialises this container.
     * Called on init to recalculate the flow of elements and append its contents.
     *
     * @param contentInitializer A method to call to initialise this element's contents.
     */
    public void init(Runnable contentInitializer) {
        clearWidgets();

        width = getBounds().width = minecraft.getWindow().getGuiScaledWidth() - margin.left - margin.right;
        height = getBounds().height = minecraft.getWindow().getGuiScaledHeight() - margin.top - margin.bottom;
        getBounds().top = margin.top;
        getBounds().left = margin.left;

        contentInitializer.run();

        verticalScrollbar.reposition();
        horizontalScrollbar.reposition();
        getChildElements().add(0, verticalScrollbar);
        getChildElements().add(0, horizontalScrollbar);
    }

    @Override
    public final void render(GuiGraphics context, int mouseX, int mouseY, float tickDelta) {

        Matrix3x2fStack matrices = context.pose();
        matrices.pushMatrix();
        getBounds().scissor(context);
        getBounds().translate(matrices);

        drawBackground(context, mouseX, mouseY, tickDelta);

        int subMouseX = mouseX < margin.left || mouseX > margin.left + getBounds().width ? -1000 : mouseX + getMouseXOffset();
        int subMouseY = mouseY < margin.top || mouseY > margin.top + getBounds().height ? -1000 : mouseY + getMouseYOffset();

        GuiGraphics subContext = new GuiGraphics(minecraft, context.guiRenderState, subMouseX, subMouseY);
        getBounds().scissor(subContext);
        subContext.pose().set(context.pose());
        subContext.pose().pushMatrix();
        subContext.pose().translate(
                getScrollX() + getContentPadding().left,
                getScrollY() + getContentPadding().top
        );

        renderContents(subContext, subMouseX, subMouseY, tickDelta);

        verticalScrollbar.render(context,
                mouseX - margin.left,
                mouseY - margin.top,
                tickDelta
        );
        horizontalScrollbar.render(context,
                mouseX - margin.left,
                mouseY - margin.top,
                tickDelta
        );

        drawDecorations(context, mouseX, mouseY, tickDelta);

        matrices.popMatrix();

        context.disableScissor();

        drawOverlays(context, mouseX, mouseY, tickDelta);

        subContext.disableScissor();
        subContext.renderDeferredElements();
        subContext.pose().popMatrix();
    }

    protected void renderContents(GuiGraphics context, int mouseX, int mouseY, float tickDelta) {
        super.render(context, mouseX, mouseY, tickDelta);
    }

    @Deprecated
    @Override
    public final void renderBackground(GuiGraphics context, int mouseX, int mouseY, float tickDelta) { }

    protected void drawBackground(GuiGraphics context, int mouseX, int mouseY, float tickDelta) {
        context.fill(0, 0, width, height, backgroundColor);
    }

    protected void drawDecorations(GuiGraphics context, int mouseX, int mouseY, float tickDelta) {
        context.fillGradient(0, -3, width, 5, decorationColor, 0);
        context.fillGradient(0, height - 6, width, height + 3, 0, decorationColor);
    }

    protected void drawOverlays(GuiGraphics context, int mouseX, int mouseY, float tickDelta) {
        Runnable task;
        Window window = Minecraft.getInstance().getWindow();
        context.enableScissor(0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight());

        while ((task = delayedCalls.poll()) != null) {
            task.run();
        }

        context.disableScissor();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            super.mouseDragged(new MouseButtonEvent(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), new MouseButtonInfo(0, 0)), 0, 0);
        }
    }

    public int getMouseYOffset() {
        return -getBounds().top - getScrollY() - getContentPadding().top;
    }

    public int getMouseXOffset() {
        return -getBounds().left - getScrollX() - getContentPadding().left;
    }

    @Override
    public int getScrollX() {
        return -horizontalScrollbar.getScrubber().getPosition();
    }

    @Override
    public int getScrollY() {
        return -verticalScrollbar.getScrubber().getPosition();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return isMouseOver(click.x(), click.y()) && super.mouseClicked(getContentClick(click), doubled);
    }

    protected MouseButtonEvent getContentClick(MouseButtonEvent click) {
        return new MouseButtonEvent(click.x() + getMouseXOffset(), click.y() + getMouseYOffset(), click.buttonInfo());
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        return isMouseOver(click.x(), click.y()) && super.mouseReleased(getContentClick(click));
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double differX, double differY) {
        return super.mouseDragged(getContentClick(click), differX, differY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double xScroll, double yScroll) {
        verticalScrollbar.scrollBy((float)Math.signum(yScroll) * 12);
        horizontalScrollbar.scrollBy((float)Math.signum(xScroll) * 12);

        return isMouseOver(mouseX, mouseY) && super.mouseScrolled(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), xScroll, yScroll);
    }

    protected void renderOutside(GuiGraphics context, int mouseX, int mouseY, BiConsumer<Integer, Integer> renderCall) {
        delayedCalls.add(() -> {
            context.pose().pushMatrix();
            renderCall.accept(mouseX - getMouseXOffset(), mouseY - getMouseYOffset());
            context.pose().popMatrix();
        });
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return getBounds().contains(mouseX, mouseY);
    }

    @Override
    public void setBounds(Bounds bounds) {
        margin.top = bounds.top;
        margin.left = bounds.left;
    }

    @Override
    protected boolean isUnFixedPosition(Bounds bound) {
        return bound != verticalScrollbar.getBounds() && bound != horizontalScrollbar.getBounds();
    }
}
