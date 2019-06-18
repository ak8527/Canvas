package com.example.canvas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.canvas.adaptor.StickerAdaptor;
import com.example.canvas.db.CanvasDataBase;
import com.example.canvas.view.PaintView;
import com.ramotion.fluidslider.FluidSlider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import top.defaults.colorpicker.ColorPickerView;

public class PaintActivity extends AppCompatActivity {

    @BindView(R.id.paintView)
    PaintView paintView;

    DisplayMetrics displayMetrics;

    CanvasDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        ButterKnife.bind(this);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        PaintView.BRUSH_SIZE = 20;
        paintView.init(displayMetrics);
        db = CanvasDataBase.getInstance(getApplicationContext());
        db.canvasDao().getAllCanvas().observe(this, canvas -> Log.e("MainActivity", "onChanged: " + canvas.size() ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.paint_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal:
                paintView.normal();
                return true;
            case R.id.emboss:
                paintView.emboss();
                return true;
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.clear:
                paintView.clear();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.eraserIv)
    public void eraserPick() {
        PaintView.DEFAULT_COLOR = Color.WHITE;
        PaintView.BRUSH_SIZE = 20;
        paintView.init(displayMetrics);
    }

    @OnClick(R.id.paintIv)
    public void paint() {
        colorPickerDialog();
    }


    static int pickColor;
    static int brushWidth;

    public void colorPickerDialog() {
        ColorPickerView colorPickerView;
        FluidSlider fluidSlider;
        View colorPicker = View.inflate(this, R.layout.color_picker_dialog, null);
        colorPickerView = colorPicker.findViewById(R.id.colorPickerView);
        fluidSlider = colorPicker.findViewById(R.id.fluidSlider);
        fluidSlider.setBubbleText(String.valueOf(brushWidth));
        fluidSlider.setPosition((float) brushWidth / 100);


        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("Color Picker")
                .setView(colorPicker)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    PaintView.DEFAULT_COLOR = pickColor;
                    PaintView.BRUSH_SIZE = brushWidth;
                    paintView.init(displayMetrics);
                })
                .show();


        colorPickerView.subscribe((color, fromUser, shouldPropagate) -> {
            pickColor = color;
        });

        fluidSlider.setPositionListener(pos -> {
                    final String value = String.valueOf((int) (pos * 100));
                    brushWidth = Integer.parseInt(value);
                    fluidSlider.setBubbleText(value);
                    return Unit.INSTANCE;
                }
        );

    }

    FileOutputStream fos = null;
    FileOutputStream fos1 = null;
    File file = null;

    @OnClick(R.id.saveIv)
    public void saveCanvas() {

        try {
            file = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS  + "/" + System.currentTimeMillis() + "canvas.jpeg");
            fos = new FileOutputStream(file.getAbsolutePath());
            Bitmap bitmap = Bitmap.createBitmap(paintView.getWidth(), paintView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            int size;

          Runnable runnable = () -> db.canvasDao().insert(new com.example.canvas.db.Canvas(file.getAbsolutePath()));
          new Thread(runnable).start();


            paintView.draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.shareIv)
    public void share() {
        saveCanvas();
        if (file != null)
            shareMedia(file);
    }

    private void shareMedia(File file) {

        String intentType = "jpeg" + "/*";

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(this, "com.example.canvas.fileprovider", file);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.setType(intentType);
        startActivity(sharingIntent);
    }


    @OnClick(R.id.stickerIv)
    public void pickSticker() {
        View stickerPv = View.inflate(this,R.layout.sticker_dialog,null);
        AlertDialog builder = new AlertDialog
                .Builder(this)
                .setTitle("Stickers")
                .setView(stickerPv)
                .show();

        RecyclerView stickerRv = stickerPv.findViewById(R.id.stickerRv);

        DrawableResource drawableResource = new DrawableResource(this);
        StickerAdaptor stickerAdaptor = new StickerAdaptor(0,drawableResource.getAllDrawableResource());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        stickerRv.setLayoutManager(gridLayoutManager);
        stickerRv.setAdapter(stickerAdaptor);

    }


}
