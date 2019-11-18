package com.minelittlepony.common.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;

public interface ITextContext {

    default TextRenderer getFont() {
        return MinecraftClient.getInstance().textRenderer;
    }

    default void drawLabel(String text, int x, int y, int color, double zIndex) {
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0, 0, zIndex);
        Matrix4f matrix4f = matrixStack.peek().getModel();

        getFont().draw(text, x, y, color, true, matrix4f, immediate, true, 0, 0xF000F0);
        immediate.draw();
    }

    default void drawCenteredLabel(String text, int x, int y, int color, double zIndex) {
        int width = getFont().getStringWidth(text);

        drawLabel(text, x - width/2, y, color, zIndex);
    }
}
