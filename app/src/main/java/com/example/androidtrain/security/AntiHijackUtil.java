package com.example.androidtrain.security;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.view.Gravity;
import android.widget.Toast;

import com.example.androidtrain.R;

/**
 * Created by sx on 2018/8/1.
 */

public enum AntiHijackUtil {
    INSTANCE;

    //若判断为被劫持，则为true
    public boolean isHijacked = false;

    public void checkHijack(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isHijacked){
                    showHijackToast(context);
                }
            }
        }).start();
    }

    private void showHijackToast(Context context) {
        Looper.prepare();
        Toast beHijackedToast = Toast.makeText(context, context.getResources().getString(R.string.app_name) + "已切换至后台运行！" , Toast.LENGTH_LONG);
        beHijackedToast.setGravity(Gravity.CENTER, 0, 0);
        beHijackedToast.show();
        Looper.loop();
    }


}
