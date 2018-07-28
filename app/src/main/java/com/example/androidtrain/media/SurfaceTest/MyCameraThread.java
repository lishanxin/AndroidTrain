package com.example.androidtrain.media.SurfaceTest;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by sx on 2018/7/2.
 */

public class MyCameraThread {

    SurfaceHolder holder;

    public MyCameraThread(SurfaceHolder holder){
        this.holder = holder;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                setShow();
//            }
//        }).start();

    }


}
