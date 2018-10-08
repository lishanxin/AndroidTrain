package com.example.androidtrain.backgroundJob.scheduler;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by lizz on 2018/10/8.
 */

public class MyIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    Notification.Builder builder;

    public MyIntentService(){
        super("MyIntentService");
    }


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //do something that cause some seconds, and then release the wake lock
        //执行完任务后，执行下方的方法来释放CPU唤醒锁.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            MyWakefulReceiver.completeWakefulIntent(intent);
        }
    }
}
