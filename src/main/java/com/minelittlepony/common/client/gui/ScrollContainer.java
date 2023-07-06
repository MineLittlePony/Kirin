package com.minelittlepony.common.client.gui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.minelittlepony.common.client.gui.element.Scrollbar;
import com.minelittlepony.common.client.gui.scrollable.ScrollOrientation;
import com.minelittlepony.common.util.render.ClippingSpace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

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
     * The scrollbar for this container.
     */
    public final Scrollbar verticalScrollbar = new Scrollbar(this, ScrollOrientation.VERTICAL);
    @Deprecated
    public final Scrollbar scrollbar = verticalScrollbar;

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
        super(ScreenTexts.EMPTY);
        horizontalScrollbar.layoutToEnd = true;

        init(MinecraftClient.getInstance(), 0, 0);
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
        clearChildren();

        width = getBounds().width = client.getWindow().getScaledWidth() - margin.left - margin.right;
        height = getBounds().height = client.getWindow().getScaledHeight() - margin.top - margin.bottom;
        getBounds().top = margin.top;
        getBounds().left = margin.left;

        contentInitializer.run();

        verticalScrollbar.reposition();
        horizontalScrollbar.reposition();
        getChildElements().add(verticalScrollbar);
        getChildElements().add(horizontalScrollbar);
    }

    @Override
    public final void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        ClippingSpace.renderClipped(margin.left, margin.top, getBounds().width, getBounds().height, () -> {
            matrices.push();
            getBounds().translate(matrices);

            drawBackground(matrices, mouseX, mouseY, partialTicks);

            Padding padding = getContentPadding();

            matrices.push();
            matrices.translate(
                    getScrollX() + padding.left,
                    getScrollY() + padding.top, 0);

            renderContents(matrices,
                    mouseX < margin.left || mouseX > margin.left + getBounds().width ? -1000 : mouseX + getMouseXOffset(),
                    mouseY < margin.top || mouseY > margin.top + getBounds().height ? -1000 : mouseY + getMouseYOffset(),
                    partialTicks);

            matrices.pop();

            verticalScrollbar.render(matrices,
                    mouseX - margin.left,
                    mouseY - margin.top,
                    partialTicks
            );
            horizontalScrollbar.render(matrices,
                    mouseX - margin.left,
                    mouseY - margin.top,
                    partialTicks
            );

            drawDecorations(matrices, mouseX, mouseY, partialTicks);

            matrices.pop();
        });

        drawOverlays(matrices, mouseX, mouseY, partialTicks);
    }

    protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        super.render(matrices, mouseX, mouseY, partialTicks);
    }

    protected void drawBackground(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        fill(matrices, 0, 0, width, height, backgroundColor);
    }

    protected void drawDecorations(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        fillGradient(matrices, 0, -3, width, 5, decorationColor, 0);
        fillGradient(matrices, 0, height - 6, width, height + 3, 0, decorationColor);
    }

    protected void drawOverlays(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        Runnable task;
        while ((task = delayedCalls.poll()) != null) {
            task.run();
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            super.mouseDragged(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), 0, 0, 0);
        }
    }

    public int getMouseYOffset() {
        return -margin.top - getScrollY() - getContentPadding().top;
    }

    public int getMouseXOffset() {
        return -margin.left - getScrollX() - getContentPadding().left;
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isMouseOver(mouseX, mouseY) && super.mouseClicked(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return isMouseOver(mouseX, mouseY) && super.mouseReleased(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double differX, double differY) {
        return super.mouseDragged(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), button, differX, differY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        verticalScrollbar.scrollBy((float)Math.signum(amount) * 12);

        return isMouseOver(mouseX, mouseY) && super.mouseScrolled(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), amount);
    }

    @Override
    public void renderTooltip(MatrixStack matrices, List<Text> tooltip, Optional<TooltipData> data, int mouseX, int mouseY) {
        renderOutside(matrices, mouseX, mouseY, (x, y) -> {
            if (client.currentScreen == this) {
                super.renderTooltip(matrices, tooltip, data, x, y);
            } else {
                client.currentScreen.renderTooltip(matrices, tooltip, x, y);
            }
        });
    }

    @Override
    public void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> tooltip, int mouseX, int mouseY) {
        renderOutside(matrices, mouseX, mouseY, (x, y) -> {
            if (client.currentScreen == this) {
                super.renderOrderedTooltip(matrices, tooltip, x, y);
            } else {
                client.currentScreen.renderOrderedTooltip(matrices, tooltip, x, y);
            }
        });
    }

    protected void renderOutside(MatrixStack matrices, int mouseX, int mouseY, BiConsumer<Integer, Integer> renderCall) {
        delayedCalls.add(() -> {
            ClippingSpace.renderUnclipped(() -> {
                matrices.push();
                renderCall.accept(mouseX - getMouseXOffset(), mouseY - getMouseYOffset());
                matrices.pop();
            });
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
