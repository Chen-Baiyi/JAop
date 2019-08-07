package com.cby.aspectj.util;

import android.util.Log;

/**
 * @ProjectName: JAop
 * @Package: com.cby.aspectj.util
 * @ClassName: DebugLog
 * @Description: 日志打印
 * @Author: 陈白衣
 * @CreateDate: 2019/8/8 2:25
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/8 2:25
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
public class DebugLog {
    public static String LOG_TAG = "JAop --> ";
    private static boolean isDebug = false;

    public DebugLog(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public static void d(String message) {
        d(LOG_TAG, message);
    }

    public static void d(String tag, String message) {
        if (isDebug) {
            Log.d(tag, message);
        }
    }

    public static void v(String message) {
        v(LOG_TAG, message);
    }

    public static void v(String tag, String message) {
        if (isDebug) {
            Log.v(tag, message);
        }
    }

    public static void w(String message) {
        w(LOG_TAG, message);
    }

    public static void w(String tag, String message) {
        if (isDebug) {
            Log.w(tag, message);
        }
    }

    public static void e(String message) {
        e(LOG_TAG, message);
    }

    public static void e(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }

    public static void i(String message) {
        i(LOG_TAG, message);
    }

    public static void i(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }
}
