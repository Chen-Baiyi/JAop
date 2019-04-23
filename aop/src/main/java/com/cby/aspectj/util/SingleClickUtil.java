package com.cby.aspectj.util;

import android.os.SystemClock;
import android.view.View;

public class SingleClickUtil {

    private static long mLastClickTime;
    private static int mLastClickViewId;


    /**
     * 是否快速点击
     *
     * @param view           点击的控件
     * @param intervalMillis 时间间隔（毫秒）
     * @return true：是；false：不是
     */
    public static boolean isFastDoubleClick(View view, long intervalMillis) {
        int viewId = view.getId();
        long time = SystemClock.elapsedRealtime();
        long timeInerval = Math.abs(time - mLastClickTime);
        if (timeInerval < intervalMillis && viewId == mLastClickViewId) {
            return true;
        } else {
            mLastClickTime = time;
            mLastClickViewId = viewId;
            return false;
        }
    }
}
