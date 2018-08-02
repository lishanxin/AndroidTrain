package com.example.androidtrain.buildingconnect.efficientDownloads;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sx on 2018/8/2.
 */

//网络管理，如何省电。无线状态机工作时，最是耗电。
public class RegularUpdate {
    private Context mContext;
    private AlarmManager alarmManager;
    int alarmType = AlarmManager.ELAPSED_REALTIME;
    long interval = AlarmManager.INTERVAL_HOUR;
    long start = System.currentTimeMillis() + interval;

    public static final String PREFS = "ExpBackOff";
    public static String PREFS_APPUSED = "IsAppUsed";
    private static String PREFS_INTERVAL = "PrefsInterval";
    private static final long DEFAULT_REFRESH_INTERVAL = 60;
    private static final long MAX_REFRESH_INTERVAL = Long.MAX_VALUE/16;

    public RegularUpdate(Context context){
        this.mContext = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    //省电操作方法一：延迟操作至某一个共同的时间点
    public void initAlarm(){
        //如果几个提醒都安排在某个点同时被触发，那么就可以使得多个操作在同一个无线电状态下操作完。
        //即设定某个固定时间，将多个无线电状态机操作一齐放在那个时间进行操作，进行省电。
//        alarmManager.setInexactRepeating(alarmType, start, interval, pi);
    }


    //省电操作方法二：指数退避算法
    //上一次更新操作之后还未被使用的情况下，使用指数退避算法 exponential back-off algorithm 来减少更新频率
    //这个方法还不太了解，需查看资料
    public void ExpBackOffAlgorithm(){
        SharedPreferences sp =
                mContext.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE);

        boolean appUsed = sp.getBoolean(PREFS_APPUSED, false);
        long updateInterval = sp.getLong(PREFS_INTERVAL, DEFAULT_REFRESH_INTERVAL);

        if (!appUsed){
            if ((updateInterval *=2) > MAX_REFRESH_INTERVAL)
                updateInterval = MAX_REFRESH_INTERVAL;
        }

        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putBoolean(PREFS_APPUSED, false);
        spEdit.putLong(PREFS_INTERVAL, updateInterval);
        spEdit.apply();

        rescheduleUpdates(updateInterval);
        executeUpdateOrPrefetch();
    }

    private void executeUpdateOrPrefetch() {

    }

    private void rescheduleUpdates(long updateInterval) {

    }


    private void retryIn(long interval){
        boolean success = attemptTransfer();

        if (!success){
            retryIn(interval *2 < MAX_REFRESH_INTERVAL ?
            interval*2 : MAX_REFRESH_INTERVAL);
        }
    }

    private boolean attemptTransfer() {
        return false;
    }


}
