package com.example.androidtrain.security;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;

/**
 * Created by sx on 2019/1/7.
 */

public class AntiEmulator {
    private static String[] known_pipes = {
            "/dev/socket/qemud",
            "/dev/qemu_pipe"
    };
    private static String[] known_qemu_drivers = {
            "goldfish"
    };
    private static String[] known_files = {
            "/system/lib/libc_malloc_debug_qemu.so",
            "/system/qemu_trace",
            "/system/bin/qemu_props"
    };
    private static String[] known_numbers = {
            "15555215554", "15555215556",
            "15555215558", "15555215560", "15555215562", "15555215564",
            "15555215566", "15555215568", "15555215570", "15555215572",
            "15555215574", "15555215576", "15555215578", "15555215580",
            "15555215582", "15555215584",
    };
    private static String[] known_device_ids = {
            "000000000000000"
    };
    private static String[] known_imsi_ids = {
            "310260000000000"
    };

    // 检测模拟器上的特有几个文件
    public static boolean checkEmulatorFiles(){
        for (int i = 0; i < known_files.length; i++){
            String fileName = known_files[i];
            File qemu_file = new File(fileName);
            if (qemu_file.exists()){
                Log.v("Result:", "Find Emulator files.");
                return true;
            }
        }
        Log.v("Result:", "Not Find Emulator files.");
        return false;
    }

    // 检测模拟器默认的电话号码
    public static boolean checkPhoneNumber(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"MissingPermission"}) String phoneNumber = telephonyManager.getLine1Number();
        for (String known_number : known_numbers) {
            if (known_number.equalsIgnoreCase(phoneNumber)){
                Log.v("Result:", "Find Emulator PhoneNumber.");
                return true;
            }
        }

        Log.v("Result:", "Not Find Emulator PhoneNumber.");
        return false;
    }

    // 检测设备的IDS是不是“000000000000000”
    public static boolean checkDeviceIds(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"MissingPermission"}) String deviceIds = telephonyManager.getDeviceId();
        for (String deviceId : known_device_ids) {
            if (deviceId.equalsIgnoreCase(deviceIds)){
                Log.v("Result:", "Find ids: 000000000000000.");
                return true;
            }
        }

        Log.v("Result:", "Not Find  ids: 000000000000000.");
        return false;
    }

    // 检测设备的Imsi id是不是“310260000000000”
    public static boolean checkIMSIIds(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"MissingPermission"}) String imsiIds = telephonyManager.getSubscriberId();
        for (String imsiId : known_imsi_ids) {
            if (imsiId.equalsIgnoreCase(imsiIds)){
                Log.v("Result:", "Find imsi ids: 310260000000000.");
                return true;
            }
        }

        Log.v("Result:", "Not Find imsi ids: 310260000000000.");
        return false;
    }

    // 检测手机上的一些硬件信息
    public static boolean checkEmulatorBuild(Context context){
        String BOARD = Build.BOARD;
        String BOOTLOADER = Build.BOOTLOADER;
        String BRAND = Build.BRAND;
        String DEVICE = Build.DEVICE;
        String HARDWARE = Build.HARDWARE;
        String MODEL = Build.MODEL;
        String PRODUCT = Build.PRODUCT;

        if (BOARD == "unknown" || BOOTLOADER == "unknown"
                || BRAND == "generic" || DEVICE == "generic"
                || MODEL == "sdk" || PRODUCT == "sdk"
                || HARDWARE == "goldfish")
        {
            Log.v("Result:", "Find Emulator by EmulatorBuild!");
            return true;
        }
        Log.v("Result:", "Not Find Emulator by EmulatorBuild!");
        return false;
    }

    // 检测手机运营商
    public static boolean checkOperatorNameAndroid(Context context){
        String szOperatorName = ((TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
        if (szOperatorName.equalsIgnoreCase("android")){
            Log.v("Result:", "Find Emulator by OperatorName!");
            return true;
        }
        Log.v("Result:", "Not Find Emulator by OperatorName!");
        return true;
    }
}
