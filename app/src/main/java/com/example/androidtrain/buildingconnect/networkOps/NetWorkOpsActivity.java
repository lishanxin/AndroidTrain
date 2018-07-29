package com.example.androidtrain.buildingconnect.networkOps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidtrain.R;

import org.w3c.dom.Text;

import java.io.IOException;

public class NetWorkOpsActivity extends AppCompatActivity {

    NetWorkUtil mNetWorkUtil;

    EditText mEditText;

    TextView mTextView;

    //相应网络管理的选择
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL = "https://www.baidu.com";

    //是否有Wifi连接
    private static boolean wifiConnected = false;
    //是否数据连接
    private static boolean mobileConnected = false;
    //是否需要重新刷新-Preferences
    public static boolean refreshDisplay = true;

    //用户当前Preference设置
    public static String sPref = null;

    //响应网络连接变化的广播接收器
    private NetworkReceiver receiver = new NetworkReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work_ops);

        mEditText = (EditText)findViewById(R.id.net_work_ops_edit_view);
        mTextView = (TextView)findViewById(R.id.net_work_ops_text_view);
        mNetWorkUtil = new NetWorkUtil(this);

        //相应网络管理的选择
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (receiver != null){
            this.unregisterReceiver(receiver);
        }
    }

    //当网络状态和偏好状态允许时，刷新界面显示


    @Override
    protected void onStart() {
        super.onStart();

        //获取用户网络偏好设置
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //获取偏好值,方法的第二个参数为默认值（当无法找到偏好值时）
        sPref = sharedPreferences.getString("listPref", "Wi-Fi");

        //获取当前网络的连接状态，并保存
        updateConnectedFlags();

        if (refreshDisplay){
            loadPage();
        }
    }

    //获取当前网络的连接状态，并保存
    private void updateConnectedFlags() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()){
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    //使用AsyncTask来下载xml数据流
    private void loadPage() {
        //此处语句判断是管理网络
        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                ||((sPref.equals(WIFI)) && (wifiConnected))){
            new DownloadWebpageText().execute(URL);
        }else {
            showErrorPage();
        }
    }

    private void showErrorPage() {
        mTextView.setText("No network connection available.");
    }


    public void getHttpResponse(View view) {
        String url = mEditText.getText().toString();
        if (mNetWorkUtil.isConnected()){
            new DownloadWebpageText().execute(url);
        }else {
            mTextView.setText("No network connection available.");
        }
    }

    public void netWorkSettings(View view) {
        Intent intent = new Intent(this, NetWorkSettingsActivity.class);
        startActivity(intent);
    }

    private class DownloadWebpageText extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            try {
                return mNetWorkUtil.downloadUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            mTextView.setText(result);
        }
    }

    private class NetworkReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
