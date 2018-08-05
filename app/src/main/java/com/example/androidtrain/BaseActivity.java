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

    AntiHijackUtil.HomeKeyClickReceiver homeKeyClickReceiver;

    IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLog("onCreate");

        //此处监听Home键
        homeKeyClickReceiver = new AntiHijackUtil.HomeKeyClickReceiver(new AntiHijackUtil.HomeKeyListener() {

            //Home键的监听内容，设置不产生提示
            @Override
            public void onPressHomeKey() {
                antiHijack.needAlarm = false;
            }
        });
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    //监听返回键，如果是返回键，则表示不需要提醒客户
    //Home键被系统屏蔽，此处无法监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showLog("" + keyCode);
        if((keyCode==KeyEvent.KEYCODE_BACK ) && event.getRepeatCount()==0){
            antiHijack.needAlarm = false;

            //有一种情况是：按下Back键，但是被Rn代码拦截，不会切换Activity。此时needAlarm保持为false，若被劫持，则不会弹出窗口。
            //所以要对这种情况进行判断，如果发生这种情况，则重置needAlarm为true；
            //通过声明周期来判断这种情况，如果正常切换Activity的情况，肯定会发生Activity.onPause；否则就不会发生。
            //对是否发生onPause进行监听，发生了，则设置为true，然后在onResume中对其设置为false；
            //按下按钮后，发生了onPause，则设置needAlarm为false；
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

        antiHijack.isHijacked = true;
        antiHijack.checkHijack(this);
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
}
