package com.minelittlepony.common.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.client.util.math.AffineTransformation;
import net.minecraft.client.util.math.MatrixStack;

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

        Matrix4f matrix = AffineTransformation.identity().getMatrix();

        for (String line : getFont().wrapStringToWidthAsList(text, maxWidth)) {
            float left = x;
            if (getFont().isRightToLeft()) {
                left += maxWidth - getFont().getStringWidth(getFont().mirror(line));
            }

            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            getFont().draw(line, left, y, color, false, matrix, immediate, true, 0, 0xF000F0);
            immediate.draw();

            y += 9;
        }
    }
}
