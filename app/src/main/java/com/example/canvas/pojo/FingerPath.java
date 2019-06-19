package com.example.canvas.pojo;


import android.graphics.Path;

public class FingerPath {

    public int color;
    public boolean blur;
    public int strokeWidth;
    public Path path;

    public FingerPath(int color, boolean blur, int strokeWidth, Path path) {
        this.color = color;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}
