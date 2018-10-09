package com.example.androidtrain.backgroundJob.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by lizz on 2018/10/9.
 */

public class AlarmReceiver extends BroadcastReceiver{

    private MediaPlayer myMediaPlayer;

    private int count = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "alarm" + count++, Toast.LENGTH_SHORT).show();
        playMusic(context);
    }


    private void playMusic(Context context){
        //强制设置音乐大小
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//获取当前音乐音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取最大音量

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);// 设置为最大音量

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getAssets().openFd("songbie.mp3");
            myMediaPlayer = new MediaPlayer();
            myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            myMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            myMediaPlayer.prepare();
            myMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
