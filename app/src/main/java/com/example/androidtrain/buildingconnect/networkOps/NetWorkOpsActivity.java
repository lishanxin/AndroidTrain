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
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class NetWorkOpsActivity extends BaseActivity {

    NetWorkUtil mNetWorkUtil;

    EditText mEditText;

    TextView mTextView;

    WebView mWebView;

    //相应网络管理的选择
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL = "https://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

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
        mWebView = (WebView) findViewById(R.id.net_work_ops_web_view);
        mNetWorkUtil = new NetWorkUtil(this);

        mNetWorkUtil.getProvidersName();

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
            //显示String
//            loadPage();
            //将xml部分解析转换成html，并显示
            loadXmlPage();
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

    private void loadXmlPage(){
        //此处语句判断是管理网络
        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                ||((sPref.equals(WIFI)) && (wifiConnected))){
            new DownloadXmlTask().execute(URL);
        }else {
//            showErrorPage();
        }
    }

    private void showErrorPage() {
        mTextView.setText("No network connection available.");
    }


    public void getHttpResponse(View view) {
        String url = mEditText.getText().toString();
        loadXmlPage();
//        if (mNetWorkUtil.isConnected()){
//            new DownloadWebpageText().execute(url);
//        }else {
//            mTextView.setText("No network connection available.");
//        }
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

    private class DownloadXmlTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            try {
                return mNetWorkUtil.loadXmlFromNetwork(url);
            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            } catch (XmlPullParserException e){
                e.printStackTrace();
                return "Xml parse is error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            mWebView.loadData(result, "text/html", null);
        }
    }

    private class NetworkReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conn = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            //检查用户的网络偏好设置，根据此设置来判断是否刷新当前页。
            //如果用户的网络偏好设置为:Wifi,则检查此设备是否连接Wifi
            if (WIFI.equals(sPref) && networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                //设备已连接Wifi，则启动刷新。启动刷新后，展示界面会在用户回到app后刷新。
                refreshDisplay = true;
                Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_LONG).show();
            }else if (ANY.equals(sPref) && networkInfo != null){
                refreshDisplay = true;
                Toast.makeText(context, R.string.any_connected, Toast.LENGTH_LONG).show();
            }else {
                //此处用户无法下载任何内容，原因可能有如下几点：
                //1:没有网络连接，wifi和数据网络都未连接；
                //2：用户连接了数据网络，但是用户的网络偏好设置为Wifi，所以无法连接到网络
                refreshDisplay = false;
                Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_LONG).show();;
            }
        }
    }
}
