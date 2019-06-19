package com.example.canvas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.canvas.R;
import com.example.canvas.adaptor.CanvasAdaptor;
import com.example.canvas.adaptor.StickerAdaptor;

import com.example.canvas.db.Canvas;
import com.example.canvas.db.CanvasViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.canvasRv)
    RecyclerView canvasRv;
    @BindView(R.id.fsTextView)
    TextView tapTv;
    private CanvasAdaptor canvasAdaptor;
    private ArrayList<Canvas> canvasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 2);
        canvasRv.setLayoutManager(gridLayoutManager);
        canvasAdaptor = new CanvasAdaptor(canvasList);
        canvasRv.setAdapter(canvasAdaptor);
        CanvasViewModel canvasViewModel = ViewModelProviders.of(this).get(CanvasViewModel.class);
        canvasViewModel.getCanvasLiveData().observe(this, canvas -> {
            if (canvas.size() != 0)
                tapTv.setVisibility(View.GONE);
            else
                tapTv.setVisibility(View.VISIBLE);
            canvasList = (ArrayList<Canvas>) canvas;
            canvasAdaptor.dataSetChanged(canvasList);

        });

    }

    @OnClick(R.id.fabBtn)
    public void startPaintActivity() {
        Intent intent = new Intent(this, PaintActivity.class);
        startActivity(intent);
    }

}
