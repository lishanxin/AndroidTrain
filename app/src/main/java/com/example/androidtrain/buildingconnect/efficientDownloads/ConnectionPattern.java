package com.example.androidtrain.buildingconnect.efficientDownloads;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by lizz on 2018/8/5.
 */

//根据网络连接类型来调整下载模式
public class ConnectionPattern {

    private static final int DEFAULT_PREFETCH_CACHE = 2*1024*1024;
    private static final int MAX_PREFETCH_CACHE = 6 * DEFAULT_PREFETCH_CACHE;

    //使用更大的贷款来更不频繁地下载更多数据
    public int getPrefetchCacheSize(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        int PrefetchCacheSize = DEFAULT_PREFETCH_CACHE;

        switch (activeNetwork.getType()){
            case (ConnectivityManager.TYPE_WIFI):
                PrefetchCacheSize = MAX_PREFETCH_CACHE; break;
            case (ConnectivityManager.TYPE_MOBILE):{
                switch (tm.getNetworkType()){
                    //4g
                    case (TelephonyManager.NETWORK_TYPE_LTE |
                        TelephonyManager.NETWORK_TYPE_HSPAP
                    ):
                        PrefetchCacheSize *= 4;
                    break;
                    //飞
                    case (TelephonyManager.NETWORK_TYPE_EDGE |
                    TelephonyManager.NETWORK_TYPE_GPRS):
                        PrefetchCacheSize /= 2;
                    break;
                    default:break;
                }
                break;
            }
            default:break;
        }
        return PrefetchCacheSize;
    }
}
