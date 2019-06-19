package com.example.canvas.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.canvas.R;
import com.example.canvas.activity.ImageActivity;
import com.example.canvas.db.Canvas;
import com.example.canvas.db.CanvasDataBase;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CanvasAdaptor extends RecyclerView.Adapter<CanvasAdaptor.CanvasHolder> {

    private Context context;
    private ArrayList<Canvas> canvasArrayList;
    public CanvasAdaptor(ArrayList<Canvas> canvasArrayList) {
        this.canvasArrayList = canvasArrayList;
    }


    @NonNull
    @Override
    public CanvasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.canvas_list_item,parent,false);
        return new CanvasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CanvasHolder holder, int position) {
        Glide.with(context)
                .load(new File(canvasArrayList.get(position).getCanvasName()))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.d1)
                .into(holder.canvasIv);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("path", canvasArrayList.get(position).getCanvasName());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete?")
                    .setPositiveButton("CONFIRM", (dialogInterface, i) -> deleteCanvas(canvasArrayList.get(position), position))
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> {

                    })
                    .setTitle("Delete")
                    .show();
            return false;
        });


    }


    private void deleteCanvas(Canvas canvas, int position) {

        new Thread(() -> CanvasDataBase.getInstance(context).canvasDao().delete(canvas)).start();
        canvasArrayList.remove(position);
        notifyDataSetChanged();
        Toast.makeText(context,"Item Deleted",Toast.LENGTH_SHORT).show();
    }


    public void dataSetChanged(ArrayList<Canvas> canvas) {
        canvasArrayList = canvas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return canvasArrayList.size();
    }

    class CanvasHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.canvasIv)
        ImageView canvasIv;
        public CanvasHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
