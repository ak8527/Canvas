package com.example.canvas.db;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CanvasViewModel extends AndroidViewModel {
    private final LiveData<List<Canvas>> canvasLiveData;

    public CanvasViewModel(Application context) {
        super(context);
        canvasLiveData = CanvasDataBase.getInstance(getApplication().getBaseContext()).canvasDao().getAllCanvas();
    }

    public LiveData<List<Canvas>> getCanvasLiveData() {
        return canvasLiveData;
    }
}
