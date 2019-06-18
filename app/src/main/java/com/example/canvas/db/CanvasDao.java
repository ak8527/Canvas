package com.example.canvas.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface CanvasDao {

    @Update
    void update(Canvas canvas);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Canvas canvas);

    @Delete
    void delete(Canvas canvas);

    @Query("SELECT * FROM Canvas")
    LiveData<List<Canvas>> getAllCanvas();
}
