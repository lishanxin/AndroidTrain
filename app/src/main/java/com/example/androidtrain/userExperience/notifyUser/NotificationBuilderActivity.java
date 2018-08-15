package com.example.androidtrain.userExperience.notifyUser;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.androidtrain.MainActivity;
import com.example.androidtrain.R;

public class NotificationBuilderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_builder);


        WebView webView = (WebView) findViewById(R.id.notification_webView);
        webView.loadUrl("http://zj.golden-palm.com.cn/Index/lists/colid/46.html");

        setNotificate();
    }

    public void setNotificate(){
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("HelloWorld!");

        Intent resultIntent = new Intent(this, NotificationResultActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int mNotificationId = 101;

        mNotifyMgr.notify(MainActivity.count++, mBuilder.build());
    }
}
