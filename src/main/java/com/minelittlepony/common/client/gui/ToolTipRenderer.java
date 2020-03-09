package com.minelittlepony.common.client.gui;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;

public class ToolTipRenderer extends DrawableHelper {

    private final Screen screen;

    public ToolTipRenderer(Screen parent) {
        screen = parent;
    }

    protected int getTextColor() {
        return 0xF000F0;
    }

    protected int getFill() {
        return 0xF0100010;
    }

    protected int getOutlineGradientTop() {
        return 0x505000FF;
    }

    protected int getOutlineGradientBottom() {
        return 0x5028007F;
    }

    public void render(List<String> text, int x, int y) {
        if (text.isEmpty()) {
            return;
        }

        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        RenderSystem.disableRescaleNormal();
        RenderSystem.disableDepthTest();

        int labelWidth = 0;

        for (String string : text) {
            labelWidth = Math.max(labelWidth, font.getStringWidth(string));
        }

        int left = x + 12;
        int top = y - 12;
        int labelHeight = 8;

        if (text.size() > 1) {
            labelHeight += 2 + (text.size() - 1) * 10;
        }

        if (left + labelWidth > screen.width) {
            left -= 28 + labelWidth;
        }

        if (top + labelHeight + 6 > screen.height) {
            top = screen.height - labelHeight - 6;
        }

        setZOffset(300);
        itemRenderer.zOffset = 300;

        int labelFill = getFill();
        fillGradient(left - 3,              top - 4,               left + labelWidth + 3, top - 3,               labelFill, labelFill);
        fillGradient(left - 3,              top + labelHeight + 3, left + labelWidth + 3, top + labelHeight + 4, labelFill, labelFill);
        fillGradient(left - 3,              top - 3,               left + labelWidth + 3, top + labelHeight + 3, labelFill, labelFill);
        fillGradient(left - 4,              top - 3,               left - 3,              top + labelHeight + 3, labelFill, labelFill);
        fillGradient(left + labelWidth + 3, top - 3,               left + labelWidth + 4, top + labelHeight + 3, labelFill, labelFill);

        int borderGradientTop = getOutlineGradientTop();
        int borderGradientBot = getOutlineGradientBottom();
        fillGradient(left - 3,              top - 3 + 1,           left - 3 + 1,          top + labelHeight + 3 - 1, borderGradientTop, borderGradientBot);
        fillGradient(left + labelWidth + 2, top - 3 + 1,           left + labelWidth + 3, top + labelHeight + 3 - 1, borderGradientTop, borderGradientBot);
        fillGradient(left - 3,              top - 3,               left + labelWidth + 3, top - 3 + 1,               borderGradientTop, borderGradientTop);
        fillGradient(left - 3,              top + labelHeight + 2, left + labelWidth + 3, top + labelHeight + 3,     borderGradientBot, borderGradientBot);

        MatrixStack stack = new MatrixStack();
        Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

        stack.translate(0, 0, itemRenderer.zOffset);
        Matrix4f matrix = stack.peek().getModel();

        int color = getTextColor();

        for(int r = 0; r < text.size(); ++r) {
            String line = text.get(r);
            if (line != null) {
                font.draw(line, left, top, -1, true, matrix, immediate, true, 0, color);
            }

            if (r == 0) {
                top += 2;
            }

            top += 10;
        }

        immediate.draw();

        setZOffset(0);
        itemRenderer.zOffset = 0;
        RenderSystem.enableDepthTest();
        RenderSystem.enableRescaleNormal();
    }
}
