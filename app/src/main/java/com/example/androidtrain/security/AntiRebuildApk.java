package com.example.androidtrain.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

/**
 * 对抗重打包
 * Created by sx on 2019/1/7.
 */

public class AntiRebuildApk {
    private static final String TAG = "Signature";

    // 签名值
    private static final int sig = 20398409;

    // 判断是否被重新打包
    public boolean checkRebuildApk(String packageName, Context context){
        if (getSignature(packageName, context) != sig){
            Log.d("Result:" , "App被重新打包了");
            return true;
        }
        return false;
    }

    // 获取包的签名值
    private int getSignature(String packageName, Context context){
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        int sig = 0;
        try {
            pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] s = pi.signatures;
            sig = s[0].hashCode();
        } catch (PackageManager.NameNotFoundException e) {
            sig = 0;
            e.printStackTrace();
        }
        // 返回查询到的签名
        return sig;
    }
}
