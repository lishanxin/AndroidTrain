package com.example.androidtrain.backgroundJob.scheduler;

import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sx on 2018/10/10.
 */

public class SampleBootReceiver extends BroadcastReceiver {

    private static final String TAG = "SampleBoot";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Log.d(TAG, "Boot_Completed");
            Toast.makeText(context, "Boot Completed", Toast.LENGTH_LONG).show();
        };
    }
}
