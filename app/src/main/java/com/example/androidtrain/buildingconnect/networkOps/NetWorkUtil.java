package com.example.androidtrain.buildingconnect.networkOps;

/**
 * Created by lizz on 2018/7/26.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 需求权限：INTERNET，ACCESS_NETWORK_STATE
 */
public class NetWorkUtil {
    private static final String TAG = "NetWorkUtil";
    Context mContext;
    public NetWorkUtil(Context context){
        mContext = context;
    }

    /**
     *
     * @return 检查当前网络是否可用,推荐。若要判断是连接Wifi还是数据，可以使用方法：testNetWorkState
     */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public String downloadUrl(String myUrl) throws IOException{
        InputStream is = null;

        //只显示获得网页的前500个字符
        int len = 500;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);//milliseconds
            conn.setConnectTimeout(15000);//milliseconds
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            //Starts the qury
            conn.connect();

            //状态码是 200 则意味着连接成功
            int response = conn.getResponseCode();

            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            //Convert the InputStream into a string
            String contentAsString = readStringFromInputStream(is, len);
            return contentAsString;
        } finally {
            //最终确认InputStream是否关闭
            if (is != null){
                is.close();
            }
        }
    }

    private InputStream downloadUrlForInputStream(String urlString) throws  IOException{
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);//milliseconds
        conn.setConnectTimeout(15000);//milliseconds
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        //Starts the qury
        conn.connect();
        return conn.getInputStream();
    }

    private String readStringFromInputStream(InputStream is, int len) throws IOException, UnsupportedEncodingException{
        Reader reader = null;
        reader = new InputStreamReader(is, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        String buffer1 = new String(buffer);
        Log.d(TAG, "buffer1: " + buffer1 );
        reader.read(buffer);
        String buffer2 = new String(buffer);
        Log.d(TAG, "buffer2: " + buffer2 );
        return buffer1;
    }

    private Bitmap readBitmapFromInputStream(InputStream is){
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    //判断网络时Wifi还是数据网络
    public void testNetWorkState(){
        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isAvailable();
        Log.d(TAG, "Wifi connected: " + isWifiConn);
        Log.d(TAG, "Mobile connected: " + isMobileConn);
    }

    //判断网络供应商
    public  String getProvidersName() {
        TelephonyManager telephonyManager = ((TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE));
        String str = "N/A";
        String IMSI = "NULL";
        try {
            IMSI = telephonyManager.getSubscriberId();
            System.out.println(IMSI);
            if (IMSI.startsWith("46000"))
                str = "中国移动";
            else if (IMSI.startsWith("46002"))
                str = "中国移动";
            else if (IMSI.startsWith("46001"))
                str = "中国联通";
            else if (IMSI.startsWith("46003"))
                str = "中国电信";
        } catch (SecurityException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "IMSI is:" + IMSI + "; 供应商：" + str);
        return str;
    }

    public String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException{
        InputStream is = null;

        StackOverXmlParseUtil stackOverXmlParseUtil = new StackOverXmlParseUtil();
        List<StackOverXmlParseUtil.Entry> entries = null;
        String title = null;
        String url = null;
        String summary = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        //检查用户是否对概要文字设置了偏好
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean pref = sharedPref.getBoolean("summaryPref", false);

        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<h3>" + "网页标题" + "</h3>");
        htmlString.append("<em>" + "更新于" + " " + formatter.format(rightNow.getTime()) + "</em>");

        try {
            is = downloadUrlForInputStream(urlString);
            entries = stackOverXmlParseUtil.pase(is);
        }finally {
            if (is != null){
                is.close();
            }
        }

        for (StackOverXmlParseUtil.Entry entry: entries){
            htmlString.append("<p><a href='");
            htmlString.append(entry.link);
            htmlString.append("'>" + entry.title + "</a></p>");

            if (pref){
                htmlString.append(entry.summary);
            }
        }

        return htmlString.toString();
    }
}
