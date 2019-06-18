package com.example.canvas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.canvas.adaptor.StickerAdaptor;
import com.example.canvas.db.Canvas;
import com.example.canvas.db.CanvasDataBase;
import com.example.canvas.db.CanvasViewModel;
import com.example.canvas.view.PaintView;
import com.ramotion.fluidslider.FluidSlider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import top.defaults.colorpicker.ColorPickerView;

public class MainActivity extends AppCompatActivity {

    CanvasDataBase canvasDataBase;
    @BindView(R.id.canvasRv)
    RecyclerView canvasRv;
    @BindView(R.id.fsTextView)
    TextView tapTv;
    StickerAdaptor stickerAdaptor;
    ArrayList<Canvas> canvasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        canvasDataBase = CanvasDataBase.getInstance(getApplicationContext());
        CanvasViewModel canvasViewModel;
        canvasViewModel = ViewModelProviders.of(this).get(CanvasViewModel.class);
        canvasViewModel.getCanvasLiveData().observe(this, new Observer<List<Canvas>>() {
            @Override
            public void onChanged(List<Canvas> canvas) {
                if (canvas.size() != 0)
                    tapTv.setVisibility(View.GONE);
                canvasList = (ArrayList<Canvas>) canvas;
                stickerAdaptor = new StickerAdaptor(0,null,canvasList);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(),2);
                canvasRv.setLayoutManager(gridLayoutManager);
                canvasRv.setAdapter(stickerAdaptor);
            }
        });
    }

    @OnClick(R.id.fabBtn)
    public void startPaintActivity() {
        Intent intent = new Intent(this,PaintActivity.class);
        startActivity(intent);
    }
}
