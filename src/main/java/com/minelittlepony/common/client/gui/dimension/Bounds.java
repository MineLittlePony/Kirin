package com.minelittlepony.common.client.gui.dimension;

import org.joml.Matrix3x2fStack;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Represents the bounding rectangle of an element on the screen.
 *
 * @author     Sollace
 */
public class Bounds {
    public static final Codec<Bounds> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("top").forGetter(b -> b.top),
            Codec.INT.fieldOf("left").forGetter(b -> b.left),
            Codec.INT.fieldOf("width").forGetter(b -> b.width),
            Codec.INT.fieldOf("height").forGetter(b -> b.height)
    ).apply(i, Bounds::new));
    public static final StreamCodec<ByteBuf, Bounds> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, b -> b.top,
            ByteBufCodecs.INT, b -> b.left,
            ByteBufCodecs.INT, b -> b.width,
            ByteBufCodecs.INT, b -> b.height,
            Bounds::new
    );

    public int top;
    public int left;

    public int width;
    public int height;

    public static Bounds empty() {
        return new Bounds(0, 0, 0, 0);
    }

    public Bounds(int top, int left, int width, int height) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
    }

    public int right() {
        return left + width;
    }

    public int bottom() {
        return top + height;
    }

    public boolean isEmpty() {
        return width <= 0 || height <= 0;
    }

    /**
     * Determines whether the given point is somewhere in this bounding rectangle.
     *
     * @param x The X position
     * @param y The Y Position
     * @return True if the point overlaps or touches the edge of this rectangle
     */
    public boolean contains(double x, double y) {
        return !isEmpty() && containsX(x) && containsY(y);
    }

    /**
     * Determines whether the given point is somewhere in this bounding rectangle along the X axis.
     *
     * @param x The X position
     * @return True if the point overlaps or touches the edge of this rectangle
     */
    public boolean containsX(double x) {
        return !isEmpty() && x >= left && x <= (left + width);
    }

    /**
     * Determines whether the given point is somewhere in this bounding rectangle along the Y axis.
     *
     * @param y The Y position
     * @return True if the point overlaps or touches the edge of this rectangle
     */
    public boolean containsY(double y) {
        return !isEmpty() && y >= top && y <= (top + height);
    }

    /**
     * Computes a new bounds expanded on all sides by the given padding
     *
     * @param other The padding to add to these bounds.
     * @return A new bounds with the computed dimensions.
     */
    public Bounds add(Padding other) {
        return new Bounds(
            top - other.top,
            left - other.left,
            width + other.left + other.right,
            height + other.top + other.bottom
        );
    }

    /**
     * Computes a new bounds with the same dimensions as this one,
     * but shifted by the given margin.
     *
     * @param other the margins to move this bounds by.
     * @return A new bounds with the computed dimensions.
     */
    public Bounds offset(Padding other) {
        return new Bounds(
            top + other.top,
            left + other.left,
            width,
            height
        );
    }

    /**
     * Computes a new bounds that encompasses both this one and the one passed in.
     *
     * @param other The bounds to add to this one.
     * @return A new bounds with the computed dimensions.
     */
    public Bounds add(Bounds other) {

        if (other.isEmpty()) {
            return this;
        }

        if (isEmpty()) {
            return other;
        }

        int t = Math.min(top, other.top);
        int l = Math.min(left, other.left);

        int b = Math.max(top + height, other.top + other.height);
        int r = Math.max(left + width, other.left + other.width);

        int h = b - t;
        int w = r - l;

        return new Bounds(t, l, w, h);
    }

    /**
     * Copies the values of another bounds into this one.
     */
    public void copy(Bounds other) {
        top = other.top;
        left = other.left;
        width = other.width;
        height = other.height;
    }

    /**
     * Draws a coloured rectangle over the area covered by this bounds.
     * Useful for debugging.
     */
    @Environment(EnvType.CLIENT)
    public void draw(GuiGraphics context, int tint) {
        context.fill(left, top, left + width, top + height, tint);
    }

    /**
     * Applies this bounds' offset as a translation to the passed in matrix stack.
     */
    @Environment(EnvType.CLIENT)
    public void translate(PoseStack matrices) {
        matrices.translate(left, top, 0);
    }

    /**
     * Applies this bounds' offset as a translation to the passed in matrix stack.
     */
    @Environment(EnvType.CLIENT)
    public void translate(Matrix3x2fStack matrices) {
        matrices.translate(left, top);
    }

    /**
     * Sets this bounds as the current scissor bounds for drawing
     * @param context
     */
    @Environment(EnvType.CLIENT)
    public void scissor(GuiGraphics context) {
        context.enableScissor(left, top, right(), bottom());
    }

    @Environment(EnvType.CLIENT)
    public void debugMeasure(GuiGraphics context) {
        Window window = Minecraft.getInstance().getWindow();
        context.fill(left, -1000, left + 1, window.getGuiScaledHeight() * 9, 0xFFFFFFFF);
        context.fill(left + width, -1000, left + width + 1, window.getGuiScaledHeight() * 9, 0xFFFFFFFF);

        context.fill(-1000, top, window.getGuiScaledWidth(), top + 1, 0xFFFFFFFF);
        context.fill(-1000, top + height, window.getGuiScaledWidth(), top + height + 1, 0xFFFFFFFF);
    }

    protected boolean equals(Bounds o) {
        return this == o || o.top == top && o.left == left && o.width == width && o.height == height;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Bounds && equals((Bounds)o));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + left;
        result = prime * result + top;
        result = prime * result + width;
        return result;
    }
}
