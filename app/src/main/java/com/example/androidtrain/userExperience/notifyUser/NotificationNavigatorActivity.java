package com.example.androidtrain.userExperience.notifyUser;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.androidtrain.MainActivity;
import com.example.androidtrain.R;

public class NotificationNavigatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_navigator);

        notifyWithBackStack();
        notifyWithoutStack();
    }

    private void notifyWithBackStack(){
        Intent resultIntent = new Intent(this, NotificationResultActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the back stack
        stackBuilder.addParentStack(NotificationResultActivity.class);

        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(resultPendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Navigator")
                .setContentText("Navigator from Result to MainActivity");

        NotificationManager mNotificationMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotificationMgr.notify(MainActivity.count++, builder.build());
    }

    private void notifyWithoutStack(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("NavigatorWithoutStack")
                .setContentText("Navigator to Result without stack");

        Intent notifyIntent = new Intent(this, NotifyResultWithoutStackActivity.class);

        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Creates the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        //Puts the PendingIntent into the notification builder
        builder.setContentIntent(notifyPendingIntent);



        //Notifications are issued by sending them to the
        // NotificationManager system service.
        NotificationManager mNotificationMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Builds an anonymous Notification object from the builder, and
        // passes it to the NotificationManager
        mNotificationMgr.notify(MainActivity.count++, builder.build());
        mNotificationMgr.cancelAll();
    }

    //更新通知
    private void updateNotification(){
        //mNotificationMgr.notify(ID, Notification);保持ID不变，改变Notification，重新调用此方法，就能够更新之前相同ID的通知。
    }

    //删除/移除通知
    private void deleteNotification(){
        //1: NotificationCompat.Builder.setAutoCancel(true);设置通知被点击后，自动删除。
        //2:NotificationManager.cancel(ID);删除制定ID的通知
        //3:NotificationManager.cancelAll();删除所有此应用的通知
    }
}
