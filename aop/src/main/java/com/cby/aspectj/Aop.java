package com.cby.aspectj;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.cby.aspectj.common.Interceptor;
import com.cby.aspectj.util.DebugLog;
import com.cby.aspectj.util.PermissionUtils;

public class Aop {

    private static Context mContext;
    private static Interceptor mInterceptor;
    /**
     * 权限申请被拒绝的监听
     */
    private static PermissionUtils.OnPermissionDeniedListener sOnPermissionDeniedListener;

    public static void init(Application application) {
        mContext = application.getApplicationContext();
    }

    public static void setDebug(boolean isDebug){
        new DebugLog(isDebug);
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        testInitialize();
        return mContext;
    }

    private static void testInitialize() {
        if (mContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 Aop.init() 初始化！");
        }
    }


    public static Interceptor getInterceptor() {
        return mInterceptor;
    }

    public static void setInterceptor(@NonNull Interceptor interceptor) {
        mInterceptor = interceptor;
    }

    /**
     * 设置权限申请被拒绝的监听
     *
     * @param listener 权限申请被拒绝的监听器
     */
    public static void setOnPermissionDeniedListener(@NonNull PermissionUtils.OnPermissionDeniedListener listener) {
        Aop.sOnPermissionDeniedListener = listener;
    }

    public static PermissionUtils.OnPermissionDeniedListener getOnPermissionDeniedListener() {
        return sOnPermissionDeniedListener;
    }
}
