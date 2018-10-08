package com.example.androidtrain.backgroundJob.scheduler;

import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.androidtrain.R;

public class SchedulerActivity extends AppCompatActivity {

    //还可以再layout.xml文件中设置android:keepScreenOn="true"来使屏幕常亮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        //设置屏幕常亮。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    //动态关闭屏幕常亮的方法
    private void closeScreenOn(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    //控制CPU，使其保持唤醒状态
    //使用唤醒锁的一种合理情况可能是：一个后台服务需要在屏幕关闭时利用唤醒锁保持CPU运行。
    // 强调，应该尽可能规避使用强制唤醒，因为它会影响到电池寿命。
    /**
     * 不必使用唤醒锁的情况：
     1：如果你的应用正在执行一个HTTP长连接的下载任务，可以考虑使用DownloadManager。
     2：如果你的应用正在从一个外部服务器同步数据，可以考虑创建一个SyncAdapter
     3：如果你的应用需要依赖于某些后台服务，可以考虑使用RepeatingAlarm或者Google Cloud Messaging，
     以此每隔特定的时间，将这些服务激活。
     */
    public void wakeLockAcquire(){
        //首先需要权限android.permission.WAKE_LOCK，然后再设置唤醒锁
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();

        //释放唤醒锁
//        wakeLock.release();
    }

}
