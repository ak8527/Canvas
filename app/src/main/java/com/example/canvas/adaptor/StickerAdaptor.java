package com.example.canvas.adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvas.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickerAdaptor extends RecyclerView.Adapter<StickerAdaptor.StickerHolder> {

    private ArrayList<Integer> stickerArrayList;
    private Context context;
    private int id;

    public StickerAdaptor(int id,  ArrayList<Integer> stickerArrayList) {
        this.id = id;
        this.stickerArrayList = stickerArrayList;
    }

    @NonNull
    @Override
    public StickerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_list_item,null);
        return new StickerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerHolder holder, int position) {
        holder.stickerIv.setImageResource(stickerArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return stickerArrayList.size();
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
