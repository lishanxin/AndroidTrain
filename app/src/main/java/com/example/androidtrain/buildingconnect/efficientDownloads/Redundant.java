package com.example.androidtrain.buildingconnect.efficientDownloads;

import android.app.AlarmManager;
import android.content.Context;
import android.net.http.HttpResponseCache;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lizz on 2018/8/2.
 */

//重复下载是冗余的
public class Redundant {

    private  Context mContext;

    public Redundant(Context context){
        this.mContext = context;
    }

    //设置要更新的时间
    public  static long lastUpdateTime = System.currentTimeMillis() + AlarmManager.INTERVAL_HOUR;

    public void downloadCheckUpdate(String url) throws IOException{
        long currentTime = System.currentTimeMillis();

        URL myUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();

        long expires = conn.getHeaderFieldDate("Expires", currentTime);
        long lastModified = conn.getHeaderFieldDate("Last-Modified", currentTime);

        setDataExpirationData(expires);

        //更新时间未到，跳过，不更新缓存
        if (lastModified < lastUpdateTime){
            //skip update
        }else {
            //Parse update
        }

        //获取缓存存储目录，在此目录下的文件会随着应用卸载而释放
        File file =  mContext.getExternalCacheDir();

    }

    private void setDataExpirationData(long expires) {

    }


    //HttpURLConnection响应缓存，
    //在4.0以上的设备会开启response cache，
    //在cache被开启之后，所有cache中的HTTP请求都可以直接在本地存储中进行响应，并不需要开启一个新的网络连接
    //被cache起来的response可以被server所确保没有过期，这样就减少了下载所需的带宽。
    //没有被cached的response会为了方便下次请求而被存储在response cache中。
    private void enableHttpResponeseCache(){
        try{
            long httpCacheSize = 10 * 1024 * 1024;
            File httpCacheDir = new File(mContext.getCacheDir(), "http");
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
//            Class.forName("android.net.http.HttpResponseCache")
//                    .getMethod("install", File.class, long.class)
//                    .invoke(null, httpCacheDir, httpCacheSize);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
