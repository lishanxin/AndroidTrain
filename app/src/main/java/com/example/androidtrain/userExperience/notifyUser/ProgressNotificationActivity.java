package com.example.androidtrain.userExperience.notifyUser;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.androidtrain.R;

public class ProgressNotificationActivity extends AppCompatActivity {

    private static final String TAG = "ProgressNotify";

    NotificationManager mNotifyMgr;

    NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_notification);

        startProgressNotification();
    }

    public void startProgressNotification(){
        //展示进度条： setProgress(max, progress, false))
        // 第三个参数boolean决定是indeterminate(true),
        //还是determinate(false).如果你可以在任何时候估算这个操作得花多少时间以及当前已经完成多少，你
        // 可以用“determinate（确定的，下同）”形式的指示器（一个进度条）。如果你不能估算这个操作的
        // 长度，使用“indeterminate（不确定，下同）”形式的指示器（一个活动的指示器）。
        // 一般设置max为100，增加progress表示“完成百分比”
        //移除进度条：setProgress(0,0,false)

        final int id = 1;

        mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher_background);

        // start a lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int incr;
                // Do the "lengthy" operation 20 times
                for (incr = 0; incr <= 100; incr += 5){
                    //set progress to a max value
                    mBuilder.setProgress(100, incr, false);
                    // Displays the progress bar for the first time
                    mNotifyMgr.notify(id, mBuilder.build());

                    //作下载假象
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "sleep Failure");
                        e.printStackTrace();
                    }
                }

                // 下载完成时，更新通知
                mBuilder.setContentText("Download complete");

                //移除进度条
                mBuilder.setProgress(0,0,false);

                //设置此项会让进度条一直转动
//                mBuilder.setProgress(0,0,true);
                mNotifyMgr.notify(id, mBuilder.build());
            }
        }).start();
    }
}
