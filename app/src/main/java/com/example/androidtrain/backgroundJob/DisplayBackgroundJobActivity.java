package com.example.androidtrain.backgroundJob;

import android.app.Activity;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.androidtrain.R;
import com.example.androidtrain.backgroundJob.backgroundService.BackgroundConstants;
import com.example.androidtrain.backgroundJob.backgroundService.RSSPullService;

import java.util.ArrayList;
import java.util.List;

public class DisplayBackgroundJobActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "BackgroundJob";
    private static final int URL_LOADER = 1001;

    TextView mRssTextView;
    ListView mListView;

    RssBroadcastReceiver mBroadcastReceiver;

    LocalBroadcastManager broadcastManager;

    List<String> contactsList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_background_job);

        mRssTextView = (TextView)findViewById(R.id.rss_pullService_text_view);
        mListView = (ListView) findViewById(R.id.cursor_loader_list_view);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contactsList);
        mListView.setAdapter(adapter);

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

    public void startSearch(){
        getSupportLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case URL_LOADER:
                return new CursorLoader(this,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            while (data.moveToNext()){
                String displayName = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contactsList.add(displayName + "\n" + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (data != null){
                data.close();
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 这个方法在CursorLoader检测到Cursor上的数据发生变化的时候会被触发。当数据发生变化时，系统也会
     * 触发重新查询的操作。
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void startCursorLoaderTest(View view) {
        startSearch();
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
