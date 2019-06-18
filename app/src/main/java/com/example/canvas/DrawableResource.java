package com.example.canvas;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.util.ArrayList;

public class DrawableResource {
    Context context;

    public DrawableResource(Context context) {
        this.context = context;
    };

    public  ArrayList<Integer> getAllDrawableResource() {
       ArrayList<Integer>  stickerList = new ArrayList<>();
       stickerList.add(R.drawable.d1);
       stickerList.add(R.drawable.d2);
       stickerList.add(R.drawable.d3);
       stickerList.add(R.drawable.d4);
       stickerList.add(R.drawable.d5);
       stickerList.add(R.drawable.d6);
       stickerList.add(R.drawable.d7);
       stickerList.add(R.drawable.d8);
       stickerList.add(R.drawable.d9);
       stickerList.add(R.drawable.d10);
       stickerList.add(R.drawable.d11);
       stickerList.add(R.drawable.d12);
       stickerList.add(R.drawable.d13);
       stickerList.add(R.drawable.d14);
       stickerList.add(R.drawable.d15);
       stickerList.add(R.drawable.d16);
       stickerList.add(R.drawable.d17);
       stickerList.add(R.drawable.d18);
       stickerList.add(R.drawable.d19);
       stickerList.add(R.drawable.d20);

       return stickerList;

    }
}
