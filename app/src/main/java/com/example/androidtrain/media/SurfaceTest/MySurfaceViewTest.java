package com.example.androidtrain.media.SurfaceTest;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by lizz on 2018/7/2.
 */

public class MySurfaceViewTest  extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder holder;
//    private MyThread myThread;
    private MyCameraThread myCameraThread;

    public MySurfaceViewTest(Context context) {
        super(context);

        holder = getHolder();

        holder.addCallback(this);

//        myThread = new MyThread(holder);

        myCameraThread = new MyCameraThread(holder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        myThread.setRun(true);
//        myThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        myThread.setRun(false);
    }
}
