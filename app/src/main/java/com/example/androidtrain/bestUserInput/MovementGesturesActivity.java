package com.example.androidtrain.bestUserInput;

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

        switch (action){
            case MotionEvent.ACTION_DOWN:
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
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
        }
        return true;
    }
}
