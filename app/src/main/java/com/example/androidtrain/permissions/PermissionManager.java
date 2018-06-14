package com.example.androidtrain.permissions;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.AppOpsManagerCompat;

import java.lang.reflect.Method;

/**
 * Created by sx on 2018/6/14.
 */

public enum  PermissionManager {

    INSTANCE;

    /**
     * All come from AppOpsManager, can go and read the source code
     */
    /**   No operation specified. */
    public static final int OP_NONE = -1;
    /**   Access to coarse location information. */
    public static final int OP_COARSE_LOCATION = 0;
    /**   Access to fine location information. */
    public static final int OP_FINE_LOCATION = 1;
    /**   Causing GPS to run. */
    public static final int OP_GPS = 2;
    /**   */
    public static final int OP_VIBRATE = 3;
    /**   */
    public static final int OP_READ_CONTACTS = 4;
    /**   */
    public static final int OP_WRITE_CONTACTS = 5;
    /**   */
    public static final int OP_READ_CALL_LOG = 6;
    /**   */
    public static final int OP_WRITE_CALL_LOG = 7;
    /**   */
    public static final int OP_READ_CALENDAR = 8;
    /**   */
    public static final int OP_WRITE_CALENDAR = 9;
    /**   */
    public static final int OP_WIFI_SCAN = 10;
    /**   */
    public static final int OP_POST_NOTIFICATION = 11;
    /**   */
    public static final int OP_NEIGHBORING_CELLS = 12;
    /**   */
    public static final int OP_CALL_PHONE = 13;
    /**   */
    public static final int OP_READ_SMS = 14;
    /**   */
    public static final int OP_WRITE_SMS = 15;
    /**   */
    public static final int OP_RECEIVE_SMS = 16;
    /**   */
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    /**   */
    public static final int OP_RECEIVE_MMS = 18;
    /**   */
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    /**   */
    public static final int OP_SEND_SMS = 20;
    /**   */
    public static final int OP_READ_ICC_SMS = 21;
    /**   */
    public static final int OP_WRITE_ICC_SMS = 22;
    /**   */
    public static final int OP_WRITE_SETTINGS = 23;
    /**   */
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    /**   */
    public static final int OP_ACCESS_NOTIFICATIONS = 25;
    /**   */
    public static final int OP_CAMERA = 26;
    /**   */
    public static final int OP_RECORD_AUDIO = 27;
    /**   */
    public static final int OP_PLAY_AUDIO = 28;
    /**   */
    public static final int OP_READ_CLIPBOARD = 29;
    /**   */
    public static final int OP_WRITE_CLIPBOARD = 30;
    /**   */
    public static final int OP_TAKE_MEDIA_BUTTONS = 31;
    /**   */
    public static final int OP_TAKE_AUDIO_FOCUS = 32;
    /**   */
    public static final int OP_AUDIO_MASTER_VOLUME = 33;
    /**   */
    public static final int OP_AUDIO_VOICE_VOLUME = 34;
    /**   */
    public static final int OP_AUDIO_RING_VOLUME = 35;
    /**   */
    public static final int OP_AUDIO_MEDIA_VOLUME = 36;
    /**   */
    public static final int OP_AUDIO_ALARM_VOLUME = 37;
    /**   */
    public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    /**   */
    public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    /**   */
    public static final int OP_WAKE_LOCK = 40;
    /**   Continually monitoring location data. */
    public static final int OP_MONITOR_LOCATION = 41;
    /**   Continually monitoring location data with a relatively high power request. */
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    /**   Retrieve current usage stats via {}. */
    public static final int OP_GET_USAGE_STATS = 43;
    /**   */
    public static final int OP_MUTE_MICROPHONE = 44;
    /**   */
    public static final int OP_TOAST_WINDOW = 45;
    /**   Capture the device's display contents and/or audio */
    public static final int OP_PROJECT_MEDIA = 46;
    /**   Activate a VPN connection without user intervention. */
    public static final int OP_ACTIVATE_VPN = 47;
    /**   Access the WallpaperManagerAPI to write wallpapers. */
    public static final int OP_WRITE_WALLPAPER = 48;
    /**   Received the assist structure from an app. */
    public static final int OP_ASSIST_STRUCTURE = 49;
    /**   Received a screenshot from assist. */
    public static final int OP_ASSIST_SCREENSHOT = 50;
    /**   Read the phone state. */
    public static final int OP_READ_PHONE_STATE = 51;
    /**   Add voicemail messages to the voicemail content provider. */
    public static final int OP_ADD_VOICEMAIL = 52;
    /**   Access APIs for SIP calling over VOIP or WiFi. */
    public static final int OP_USE_SIP = 53;
    /**   Intercept outgoing calls. */
    public static final int OP_PROCESS_OUTGOING_CALLS = 54;
    /**   User the fingerprint API. */
    public static final int OP_USE_FINGERPRINT = 55;
    /**   Access to body sensors such as heart rate, etc. */
    public static final int OP_BODY_SENSORS = 56;
    /**   Read previously received cell broadcast messages. */
    public static final int OP_READ_CELL_BROADCASTS = 57;
    /**   Inject mock location into the system. */
    public static final int OP_MOCK_LOCATION = 58;
    /**   Read external storage. */
    public static final int OP_READ_EXTERNAL_STORAGE = 59;
    /**   Write external storage. */
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    /**   Turned on the screen. */
    public static final int OP_TURN_SCREEN_ON = 61;
    /**   Get device accounts. */
    public static final int OP_GET_ACCOUNTS = 62;
    /**   Control whether an application is allowed to run in the background. */
    public static final int OP_RUN_IN_BACKGROUND = 63;
    /**   */
    public static final int OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    /**   Read the phone number. */
    public static final int OP_READ_PHONE_NUMBERS = 65;
    /**   Request package installs through package installer */
    public static final int OP_REQUEST_INSTALL_PACKAGES = 66;
    /**   Enter picture-in-picture. */
    public static final int OP_PICTURE_IN_PICTURE = 67;
    /**   Instant app start foreground service. */
    public static final int OP_INSTANT_APP_START_FOREGROUND = 68;
    /**   Answer incoming phone calls */
    public static final int OP_ANSWER_PHONE_CALLS = 69;
    /**   */
    public static final int _NUM_OP = 70;

    /** Access to coarse location information. */
    public static final String OPSTR_COARSE_LOCATION = "android:coarse_location";
    /** Access to fine location information. */
    public static final String OPSTR_FINE_LOCATION =
            "android:fine_location";
    /** Continually monitoring location data. */
    public static final String OPSTR_MONITOR_LOCATION
            = "android:monitor_location";
    /** Continually monitoring location data with a relatively high power request. */
    public static final String OPSTR_MONITOR_HIGH_POWER_LOCATION
            = "android:monitor_location_high_power";
    /** Access to {@link android.app.usage.UsageStatsManager}. */
    public static final String OPSTR_GET_USAGE_STATS
            = "android:get_usage_stats";
    /** Activate a VPN connection without user intervention. @hide */
    public static final String OPSTR_ACTIVATE_VPN
            = "android:activate_vpn";
    /** Allows an application to read the user's contacts data. */
    public static final String OPSTR_READ_CONTACTS
            = "android:read_contacts";
    /** Allows an application to write to the user's contacts data. */
    public static final String OPSTR_WRITE_CONTACTS
            = "android:write_contacts";
    /** Allows an application to read the user's call log. */
    public static final String OPSTR_READ_CALL_LOG
            = "android:read_call_log";
    /** Allows an application to write to the user's call log. */
    public static final String OPSTR_WRITE_CALL_LOG
            = "android:write_call_log";
    /** Allows an application to read the user's calendar data. */
    public static final String OPSTR_READ_CALENDAR
            = "android:read_calendar";
    /** Allows an application to write to the user's calendar data. */
    public static final String OPSTR_WRITE_CALENDAR
            = "android:write_calendar";
    /** Allows an application to initiate a phone call. */
    public static final String OPSTR_CALL_PHONE
            = "android:call_phone";
    /** Allows an application to read SMS messages. */
    public static final String OPSTR_READ_SMS
            = "android:read_sms";
    /** Allows an application to receive SMS messages. */
    public static final String OPSTR_RECEIVE_SMS
            = "android:receive_sms";
    /** Allows an application to receive MMS messages. */
    public static final String OPSTR_RECEIVE_MMS
            = "android:receive_mms";
    /** Allows an application to receive WAP push messages. */
    public static final String OPSTR_RECEIVE_WAP_PUSH
            = "android:receive_wap_push";
    /** Allows an application to send SMS messages. */
    public static final String OPSTR_SEND_SMS
            = "android:send_sms";
    /** Required to be able to access the camera device. */
    public static final String OPSTR_CAMERA
            = "android:camera";
    /** Required to be able to access the microphone device. */
    public static final String OPSTR_RECORD_AUDIO
            = "android:record_audio";
    /** Required to access phone state related information. */
    public static final String OPSTR_READ_PHONE_STATE
            = "android:read_phone_state";
    /** Required to access phone state related information. */
    public static final String OPSTR_ADD_VOICEMAIL
            = "android:add_voicemail";
    /** Access APIs for SIP calling over VOIP or WiFi */
    public static final String OPSTR_USE_SIP
            = "android:use_sip";
    /** Access APIs for diverting outgoing calls */
    public static final String OPSTR_PROCESS_OUTGOING_CALLS
            = "android:process_outgoing_calls";
    /** Use the fingerprint API. */
    public static final String OPSTR_USE_FINGERPRINT
            = "android:use_fingerprint";
    /** Access to body sensors such as heart rate, etc. */
    public static final String OPSTR_BODY_SENSORS
            = "android:body_sensors";
    /** Read previously received cell broadcast messages. */
    public static final String OPSTR_READ_CELL_BROADCASTS
            = "android:read_cell_broadcasts";
    /** Inject mock location into the system. */
    public static final String OPSTR_MOCK_LOCATION
            = "android:mock_location";
    /** Read external storage. */
    public static final String OPSTR_READ_EXTERNAL_STORAGE
            = "android:read_external_storage";
    /** Write external storage. */
    public static final String OPSTR_WRITE_EXTERNAL_STORAGE
            = "android:write_external_storage";
    /** Required to draw on top of other apps. */
    public static final String OPSTR_SYSTEM_ALERT_WINDOW
            = "android:system_alert_window";
    /** Required to write/modify/update system settingss. */
    public static final String OPSTR_WRITE_SETTINGS
            = "android:write_settings";
    /** @hide Get device accounts. */
    public static final String OPSTR_GET_ACCOUNTS
            = "android:get_accounts";
    public static final String OPSTR_READ_PHONE_NUMBERS
            = "android:read_phone_numbers";
    /** Access to picture-in-picture. */
    public static final String OPSTR_PICTURE_IN_PICTURE
            = "android:picture_in_picture";
    /** @hide */
    public static final String OPSTR_INSTANT_APP_START_FOREGROUND
            = "android:instant_app_start_foreground";
    /** Answer incoming phone calls */
    public static final String OPSTR_ANSWER_PHONE_CALLS
            = "android:answer_phone_calls";

    //测试是否有该权限
    public boolean opsCheckPermission(Context context, int op, String opString){

        return true;
    }


    /**
     * 检查权限列表
     *
     * @param context
     * @param op
     * 这个值被hide了，去AppOpsManager类源码找，如位置权限  AppOpsManager.OP_GPS==2
     * @param opString
     *   如判断定位权限 AppOpsManager.OPSTR_FINE_LOCATION
     * @return  @see 如果返回值 AppOpsManagerCompat.MODE_IGNORED 表示被禁用了

     */
    public static int checkOp(Context context, int op, String opString) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
//            Object object = context.getSystemService("appops");
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                if (Build.VERSION.SDK_INT >= 23) {
                    return AppOpsManagerCompat.noteOp(context, opString,context.getApplicationInfo().uid,
                            context.getPackageName());
                }

            }
        }
        return -1;
    }
}
