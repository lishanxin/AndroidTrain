package com.example.androidtrain.backgroundJob;

import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.androidtrain.R;
import com.example.androidtrain.backgroundJob.backgroundService.BackgroundConstants;
import com.example.androidtrain.backgroundJob.backgroundService.RSSPullService;

public class DisplayBackgroundJobActivity extends AppCompatActivity {

    private static final String TAG = "BackgroundJob";

    TextView mRssTextView;

    RssBroadcastReceiver mBroadcastReceiver;

    LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_background_job);

        mRssTextView = (TextView)findViewById(R.id.rss_pullService_text_view);

        mBroadcastReceiver = new RssBroadcastReceiver();
        broadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BackgroundConstants.RSS_BROAD_CAST_RECEIVER_ACTION);
        broadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        broadcastManager.unregisterReceiver(mBroadcastReceiver);

        super.onDestroy();
    }

    public void startRssTest(View view) {
        Log.d(TAG, "startService");
        RSSPullService.startRssPullService(this, "https://www.baidu.com");
    }


    private class RssBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isMainLoop = Looper.getMainLooper() == Looper.myLooper();

            Log.d(TAG, "is Main loop:" + isMainLoop);

            mRssTextView.setText(intent.getStringExtra(BackgroundConstants.RSS_BROAD_CAST_MSG));
        }
    }
}
