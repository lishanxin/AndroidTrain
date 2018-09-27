package com.example.androidtrain.bestUserInput.gestures;

import android.app.Activity;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.example.androidtrain.R;

public class MovementGesturesActivity extends Activity {

    private static final String TAG = "MovementGestures";

    private VelocityTracker mVelocityTracker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_gestures);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //表示第几个手指，与getActionMasked关联
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        int xPos = -1;
        int yPos = -1;
        if (event.getPointerCount() > 1){
            // the coordinates of the current screen contact, relative to
            // the responding View or Activity
            xPos = (int)event.getX(index);
            yPos = (int)event.getY(index);
            Log.d(TAG, "Multitouch event." + "index:" + index + ";x:" + xPos + ";y" + yPos);
        }else {
            xPos = (int)event.getX(index);
            yPos = (int)event.getY(index);
            Log.d(TAG, "Single touch event." + "index:" + index + ";x:" + xPos + ";y" + yPos);
        }
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "down.");
                if (mVelocityTracker == null){
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion
                    mVelocityTracker = VelocityTracker.obtain();
                }else {
                    // Reset the velocity tracker back to its initial state
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG, "pointer down.");
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);

                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // add getYVelocity() to retrieve the velocity for each pointer ID
                mVelocityTracker.computeCurrentVelocity(1000);

                //Log velocity of pixels per second
                // Best prctice to use VelocityTrackerCompat where possible.
                Log.d(TAG, "pointer" + index + ",X velocity: " +
                        mVelocityTracker.getXVelocity(pointerId));
                Log.d(TAG, "pointer" + index + ",Y velocity: " +
                        mVelocityTracker.getYVelocity(pointerId));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG, "pointer up");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                mVelocityTracker = null;


                break;
        }
        return true;
    }
}
