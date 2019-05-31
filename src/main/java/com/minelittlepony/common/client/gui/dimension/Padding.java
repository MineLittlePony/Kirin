package com.minelittlepony.common.client.gui.dimension;

public class Padding {

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
    
    public void setAll(int padding) {
        setVertical(padding);
        setHorizontal(padding);
    }
    
    public void setVertical(int padding) {
        top = padding;
        bottom = padding;
    }
    
    public void setHorizontal(int padding) {
        left = padding;
        right = padding;
    }
}
