package com.example.androidtrain.util;

import android.util.Log;

/**
 * 日志管理工具类
 * Created by sx on 2018/12/27.
 */

public enum LogUtil {
    INSTANCE;
    public int VERBOSE_LEVEL = 0;
    public int DEBUG_LEVEL = 1;
    public int INFO_LEVEL = 2;
    public int WARN_LEVEL = 3;
    public int ERROR_LEVEL = 4;
    public int NO_SHOW_LEVEL = 99;
    private int LEVEL = DEBUG_LEVEL;

    public void setLevel(int level){
        LEVEL = level;
    }

    public void setNoShowLog(){
        setLevel(NO_SHOW_LEVEL);
    }

    public void v(String tag, String msg){
        if (VERBOSE_LEVEL >= LEVEL){
            Log.v(tag, msg);
        }
    }

    public void d(String tag, String msg){
        if (DEBUG_LEVEL >= LEVEL){
            Log.d(tag, msg);
        }
    }

    public void i(String tag, String msg){
        if (INFO_LEVEL >= LEVEL){
            Log.i(tag, msg);
        }
    }

    public void w(String tag, String msg){
        if (WARN_LEVEL >= LEVEL){
            Log.w(tag, msg);
        }
    }

    public void e(String tag, String msg){
        if (ERROR_LEVEL >= LEVEL){
            Log.e(tag, msg);
        }
    }
}
