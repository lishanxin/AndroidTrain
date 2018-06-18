package com.example.androidtrain.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.androidtrain.R;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class ManagePlaybackActivity extends AppCompatActivity {

    private static final String TAG = "ManagePlayback";

    AudioManager am;
    ComponentName mCN;
    NoisyAudioStreamReceiver mNoisyReceiver;

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT){
                Log.d(TAG, "pause playback");
                //Pause playback
            }else if (focusChange == AudioManager.AUDIOFOCUS_GAIN){
                Log.d(TAG, "Resume playback");
                //Resume playback
            }else if (focusChange == AudioManager.AUDIOFOCUS_LOSS){
                Log.d(TAG, "AUDIOFOCUS_LOSS playback/stop");
                am.unregisterMediaButtonEventReceiver(mCN);
                am.abandonAudioFocus(afChangeListener);
                ///Stop playback
            }else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                Log.d(TAG, "Lower the volume");
                //Lower the volume
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_playback);

        //设置后，不管此Activity是否可见，按下设备的音量键都能影响我们制定的音频流
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        //注册能够唯一相应媒体按钮的广播
        mCN = new ComponentName("com.example.androidtrain", "RemoteControlReceiver");
        am.registerMediaButtonEventReceiver(mCN);

        //获取音频焦点，以便进行音频管理
        //此为永久的焦点锁定
        int result = am.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            am.registerMediaButtonEventReceiver(mCN);
            //Start playback
        }

        //此为短暂的焦点锁定,开启DUCKING，其他应用的音乐不会关闭
        int tranResult = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        if (tranResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            //Start playback
        }

        //判断音频输出的硬件设备是什么，扬声器/有线耳机/蓝牙等。
        if (am.isBluetoothA2dpOn()){
            Log.d(TAG, "Bluetooth");
            //Adjust output for Bluetooth
        }else if (am.isSpeakerphoneOn()){
            Log.d(TAG, "Speakerphone");
            //Adjust output for Speakerphone.
        }else if (am.isWiredHeadsetOn()){
            Log.d(TAG, "WiredHeadset");
            //Adjust output for headsets
        }else {
            Log.d(TAG, "no one can hear it");
            //If audio plays and no one can hear it, is it still playing?
        }


        //注册当音频输出设备变化时。
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        mNoisyReceiver = new NoisyAudioStreamReceiver();
        try {
            registerReceiver(mNoisyReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        am.unregisterMediaButtonEventReceiver(mCN);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mNoisyReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当有线耳机被拔出或者蓝牙设备断开连接的时候，音频流会自动输出到内置的扬声器上。
     * 假设播放声音很大，这个时候突然转到扬声器播放会显得非常嘈杂。
     * 幸运的是，系统会在这种情况下广播带有ACTION_AUDIO_BECOMING_NOISY的Intent。
     * 无论何时播放音频，我们都应该注册一个BroadcastReceiver来监听这个Intent。
     * 在使用音乐播放器时，用户通常会希望此时能够暂停当前歌曲的播放。
     * 而在游戏当中，用户通常会希望可以减低音量。
     */
    private class NoisyAudioStreamReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())){
                Log.d(TAG, "become noisy");
                //Pause the playback
            }
        }
    }
}
