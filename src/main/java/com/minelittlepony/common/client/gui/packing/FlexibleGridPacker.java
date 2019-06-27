package com.minelittlepony.common.client.gui.packing;

import net.minecraft.util.math.MathHelper;

public class FlexibleGridPacker extends GridPacker {

    private int maxItemWidth;
    private int minItemWidth;

    private int columnCount;

    @Override
    public GridPacker setListWidth(int width) {
        super.setListWidth(width);

        if (columnCount > 0) {
            int max = maxItemWidth > 0 ? Math.min(width, maxItemWidth) : width;
            int min = Math.min(width, minItemWidth);
            setItemWidth(MathHelper.clamp(width / columnCount, Math.min(max, min), Math.max(max, min)));
        }

        return this;
    }

    public FlexibleGridPacker setColumnCount(int columns) {
        columnCount = columns;
        return this;
    }

    public FlexibleGridPacker setMaxItemWidth(int width) {
        maxItemWidth = width;
        return this;
    }

    public FlexibleGridPacker setMinItemWidth(int width) {
        minItemWidth = Math.max(0, width);
        return this;
    }
}
