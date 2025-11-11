package com.minelittlepony.common.client.gui.packing;

import com.minelittlepony.common.client.gui.dimension.Bounds;

public class ListPacker implements IPacker {

    private final Bounds bounds = new Bounds(0, 0, 0, 0);

    private int itemSpacing;
    private int xOffset;
    private int yOffset;

    public ListPacker setOffset(int x, int y) {
        xOffset = x;
        yOffset = y;
        return this;
    }

    public ListPacker setListWidth(int width) {
        bounds.width = width;

        return this;
    }

    public ListPacker setItemSpacing(int spacing) {
        itemSpacing = spacing;

        return this;
    }

    public ListPacker setItemHeight(int height) {
        bounds.height = height;

        return this;
    }

    @Override
    public void start() {
        bounds.top = -(bounds.height + itemSpacing) + yOffset;
        bounds.left = xOffset;
    }

    @Override
    public Bounds next() {
        bounds.top += bounds.height + itemSpacing;

        return bounds;
    }

}
