package com.example.androidtrain.bestUserInput.gestures;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class DragAndScaleView extends RelativeLayout {
    private static final int INVALID_POINTER_ID = -1;

    private ScaleGestureDetector mScaleDetector;

    // The 'active pointer' is the one currently moving our object
    private int mActivePointerId = INVALID_POINTER_ID;

    private float mLastTouchX;
    private float mLastTouchY;

    private float mPosX;
    private float mPosY;

    private AttributeSet mAttr;

    public DragAndScaleView(Context context) {
        super(context);
    }

    public DragAndScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mAttr = attrs;
    }

    public DragAndScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mAttr = attrs;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DragAndScaleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mAttr = attrs;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the ScaleGestureDetector inspect all events.
//        mScaleDetector.onTouchEvent(event);

        final int action = event.getActionMasked();

        switch (action){
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = event.getActionIndex();
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Rememver where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;

                // save the ID of this pointer (for dragging)
                mActivePointerId = event.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        event.findPointerIndex(mActivePointerId);

                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;


                invalidate();

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
                lp.setMargins((int)mPosX, (int)mPosY, 0, 0);
                setLayoutParams(lp);
                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            }
            case MotionEvent.ACTION_UP:{
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:{
                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId){
                    // This is our active pointer going up. Choose a new active pointer and adjust accodingly
                    final int newPointerIndex = pointerIndex == 0? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            return super.onScale(detector);
        }
    }
}
