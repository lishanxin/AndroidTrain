package com.example.androidtrain.uiDesign.createView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by lizz on 2018/9/1.
 */

public class GestureTestView extends FrameLayout {

    private static final String TAG = "GestureView";
    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    public GestureTestView(Context context) {
        this(context, null);
    }

    public GestureTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(getContext(), new MyTestGestureListener());
        mScroller = new Scroller(getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private class MyTestGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //存储位置信息至mScroller
            mScroller.startScroll(getScrollX(),getScrollY(), (int)distanceX, (int)distanceY, 10000);
            Log.d(TAG, "onScroll:" + distanceX + "-" + distanceY);
            //强制更新View，以广播mScroller数据的变化
            invalidate();
            return true;
        }
    }

    //监听子View中的mScroller数据是否变化，变化则回调此项。
    @Override
    public void computeScroll() {
        //判断mScroller数据是否变化
        if (mScroller.computeScrollOffset()){
            //移动子View
            scrollTo(mScroller.getFinalX(), mScroller.getFinalY());
            //强制更新
            postInvalidate();
            Log.d(TAG, "computeScroll:" + mScroller.getFinalY());
        }
        super.computeScroll();
    }
}
