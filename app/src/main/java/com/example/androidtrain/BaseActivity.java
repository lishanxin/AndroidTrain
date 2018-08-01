package com.example.androidtrain;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.androidtrain.security.AntiHijackUtil;

/**
 * Created by sx on 2018/8/1.
 */

//防劫持专用Activity
public class BaseActivity extends AppCompatActivity {

    //
    AntiHijackUtil antiHijack = AntiHijackUtil.INSTANCE;

    private static final String TAG = "BaseActivity";

    private static boolean needAlarm = true;

    HomeKeyClickReceiver homeKeyClickReceiver;

    IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLog("onCreate");

        //此处监听Home键
        homeKeyClickReceiver = new HomeKeyClickReceiver(new HomeKeyListener() {

            //Home键的监听内容，设置不产生提示
            @Override
            public void onPressHomeKey() {
                needAlarm = false;
            }
        });
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    //监听返回键，如果是返回键，则表示不需要提醒客户
    //Home键被系统屏蔽，此处无法监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showLog("" + keyCode);
        if((keyCode==KeyEvent.KEYCODE_BACK || keyCode==KeyEvent.KEYCODE_HOME ) && event.getRepeatCount()==0){
            needAlarm = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        showLog("onPause");

        //如果不是人为按键进行界面切换，则needAlarm=true；进行提示程序
        if (needAlarm){
            antiHijack.isHijacked = true;
            antiHijack.checkHijack(this);
        }
        needAlarm = true;
        unregisterReceiver(homeKeyClickReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLog("onResume");
        antiHijack.isHijacked = false;

        registerReceiver(homeKeyClickReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLog("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showLog("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showLog("onDestroy");
    }

    private void showLog(String msg){
        Log.d(TAG, msg);
    }

    private class HomeKeyClickReceiver extends BroadcastReceiver {
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
                            showLog("短按");
                            //短按
//                            listener.onHomeKeyShortPressed();
                        }
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        //Home key long pressed.
                        if (null != listener) {
                            listener.onPressHomeKey();
                            showLog("长按");
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
