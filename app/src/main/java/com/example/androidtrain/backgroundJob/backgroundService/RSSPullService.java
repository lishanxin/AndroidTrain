package com.example.androidtrain.backgroundJob.backgroundService;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by sx on 2018/10/8.
 */

public class RSSPullService extends IntentService {

    private static final String TAG = "RssPullService";

    public static void startRssPullService(Context context, String url){
        Intent intent = new Intent(context, RSSPullService.class);
        intent.setData(Uri.parse(url));
        context.startService(intent);
    }

    public RSSPullService() {
        super("RSSPullService");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RSSPullService(String name) {
        super(name);
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     *               This may be null if the service is being restarted after
     *               its process has gone away; see
     *               {@link Service#onStartCommand}
     *               for details.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Uri url = intent.getData();
        boolean isMainLoop = Looper.getMainLooper() == Looper.myLooper();

        Log.d(TAG, "is Main loop:" + isMainLoop);
        try {
            Thread.sleep(1000);
            sendRssBroadCase(BackgroundConstants.RSS_BROAD_CAST_RECEIVER_ACTION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public void sendRssBroadCase(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(BackgroundConstants.RSS_BROAD_CAST_MSG, "test1woshiceshi");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
