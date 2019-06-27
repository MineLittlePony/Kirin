package com.minelittlepony.common.client.gui.packing;

import com.minelittlepony.common.client.gui.dimension.Bounds;

public class GridPacker implements IPacker {

    private final Bounds screen = new Bounds(0, 0, 0, 0);
    private final Bounds bounds = new Bounds(0, 0, 0, 0);

    private int itemSpacing;

    public GridPacker setListWidth(int width) {
        screen.width = width;

        return this;
    }

    public GridPacker setItemSpacing(int spacing) {
        itemSpacing = spacing;

        return this;
    }

    public GridPacker setItemWidth(int width) {
        bounds.width = width;

        return this;
    }

    public GridPacker setItemHeight(int height) {
        bounds.height = height;

        return this;
    }

    @Override
    public void start() {
        bounds.top = -(bounds.height + itemSpacing);
        bounds.left = -(bounds.width + itemSpacing);
    }

    @Override
    public Bounds next() {
        bounds.left += bounds.width + itemSpacing;

        if (bounds.left + bounds.width > screen.width) {
            bounds.left = 0;
            bounds.top += bounds.height + itemSpacing;
        }

        return bounds;
    }

}
