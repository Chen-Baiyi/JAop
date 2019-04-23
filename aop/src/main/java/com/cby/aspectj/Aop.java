package com.cby.aspectj;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.cby.aspectj.common.Interceptor;

public class Aop {

    private static Context mContext;
    private static Interceptor mInterceptor;

    public static void init(Application application) {
        mContext = application.getApplicationContext();
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
}