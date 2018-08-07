package com.example.androidtrain.buildingconnect.volleyTrain;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by sx on 2018/8/7.
 */

/**
 * Use Demo
 *
 * RequestQueue mRequestQueue;
 * Context mContext;
 * ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCacheForImageLoader(mContext));
 */
public class LruBitmapCacheForImageLoader extends LruCache<String,Bitmap>
        implements ImageLoader.ImageCache{

    public LruBitmapCacheForImageLoader(int maxSize) {
        super(maxSize);
    }

    public LruBitmapCacheForImageLoader(Context context){
        this(getCacheSize(context));
    }

    private static int getCacheSize(Context context) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight = dm.heightPixels;

        //4 bytes per pixel
        final int screenBytes = screenHeight * screenWidth * 4;

        return screenBytes * 3;
    }

    //计算每一个Entry的大小
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
