package com.minelittlepony.common.client.gui.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/**
 * An element's external padding.
 *
 * @author     Sollace
 */
public class Padding {
    public static final Codec<Padding> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("top").forGetter(b -> b.top),
            Codec.INT.fieldOf("left").forGetter(b -> b.left),
            Codec.INT.fieldOf("bottom").forGetter(b -> b.bottom),
            Codec.INT.fieldOf("right").forGetter(b -> b.right)
    ).apply(i, Padding::new));
    public static final PacketCodec<ByteBuf, Padding> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, b -> b.top,
            PacketCodecs.INTEGER, b -> b.left,
            PacketCodecs.INTEGER, b -> b.bottom,
            PacketCodecs.INTEGER, b -> b.right,
            Padding::new
    );

    public int top;
    public int left;

    public int bottom;
    public int right;

    public Padding(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     * Sets all sides to the given value.
     */
    public void setAll(int padding) {
        setVertical(padding);
        setHorizontal(padding);
    }

    /**
     * Sets the top and bottom padding to the given value.
     */
    public void setVertical(int padding) {
        top = padding;
        bottom = padding;
    }

    /**
     * Sets the left and right padding to the given value.
     */
    public void setHorizontal(int padding) {
        left = padding;
        right = padding;
    }
}
