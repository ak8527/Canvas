package com.example.canvas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.canvas.view.PaintView;
import com.ramotion.fluidslider.FluidSlider;

import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import top.defaults.colorpicker.ColorPickerView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.paintView)
    PaintView paintView;

    DisplayMetrics displayMetrics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        PaintView.BRUSH_SIZE = 20;
        paintView.init(displayMetrics);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.paint_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
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
        View colorPicker = View.inflate(this,R.layout.color_picker_dialog,null);
        colorPickerView = colorPicker.findViewById(R.id.colorPickerView);
        fluidSlider = colorPicker.findViewById(R.id.fluidSlider);
        fluidSlider.setBubbleText(String.valueOf(brushWidth));
        fluidSlider.setPosition((float) brushWidth/100);


        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("Color Picker")
                .setView(colorPicker)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PaintView.DEFAULT_COLOR = pickColor;
                        PaintView.BRUSH_SIZE = brushWidth;
                        paintView.init(displayMetrics);
                    }
                })
                .show();


        colorPickerView.subscribe((color, fromUser, shouldPropagate) ->  {
            pickColor = color;
        });

        fluidSlider.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(pos * 100) );
            brushWidth = Integer.parseInt(value);
            fluidSlider.setBubbleText(value);
            return Unit.INSTANCE;
        }
        );





    }
}
