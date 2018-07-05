package com.example.androidtrain;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;

/**
 * Created by sx on 2018/7/3.
 */

public class AlumOrientationEventListener extends OrientationEventListener {

    public int mOrientation = 0;


    /**
     * Creates a new OrientationEventListener.
     *
     * @param context for the OrientationEventListener.
     */
    public AlumOrientationEventListener(Context context) {
        super(context);
    }

    /**
     * Creates a new OrientationEventListener.
     *
     * @param context for the OrientationEventListener.
     * @param rate    at which sensor events are processed (see also
     *                {@link SensorManager SensorManager}). Use the default
     *                value of {@link SensorManager#SENSOR_DELAY_NORMAL
     *                SENSOR_DELAY_NORMAL} for simple screen orientation change detection.
     */
    public AlumOrientationEventListener(Context context, int rate) {
        super(context, rate);
    }

    /**
     * Called when the orientation of the device has changed.
     * orientation parameter is in degrees, ranging from 0 to 359.
     * orientation is 0 degrees when the device is oriented in its natural position,
     * 90 degrees when its left side is at the top, 180 degrees when it is upside down,
     * and 270 degrees when its right side is to the top.
     * {@link #ORIENTATION_UNKNOWN} is returned when the device is close to flat
     * and the orientation cannot be determined.
     *
     * @param orientation The new orientation of the device.
     * @see #ORIENTATION_UNKNOWN
     */
    @Override
    public void onOrientationChanged(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN){
//            Log.d("Orientation","unknown");
            return;
        }

        //保证只返回四个方向，
        //int newOrientation = ((orientation + 45) / 90 * 90) % 360;
        //为了满足录像角度的问题，将45改为135.
        int newOrientation = ((orientation + 135) / 90 * 90) % 360;
        mOrientation = newOrientation;
        Log.d("Orientation",  "is: "+ newOrientation + "");
    }
}
