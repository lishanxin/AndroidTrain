package com.example.androidtrain.backgroundJob.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidtrain.R;

import java.util.Calendar;

/**
 * 重点：
 * 1：对任何由重复闹钟触发的网络请求添加一定的随机性（抖动）：
    在闹钟触发时做一些本地任务。“本地任务”指的是任何不需要访问服务器或者从服务器获取数据的任务；
    同时对于那些包含有网络请求的闹钟，在调度时机上增加一些随机性。
    2：尽量让你的闹钟频率最小；
    3：如果不是必要的情况，不要唤醒设备（这一点与闹钟的类型有关，本节课后续部分会提到）；
    4：触发闹钟的时间不必过度精确； 尽量使用setInexactRepeating()方法替代setRepeating()方法。当你使
 用setInexactRepeating()方法时，Android系统会集中多个应用的重复闹钟同步请求，并一起触发它们。这可以
 减少系统将设备唤醒的总次数，以此减少电量消耗。从Android 4.4（API Level19）开始，所有的重复闹钟都将
 是非精确型的。注意虽然setInexactRepeating()是setRepeating()的改进版本，它依然可能会导致每一个应用的
 实例在某一时间段内同时访问服务器，造成服务器负荷过重。因此如之前所述，对于网络请求，我们需要为闹钟
 的触发时机增加随机性。
    5：尽量避免让闹钟基于时钟时间。
 */
public class AlarmScheduleActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    EditText mHour, mMinute, mSecond;
    Button mStartAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_schedule);

        mHour = (EditText) findViewById(R.id.alarm_set_hours);
        mMinute = (EditText) findViewById(R.id.alarm_set_minutes);
        mSecond = (EditText) findViewById(R.id.alarm_set_seconds);

        mStartAlarmButton = (Button) findViewById(R.id.alarm_start_button);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        mStartAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.valueOf(mHour.getText().toString().trim().isEmpty()?"0":mHour.getText().toString());
                int minute = Integer.valueOf(mMinute.getText().toString().trim().isEmpty()?"0":mMinute.getText().toString());
                int second = Integer.valueOf(mSecond.getText().toString().trim().isEmpty()?"0":mSecond.getText().toString());
                repeatAlarmByRTCOne(hour,minute,second);
            }
        });
    }

    //闹钟类型有两大类：ELAPSED_REALTIME和REAL_TIME_CLOCK（RTC）。ELAPSED_REALTIME从系统启动之后开
    // 始计算，REAL_TIME_CLOCK使用的是世界统一时间（UTC）
    //使用setRepeating()时，你可以制定一个自定义的时间间隔，但在使用setInexactRepeating()时不支持这
    // 么做。此时你只能选择一些时间间隔常量，例如：INTERVAL_FIFTEEN_MINUTES ，INTERVAL_DAY等。完整的
    // 常量列表，可以查看AlarmManager。
    public void startOneTimeAlarm(int hour, int minute, int second){
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 2* 1000, alarmIntent);
    }

    public void repeatAlarmByElapsed(int hour, int minute, int second){
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
    }

    //激活Alarm，并每天重复一次
    public void repeatAlarmByRTCOne(int hour, int minute, int second){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, second);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 10 * 60 * 1000, alarmIntent);
    }


    //取消闹钟
    public void cancelAlarm(){
        if (alarmManager!= null) {
            alarmManager.cancel(alarmIntent);
        }
    }


    //设置组件是否可被隐式调用
    public void setComponentEnable(){
        ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    //设置组件是否可被隐式调用
    public void setComponentUnEnable(){
        ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
