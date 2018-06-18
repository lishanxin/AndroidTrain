package com.example.androidtrain.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by lizz on 2018/6/17.
 */

public class RemoteControlReceiver extends BroadcastReceiver {

    private static final String TAG = "ControlReceiver";

    /**
     * 无论用户按下设备上任意一个控制按钮，系统都会广播一个带有ACTION_MEDIA_BUTTON的Intent。
     * 为了正确地响应这些操作，需要在Manifest文件中注册一个针对于该Action的BroadcastReceiver
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())){
            KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()){
                //Handle key press
            }else if (KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode()){
                Log.d(TAG, "Volume up");
            }
        }
    }
}
