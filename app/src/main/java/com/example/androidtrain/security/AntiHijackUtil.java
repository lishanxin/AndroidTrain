package com.example.androidtrain.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.example.androidtrain.R;

/**
 * 防劫持
 * Created by sx on 2018/8/1.
 */

public enum AntiHijackUtil {
    INSTANCE;

    //监听是否是按钮，如何是主动按钮，则判断不需要提示
    public boolean needAlarm = true;

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

                //如果不是人为按键进行界面切换，则needAlarm=true；进行提示程序
                //通过延迟来判断是否重新进入了本App的新页面，如果是的话，isHijacked就为false。
                if (needAlarm && isHijacked){
                    showHijackToast(context);
                }
                //每次判断后，都将重置此值
                needAlarm = true;
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

    public static class HomeKeyClickReceiver extends BroadcastReceiver {
        private static final String SYSTEM_REASON = "reason";
        private static final String SYSTEM_HOME_KEY = "homekey";
        private static final String SYSTEM_RECENT_APPS = "recentapps";

        private HomeKeyListener listener;

        public HomeKeyClickReceiver(HomeKeyListener listener){
            this.listener = listener;
        }

        //由于在点击home键时，系统会发出一个Intent.ACTION_CLOSE_SYSTEM_DIALOGS广播；
        //使用此广播接收器对广播进行处理，处理内容在监听器接口中。
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (null != reason) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        //Home key short pressed.
                        if (null != listener) {
                            listener.onPressHomeKey();
//                            showLog("短按");
                            //短按
//                            listener.onHomeKeyShortPressed();
                        }
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        //Home key long pressed//or hard menu key short pressed to show recentApps
                        if (null != listener) {
                            listener.onPressHomeKey();
//                            showLog("长按");
                            //长按
//                            listener.onHomeKeyLongPressed();
                        }
                    }
                }
            }
        }
    }

    public interface HomeKeyListener{
        public void onPressHomeKey();
    }
}
