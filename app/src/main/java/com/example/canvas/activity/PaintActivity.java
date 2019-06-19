package com.example.canvas.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvas.DrawableResource;
import com.example.canvas.R;
import com.example.canvas.adaptor.StickerAdaptor;
import com.example.canvas.db.CanvasDataBase;
import com.example.canvas.interfaces.Stickerface;
import com.example.canvas.view.PaintView;
import com.ramotion.fluidslider.FluidSlider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import top.defaults.colorpicker.ColorPickerView;

public class PaintActivity extends AppCompatActivity implements Stickerface {

    @BindView(R.id.paintView)
    PaintView paintView;

    private DisplayMetrics displayMetrics;

    private CanvasDataBase db;
    private StickerAdaptor stickerAdaptor;
    private GridLayoutManager gridLayoutManager;

    private static int pickColor = 0;
    private static int brushWidth = 20;
    private File file = null;

    private static final int MY_STORAGE_REQUEST_CODE = 1111;
    private FluidSlider fluidSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        PaintView.BRUSH_SIZE = 20;
        paintView.init(displayMetrics);
        db = CanvasDataBase.getInstance(getApplicationContext());
        DrawableResource drawableResource = new DrawableResource();
        stickerAdaptor = new StickerAdaptor( drawableResource.getAllDrawableResource(),this);
        gridLayoutManager = new GridLayoutManager(this, 2);
    }

    @OnClick(R.id.blurIv)
    public void blurBrush() {
        paintView.blur();
        paintView.init(displayMetrics);
    }

    @OnClick(R.id.clearIv)
    public void clearCanvas() {

        paintView.clear();
    }

    @OnClick(R.id.eraserIv)
    public void eraserPick() {
        PaintView.DEFAULT_COLOR = Color.WHITE;
        if (brushWidth == 20)
        PaintView.BRUSH_SIZE = 20;
        else
        PaintView.BRUSH_SIZE = brushWidth;
        paintView.normal();
        paintView.init(displayMetrics);
    }

    @OnClick(R.id.normalIv)
    public void normalColor() {
        if (pickColor == 0)
        PaintView.DEFAULT_COLOR = Color.BLUE;
        else
        PaintView.DEFAULT_COLOR = pickColor;
        paintView.normal();
        paintView.init(displayMetrics);
    }

    @OnClick(R.id.paintIv)
    public void paint() {
        colorPickerDialog();
        paintView.init(displayMetrics);
    }




    private void colorPickerDialog() {
        ColorPickerView colorPickerView;
        View colorPicker = View.inflate(this, R.layout.color_picker_dialog, null);
        colorPickerView = colorPicker.findViewById(R.id.colorPickerView);

        if (pickColor != 0)
            colorPickerView.setInitialColor(pickColor);


        new AlertDialog.Builder(this)
                .setTitle("Color Picker")
                .setView(colorPicker)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    PaintView.DEFAULT_COLOR = pickColor;
                    paintView.init(displayMetrics);
                })
                .show();


        colorPickerView.subscribe((color, fromUser, shouldPropagate) -> pickColor = color);

    }

    @OnClick(R.id.brushWidthIv)
    public void setBrushWidth() {
        setBrushSize();
    }

    private void setBrushSize() {
        View view = View.inflate(this, R.layout.brush_width_dialog, null);
        fluidSlider = view.findViewById(R.id.brushWidthSlider);
//        if (brushWidth == 20) {
//            fluidSlider.setPosition(0.2f);
//            fluidSlider.setBubbleText(String.valueOf(20));
//        }
//        else {
            fluidSlider.setPosition((float) brushWidth / 100);
            fluidSlider.setBubbleText(String.valueOf(brushWidth));
//        }

        Log.e("PaintActivity", "setBrushSize: " + brushWidth);

        new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Brush Width")
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    PaintView.BRUSH_SIZE = brushWidth;
                    paintView.init(displayMetrics);
                })
                .show();
        fluidSlider.setPositionListener(pos -> {
                    final String value = String.valueOf((int) (pos * 100));
                    brushWidth = Integer.parseInt(value);
                    fluidSlider.setBubbleText(value);
                    return Unit.INSTANCE;
                }
        );


    }


    @OnClick(R.id.saveIv)
    public void saveCanvas() {

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveToMemory();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
            }
        }
    }

    private void saveToMemory() {

        try {
            file = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + System.currentTimeMillis() + "canvas.jpeg");
            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
            Bitmap bitmap = Bitmap.createBitmap(paintView.getWidth(), paintView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            Runnable runnable = () -> {
                db.canvasDao().insert(new com.example.canvas.db.Canvas(file.getAbsolutePath()));
                runOnUiThread(() -> Toast.makeText(getBaseContext(),"Image Saved",Toast.LENGTH_SHORT).show());

            };
            new Thread(runnable).start();

            paintView.draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.e("PaintActivity", "saveToMemory: " + brushWidth);
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
        View stickerPv = View.inflate(this, R.layout.sticker_dialog, null);
        new AlertDialog
                .Builder(this)
                .setTitle("Stickers")
                .setView(stickerPv)
                .show();

        RecyclerView stickerRv;
        stickerRv = stickerPv.findViewById(R.id.stickerRv);
        stickerRv.setLayoutManager(gridLayoutManager);
        stickerRv.setAdapter(stickerAdaptor);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToMemory();
            }
        }
    }

    @Override
    public void getSticker(Bitmap bitmap) {
        if (bitmap != null) {
            Log.e("PaintActivity", "getSticker: ");
//            paintView.setStickerBitmap(bitmap);
//            paintView.init(displayMetrics);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        PaintView.BRUSH_SIZE = brushWidth;
        paintView.init(displayMetrics);
    }
}
