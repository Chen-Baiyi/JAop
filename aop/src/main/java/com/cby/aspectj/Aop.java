package com.cby.aspectj;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;

import com.cby.aspectj.common.Interceptor;
import com.cby.aspectj.util.DebugLog;
import com.cby.aspectj.util.PermissionUtils;

import java.lang.ref.WeakReference;

public class Aop {
    private static Interceptor mInterceptor;
    private static WeakReference<Context> mContextWeakReference;

    /**
     * 权限申请被拒绝的监听
     */
    private static PermissionUtils.OnPermissionDeniedListener sOnPermissionDeniedListener;

    public static void init(Application application) {
        mContextWeakReference = new WeakReference<>(application.getApplicationContext());
    }

    public static void setDebug(boolean isDebug){
        new DebugLog(isDebug);
    }

    /**
     * 获取全局上下文
     *
     * @return 上下文对象
     */
    public static Context getContext() {
        testInitialize();
        return mContextWeakReference.get();
    }

    private static void testInitialize() {
        if (mContextWeakReference.get() == null) {
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
