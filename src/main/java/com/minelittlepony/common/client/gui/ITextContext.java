package com.minelittlepony.common.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rotation3;

public interface ITextContext {

    default TextRenderer getFont() {
        return MinecraftClient.getInstance().textRenderer;
    }

    default void drawLabel(String text, int x, int y, int color, double zIndex) {
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        MatrixStack stack = new MatrixStack();
        stack.translate(0, 0, zIndex);
        Matrix4f matrix = stack.peek().getModel();

        getFont().draw(text, x, y, color, true, matrix, immediate, true, 0, 0xF000F0);
        immediate.draw();
    }

    default void drawCenteredLabel(String text, int x, int y, int color, double zIndex) {
        int width = getFont().getStringWidth(text);

        drawLabel(text, x - width/2, y, color, zIndex);
    }

    default void drawTextBlock(String text, int x, int y, int maxWidth, int color) {
        while(text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }

        Matrix4f matrix = Rotation3.identity().getMatrix();

        for (String string : getFont().wrapStringToWidthAsList(text, maxWidth)) {
            float left = x;
            if (getFont().isRightToLeft()) {
                left += maxWidth - getFont().getStringWidth(getFont().mirror(string));
            }

            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            getFont().draw(text, left, y, color, true, matrix, immediate, true, 0, 0xF000F0);
            immediate.draw();

            y += 9;
        }
    }
}
