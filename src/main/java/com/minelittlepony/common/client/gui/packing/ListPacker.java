package com.minelittlepony.common.client.gui.packing;

import com.minelittlepony.common.client.gui.dimension.Bounds;

public class ListPacker implements IPacker {

    private final Bounds bounds = new Bounds(0, 0, 0, 0);

    private int itemSpacing;

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
        bounds.top = -(bounds.height + itemSpacing);
    }

    @Override
    public Bounds next() {
        bounds.top += bounds.height + itemSpacing;

        return bounds;
    }

}
