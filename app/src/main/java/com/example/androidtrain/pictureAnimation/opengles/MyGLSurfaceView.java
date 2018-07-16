package com.example.androidtrain.pictureAnimation.opengles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by lizz on 2018/7/15.
 */

class MyGLSurfaceView extends GLSurfaceView{
    private final MyGLRenderer mRenderer;

    //配置触摸监听器
    private final float TOUCH_SCALE_FACTOR = 180.0F / 320;
    private float mPreviousX;
    private float mPreviousY;

    public MyGLSurfaceView(Context context) {
        super(context);

        //Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        //Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        //为了能够让三角形旋转，需将此行代码注释掉，否者OpenGL只会对这个形状执行一次旋转
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight()/2){
                    dx = dx * -1;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth()/ 2){
                    dy = dy * -1;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy ) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviousY = y;
        mPreviousX = x;

        return true;
    }
}
