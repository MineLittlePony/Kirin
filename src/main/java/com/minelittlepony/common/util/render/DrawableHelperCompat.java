package com.minelittlepony.common.util.render;

import java.util.NoSuchElementException;

import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class DrawableHelperCompat {

    public static void drawNineSlicedTexture(DrawableHelper helper, MatrixStack matrices, int x, int y, int width, int height, int sideSliceWidth, int sideSliceHeight, int centerSliceWidth, int centerSliceHeight, int u, int v) {
        drawNineSlicedTexture(helper, matrices, x, y, width, height, sideSliceWidth, sideSliceHeight, sideSliceWidth, sideSliceHeight, centerSliceWidth, centerSliceHeight, u, v);
    }

    public static void drawNineSlicedTexture(DrawableHelper helper, MatrixStack matrices, int x, int y, int width, int height, int leftSliceWidth, int topSliceHeight, int rightSliceWidth, int bottomSliceHeight, int centerSliceWidth, int centerSliceHeight, int u, int v) {
        leftSliceWidth = Math.min(leftSliceWidth, width / 2);
        rightSliceWidth = Math.min(rightSliceWidth, width / 2);
        topSliceHeight = Math.min(topSliceHeight, height / 2);
        bottomSliceHeight = Math.min(bottomSliceHeight, height / 2);
        if (width == centerSliceWidth && height == centerSliceHeight) {
            helper.drawTexture(matrices, x, y, u, v, width, height);
            return;
        }
        if (height == centerSliceHeight) {
            helper.drawTexture(matrices, x, y, u, v, leftSliceWidth, height);
            drawRepeatingTexture(helper, matrices, x + leftSliceWidth, y, width - rightSliceWidth - leftSliceWidth, height, u + leftSliceWidth, v, centerSliceWidth - rightSliceWidth - leftSliceWidth, centerSliceHeight);
            helper.drawTexture(matrices, x + width - rightSliceWidth, y, u + centerSliceWidth - rightSliceWidth, v, rightSliceWidth, height);
            return;
        }
        if (width == centerSliceWidth) {
            helper.drawTexture(matrices, x, y, u, v, width, topSliceHeight);
            drawRepeatingTexture(helper, matrices, x, y + topSliceHeight, width, height - bottomSliceHeight - topSliceHeight, u, v + topSliceHeight, centerSliceWidth, centerSliceHeight - bottomSliceHeight - topSliceHeight);
            helper.drawTexture(matrices, x, y + height - bottomSliceHeight, u, v + centerSliceHeight - bottomSliceHeight, width, bottomSliceHeight);
            return;
        }
        helper.drawTexture(matrices, x, y, u, v, leftSliceWidth, topSliceHeight);
        drawRepeatingTexture(helper, matrices, x + leftSliceWidth, y, width - rightSliceWidth - leftSliceWidth, topSliceHeight, u + leftSliceWidth, v, centerSliceWidth - rightSliceWidth - leftSliceWidth, topSliceHeight);
        helper.drawTexture(matrices, x + width - rightSliceWidth, y, u + centerSliceWidth - rightSliceWidth, v, rightSliceWidth, topSliceHeight);
        helper.drawTexture(matrices, x, y + height - bottomSliceHeight, u, v + centerSliceHeight - bottomSliceHeight, leftSliceWidth, bottomSliceHeight);
        drawRepeatingTexture(helper, matrices, x + leftSliceWidth, y + height - bottomSliceHeight, width - rightSliceWidth - leftSliceWidth, bottomSliceHeight, u + leftSliceWidth, v + centerSliceHeight - bottomSliceHeight, centerSliceWidth - rightSliceWidth - leftSliceWidth, bottomSliceHeight);
        helper.drawTexture(matrices, x + width - rightSliceWidth, y + height - bottomSliceHeight, u + centerSliceWidth - rightSliceWidth, v + centerSliceHeight - bottomSliceHeight, rightSliceWidth, bottomSliceHeight);
        drawRepeatingTexture(helper, matrices, x, y + topSliceHeight, leftSliceWidth, height - bottomSliceHeight - topSliceHeight, u, v + topSliceHeight, leftSliceWidth, centerSliceHeight - bottomSliceHeight - topSliceHeight);
        drawRepeatingTexture(helper, matrices, x + leftSliceWidth, y + topSliceHeight, width - rightSliceWidth - leftSliceWidth, height - bottomSliceHeight - topSliceHeight, u + leftSliceWidth, v + topSliceHeight, centerSliceWidth - rightSliceWidth - leftSliceWidth, centerSliceHeight - bottomSliceHeight - topSliceHeight);
        drawRepeatingTexture(helper, matrices, x + width - rightSliceWidth, y + topSliceHeight, leftSliceWidth, height - bottomSliceHeight - topSliceHeight, u + centerSliceWidth - rightSliceWidth, v + topSliceHeight, rightSliceWidth, centerSliceHeight - bottomSliceHeight - topSliceHeight);
    }

    public static void drawRepeatingTexture(DrawableHelper helper, MatrixStack matrices, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight) {
        int i = x;
        IntIterator intIterator = createDivider(width, textureWidth);
        while (intIterator.hasNext()) {
            int j = intIterator.nextInt();
            int k = (textureWidth - j) / 2;
            int l = y;
            IntIterator intIterator2 = createDivider(height, textureHeight);
            while (intIterator2.hasNext()) {
                int m = intIterator2.nextInt();
                int n = (textureHeight - m) / 2;
                helper.drawTexture(matrices, i, l, u + k, v + n, j, m);
                l += m;
            }
            i += j;
        }
    }

    private static IntIterator createDivider(int sideLength, int textureSideLength) {
        int i = MathHelper.ceilDiv(sideLength, textureSideLength);
        return new Divider(sideLength, i);
    }

    private static final class Divider implements IntIterator {
        private final int divisor;
        private final int quotient;
        private final int mod;
        private int returnedCount;
        private int remainder;

        public Divider(int dividend, int divisor) {
            this.divisor = divisor;
            if (divisor > 0) {
                this.quotient = dividend / divisor;
                this.mod = dividend % divisor;
            } else {
                this.quotient = 0;
                this.mod = 0;
            }
        }

        @Override
        public boolean hasNext() {
            return this.returnedCount < this.divisor;
        }

        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            int i = this.quotient;
            this.remainder += this.mod;
            if (this.remainder >= this.divisor) {
                this.remainder -= this.divisor;
                ++i;
            }
            ++this.returnedCount;
            return i;
        }
    }
}
