package com.minelittlepony.common.client.gui;

import java.util.List;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.minelittlepony.common.client.gui.element.Scrollbar;
import com.minelittlepony.common.util.render.ClippingSpace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;

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
public class ScrollContainer extends GameGui implements IViewRoot {

    /**
     * The scrollbar for this container.
     */
    protected final Scrollbar scrollbar = new Scrollbar(this);

    /**
     * The external padding around this container. (default: [0,0,0,0])
     */
    public final Padding margin = new Padding(0, 0, 0, 0);
    /**
     * The external padding around the content inside this container. (default: [0,0,0,0])
     */
    public final Padding padding = new Padding(0, 0, 0, 0);

    public ScrollContainer() {
        super(LiteralText.EMPTY);

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
        buttons.clear();
        children.clear();

        width = getBounds().width = client.getWindow().getScaledWidth() - margin.left - margin.right;
        height = getBounds().height = client.getWindow().getScaledHeight() - margin.top - margin.bottom;
        getBounds().top = margin.top;
        getBounds().left = margin.left;

        contentInitializer.run();

        scrollbar.reposition();
        children.add(scrollbar);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {

        ClippingSpace.renderClipped(margin.left, margin.top, getBounds().width, getBounds().height, () -> {
            int scroll = scrollbar.getVerticalScrollAmount();

            matrices.push();
            matrices.translate(margin.left, margin.top, 0);

            fill(matrices, 0, 0, width, height, 0x66000000);

            matrices.push();
            matrices.translate(padding.left, 0, 0);

            scrollbar.render(matrices,
                    mouseX - margin.left,
                    mouseY - margin.top,
                    partialTicks);

            matrices.translate(0, -scroll + padding.top, 0);

            super.render(matrices,
                    mouseX < margin.left || mouseX > margin.left + getBounds().width ? -1 : mouseX + getMouseXOffset(),
                    mouseY < margin.top || mouseY > margin.top + getBounds().height ? -1 : mouseY + getMouseYOffset(),
                    partialTicks);

            matrices.pop();

            fillGradient(matrices, 0, -3, width, 5, 0xFF000000, 0);
            fillGradient(matrices, 0, height - 6, width, height + 3, 0, 0xEE000000);

            matrices.pop();
        });

    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            super.mouseDragged(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), 0, 0, 0);
        }
    }

    public int getMouseYOffset() {
        return -margin.top - padding.top + scrollbar.getVerticalScrollAmount();
    }

    public int getMouseXOffset() {
        return -margin.left - padding.left + scrollbar.getHorizontalScrollAmount();
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
        scrollbar.scrollBy((float)amount * 2);

        return isMouseOver(mouseX, mouseY) && super.mouseScrolled(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), amount);
    }

    @Override
    public void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> tooltip, int mouseX, int mouseY) {
        matrices.push();

        matrices.translate(-margin.left, -margin.top, 0);
        matrices.translate(-padding.left, 0, 0);
        matrices.translate(0, scrollbar.getVerticalScrollAmount() - padding.top, 0);

        client.currentScreen.renderOrderedTooltip(matrices, tooltip, mouseX - getMouseXOffset(), mouseY - getMouseYOffset());

        matrices.pop();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return getBounds().contains(mouseX, mouseY);
    }

    @Override
    public Padding getContentPadding() {
        return padding;
    }

    @Override
    public void setBounds(Bounds bounds) {
        margin.top = bounds.top;
        margin.left = bounds.left;
    }

    @Override
    public List<Element> getChildElements() {
        return children();
    }
}
