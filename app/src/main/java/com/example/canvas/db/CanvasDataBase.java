package com.example.canvas.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Canvas.class}, version = 1, exportSchema = false)
public abstract class CanvasDataBase extends RoomDatabase {
    private static CanvasDataBase instance;
    private static final Object LOCK = new Object();

    public static CanvasDataBase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(context.getApplicationContext(),CanvasDataBase.class,"canvas-db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }
    public abstract CanvasDao canvasDao();
}

