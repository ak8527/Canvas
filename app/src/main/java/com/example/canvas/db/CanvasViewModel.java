package com.example.canvas.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CanvasViewModel extends AndroidViewModel {
    private final LiveData<List<Canvas>> canvasLiveData;
    private CanvasDataBase canvasDataBase;
    private CanvasDao canvasDao;

    public CanvasViewModel(Application context) {
        super(context);
        this.canvasDataBase = CanvasDataBase.getInstance(getApplication().getBaseContext());
        this.canvasDao = canvasDataBase.canvasDao();
        this.canvasLiveData = canvasDao.getAllCanvas();
    }

    public LiveData<List<Canvas>> getCanvasLiveData() {
        return canvasLiveData;
    }

}
