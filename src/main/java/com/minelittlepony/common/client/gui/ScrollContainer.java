package com.minelittlepony.common.client.gui;

import java.util.List;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.minelittlepony.common.client.gui.element.Scrollbar;
import com.minelittlepony.common.util.render.ClippingSpace;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.network.chat.TextComponent;

public class ScrollContainer extends GameGui implements IBounded {

    protected final Scrollbar scrollbar = new Scrollbar();

    public final Padding margin = new Padding(0, 0, 0, 0);
    public final Padding padding = new Padding(0, 0, 0, 0);

    public ScrollContainer() {
        super(new TextComponent(""));

        init(MinecraftClient.getInstance(), 0, 0);
    }

    @Override
    public void init() {
        width = getBounds().width = minecraft.window.getScaledWidth() - margin.left - margin.right;
        height = getBounds().height = minecraft.window.getScaledHeight() - margin.top - margin.bottom;
        getBounds().top = margin.top;
        getBounds().left = margin.left;

        Bounds content = getContentBounds();

        children.add(scrollbar);
        scrollbar.reposition(
                content.left + content.width,
                0,
                getBounds().height,
                content.height);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        ClippingSpace.renderClipped(margin.left, margin.top, getBounds().width, getBounds().height, () -> {
            int scroll = scrollbar.getScrollAmount();

            GlStateManager.pushMatrix();
            GlStateManager.translated(margin.left, margin.top, 0);

            fill(0, 0, width, height, 0x66000000);

            GlStateManager.pushMatrix();
            GlStateManager.translated(padding.left, 0, 0);

            scrollbar.render(
                    mouseX - margin.left,
                    mouseY - margin.top,
                    partialTicks);

            GlStateManager.translated(0, -scroll + padding.top, 0);

            super.render(
                    mouseX + getMouseXOffset(),
                    mouseY + getMouseYOffset(),
                    partialTicks);
            GlStateManager.popMatrix();

            fillGradient(0, -3, width, 5, 0xFF000000, 0);
            fillGradient(0, height - 6, width, height + 3, 0, 0xEE000000);

            GlStateManager.popMatrix();
        });
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            super.mouseDragged(mouseX + getMouseXOffset(), mouseY + getMouseYOffset(), 0, 0, 0);
        }
    }

    public int getMouseYOffset() {
        return -margin.top - padding.top + scrollbar.getScrollAmount();
    }

    public int getMouseXOffset() {
        return -margin.left - padding.left;
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
    public void renderTooltip(List<String> tooltip, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();

        GlStateManager.translated(-margin.left, -margin.top, 0);
        GlStateManager.translated(-padding.left, 0, 0);
        GlStateManager.translated(0, scrollbar.getScrollAmount() - padding.top, 0);

        minecraft.currentScreen.renderTooltip(tooltip, mouseX - getMouseXOffset(), mouseY - getMouseYOffset());

        GlStateManager.popMatrix();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return getBounds().contains(mouseX - margin.left, mouseY - margin.top);
    }

    public Bounds getContentBounds() {
        Bounds bounds = Bounds.empty();

        for (Element element : children) {
            if (element instanceof IBounded) {
                bounds = bounds.add(((IBounded)element).getBounds());
            }
        }

        return bounds.add(padding);
    }
}
