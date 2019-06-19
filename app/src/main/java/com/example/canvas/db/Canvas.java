package com.example.canvas.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "canvas")
public class Canvas {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int uid;

    @ColumnInfo(name = "canvas_name")
    public String canvasName;

    public Canvas(String canvasName) {
        this.canvasName = canvasName;
    }

    public String getCanvasName() {
        return canvasName;
    }


}
