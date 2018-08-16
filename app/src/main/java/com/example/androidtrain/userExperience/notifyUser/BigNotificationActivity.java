package com.example.androidtrain.userExperience.notifyUser;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.androidtrain.MainActivity;
import com.example.androidtrain.R;

public class BigNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_notification);
        startBigViewNotification();
    }



    public void startBigViewNotification(){
        String msg = "This is Big View Notification Test detail message!";
        //设置两个按钮的方法
        Intent dismissIntent = new Intent(this, PingService.class);
        dismissIntent.setAction(NotifyConstants.ACTION_DISMISS);
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(this, PingService.class);
        snoozeIntent.setAction(NotifyConstants.ACTION_SNOOZE);
        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

        // Constructs the Builder object
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("BigViewTitle")
                .setDefaults(Notification.DEFAULT_ALL)//requires VIBRATE permission

                /**
                 * 设置BigView布局，显示详细内容在扩展的布局中，在>=4.1的设备中生效
                 */
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .addAction(R.drawable.jiguang_socialize_wechat, "微信", piDismiss)
                .addAction(R.drawable.jiguang_socialize_qq, "QQ", piSnooze);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int mNotificationId = 101;

        mNotifyMgr.notify(MainActivity.count++, builder.build());

    }
}
