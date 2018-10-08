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
import com.example.androidtrain.backgroundJob.scheduler.SchedulerActivity;

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
     * 当Cursor失效的时候，CursorLoader会被重置。这通常发生在Cursor相关的数据改变的时候。在重新执行
     * 查询操作之前，系统会执行你的onLoaderReset()回调方法。在这个回调方法中，你应该删除当前Cursor上
     * 的所有数据，避免发生内存泄露。一旦onLoaderReset()执行结束，CursorLoader就会重新执行查询操作。
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactsList.clear();
        adapter.notifyDataSetChanged();
    }

    public void startCursorLoaderTest(View view) {
        startSearch();
    }

    public void goToScheduler(View view) {
        Intent intent = new Intent(this, SchedulerActivity.class);
        startActivity(intent);
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
