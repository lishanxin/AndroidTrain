package com.example.androidtrain.pictureAnimation.screenslide;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by lizz on 2018/7/19.
 */

public class DepthPageTransformer implements ViewPager.PageTransformer {

    //设置最小缩放比例
    private static final float MIN_SCALE = 0.75f;
    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1){//(-Infinity, -1)
            // This page is way off-screen to the left
            //界面向左滑动并消失
            view.setAlpha(0);
        }else if (position <= 0) { //[-1, 0]
            //page滑动时，左边的界面使用默认的Viewpager的滑动效果
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleY(1);
            view.setScaleX(1);
        } else if (position <= 1){//(0, 1]
            //page滑动时，右边的界面使用“潜藏”效果的动画
            //设置透明度,随着position的变化，逐渐显示
            view.setAlpha(1 - position);

            //抵消ViewPager默认的滑动效果
            view.setTranslationX(pageWidth * -position);

            //缩放page，介于（MIN_SCALE,1）之间
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else{// (1, +Infinity)
            //界面向右滑动并消失
            view.setAlpha(0);
        }
    }
}
