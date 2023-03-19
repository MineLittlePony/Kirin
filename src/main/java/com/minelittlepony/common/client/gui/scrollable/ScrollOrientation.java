package com.minelittlepony.common.client.gui.scrollable;

import com.minelittlepony.common.client.gui.dimension.Bounds;

public enum ScrollOrientation {
    VERTICAL,
    HORIZONTAL;

    public int getLength(Bounds bounds) {
        return this == VERTICAL ? bounds.height : bounds.width;
    }

    public int getWidth(Bounds bounds) {
        return this == VERTICAL ? bounds.width : bounds.height;
    }

    public double pick(double x, double y) {
        return this == VERTICAL ? y : x;
    }

    public int pick(int x, int y) {
        return this == VERTICAL ? y : x;
    }
}
