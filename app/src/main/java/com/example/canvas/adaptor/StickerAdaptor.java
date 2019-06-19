package com.example.canvas.adaptor;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.canvas.R;
import com.example.canvas.interfaces.Stickerface;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickerAdaptor extends RecyclerView.Adapter<StickerAdaptor.StickerHolder> {

    private ArrayList<Integer> stickerArrayList;
    private Context context;
    private Stickerface stickerface;

    public StickerAdaptor(ArrayList<Integer> stickerArrayList,Stickerface stickerface) {
        this.stickerArrayList = stickerArrayList;
        this.stickerface = stickerface;
    }


    @NonNull
    @Override
    public StickerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_list_item,parent,false);
        return new StickerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerHolder holder, int position) {
            Glide.with(context)
                    .load(stickerArrayList.get(position))
                    .placeholder(R.drawable.d1)
                    .into(holder.stickerIv);

            holder.itemView.setOnClickListener(view -> {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),stickerArrayList.get(position));
                stickerface.getSticker(bitmap);
            });

    }

    @Override
    public int getItemCount() {
            return stickerArrayList.size();

    }

    class StickerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stickerImageView)
        ImageView stickerIv;

        StickerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
