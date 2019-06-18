package com.example.canvas.adaptor;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.canvas.R;
import com.example.canvas.db.Canvas;


import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickerAdaptor extends RecyclerView.Adapter<StickerAdaptor.StickerHolder> {

    private ArrayList<Integer> stickerArrayList;
    private ArrayList<Canvas> canvasArrayList;
    private Context context;
    private int id;

    public StickerAdaptor(int id,ArrayList<Integer> stickerArrayList, ArrayList<Canvas> canvasArrayList) {
        this.id = id;
        this.stickerArrayList = stickerArrayList;
        this.canvasArrayList = canvasArrayList;
    }



    @NonNull
    @Override
    public StickerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_list_item,null);
        return new StickerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerHolder holder, int position) {
        if (canvasArrayList == null) {
            Glide.with(context)
                    .load(stickerArrayList.get(position))
                    .error(R.drawable.d1)
                    .into(holder.stickerIv);
        }
        else {
            Glide.with(context)
                    .load(new File(canvasArrayList.get(position).getCanvasName()))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.d1)
                    .into(holder.stickerIv);

        }
    }

    @Override
    public int getItemCount() {
        if (canvasArrayList == null)
        return stickerArrayList.size();
        else
        return canvasArrayList.size();
    }

    class StickerHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.stickerImageView)
        ImageView stickerIv;
        StickerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
