package com.minelittlepony.common.client.gui.packing;

import com.minelittlepony.common.client.gui.dimension.Bounds;

public class GridPacker implements IPacker {

    private final Bounds screen = new Bounds(0, 0, 0, 0);
    private final Bounds bounds = new Bounds(0, 0, 0, 0);

    private int itemSpacing;

    private int alignmentOffset;

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
        alignmentOffset = computeAlignmentOffset();
        bounds.top = 0;
        bounds.left = -(bounds.width + itemSpacing) + alignmentOffset;
    }

    @Override
    public Bounds next() {
        bounds.left += bounds.width + itemSpacing;

        if (bounds.left + bounds.width - alignmentOffset >= screen.width - 30) {
            bounds.left = alignmentOffset;
            bounds.top += bounds.height + itemSpacing;
        }

        return bounds;
    }

    protected int computeAlignmentOffset() {
        float maxWidth = screen.width - 30;
        float colWidth = bounds.width + itemSpacing;
        float maxCols = (float) Math.floor(maxWidth / colWidth);

        float computedWidth = (bounds.width * maxCols) + (itemSpacing * (maxCols - 1));

        return (int)Math.floor(maxWidth/2 - computedWidth/2);
    }

}
