package com.example.androidtrain.pictureAnimation.screenslide;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by lizz on 2018/7/18.
 */

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    //设置相对缩放比例最小值
    private static final float MIN_SCALE = 0.85f;

    //设置透明度最小值
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1){ //(-Infinity, -1)
            //This page is way off-screen to the left
            //页面的最左侧相对于屏幕的最左侧有-1的界面差值,设置完全透明
            view.setAlpha(0);
        }else if (position <= 1){ //[-1, 1]
            // 设置缩放比例
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor)/2;
            float horzMargin = pageWidth * (1 - scaleFactor)/2;

            //在ViewPager滑动距离的前提下，继续往左或往右移一小段距离
            if (position < 0){
                //往左移
                view.setTranslationX(horzMargin -vertMargin);
            }else {
                //往右移
                view.setTranslationX(-horzMargin + vertMargin);
            }

            //进行缩放
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            //透明处理
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_ALPHA) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        }else {// (1, +Infinity)
            // This page is way off-screen to the right
            view.setAlpha(0);
        }


    }
}
