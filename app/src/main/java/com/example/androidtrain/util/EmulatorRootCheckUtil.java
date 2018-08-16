package com.example.androidtrain.util;

import android.os.Build;
import android.util.Log;

import java.io.File;

/**
 * Created by sx on 2018/8/16.
 */

//判断手机是否是模拟器或者root过。

public class EmulatorRootCheckUtil {

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static void show(){
        if (EmulatorRootCheckUtil.isEmulator()){
            Log.d("Emulator", "yes, I am emulator");
        }else {
            Log.d("Emulator", "no, I am a device");
        }
    }


    public static boolean isRootDevice(){
        File f = null;
        final String kSuSearchPaths[] =
                {"/system/bin/", "/system/xbin/", "/system/sbin","/sbin/", "/vendor/bin/"};
        try{
            for (int i= 0; i < kSuSearchPaths.length; i++){
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
