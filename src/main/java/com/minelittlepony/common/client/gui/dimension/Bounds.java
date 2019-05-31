package com.minelittlepony.common.client.gui.dimension;

public class Bounds {

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
    
    public boolean isEmpty() {
        return width <= 0 || height <= 0;
    }
    
    public boolean contains(double x, double y) {
        return !isEmpty() && x >= left && x <= (left + width) && y >= top && y <= (top + height);
    }
    
    public Bounds add(Padding other) {
        return new Bounds(
            top - other.top,
            left - other.left,
            width + other.left + other.right,
            height + other.top + other.bottom
        );
    }
    
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
}
