package com.cby.jaop;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.cby.aspectj.Aop;
import com.cby.aspectj.util.Utils;

@SuppressLint("Registered")
public class JApplication extends Application {
    private JApplication instance;
    boolean isLogin = false;


    public interface InterceptorType {
        int TYPE_0 = 0;
        int TYPE_1 = 1;
        int TYPE_2 = 2;
        int TYPE_3 = 3;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 初始化 Aop
        initAop();
    }

    /**
     * 初始化 Aop
     */
    private void initAop() {
        // 初始化 aop
        Aop.init(this);
        // 配置拦截操作，拦截成功时 return true，否则 return false。
        Aop.setInterceptor((type, joinPoint) -> {
            switch (type) {
                case InterceptorType.TYPE_0:
                    // 未登陆拦截前往登录
                    if (!isLogin) {
                        Intent intent = new Intent(instance.getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    }
                case InterceptorType.TYPE_1:
                    // 拦截
                    Toast.makeText(instance, "拦截消息", Toast.LENGTH_SHORT).show();
                    return true;
                case InterceptorType.TYPE_2:
                    // 拦截

                    break;
                case InterceptorType.TYPE_3:
                    // 拦截

                    break;
            }
            return false;
        });
        // 配置权限拒绝的操作
        Aop.setOnPermissionDeniedListener(permissionsDenied ->
                Toast.makeText(instance, "拒绝权限 -> " + Utils.listToString(permissionsDenied), Toast.LENGTH_SHORT).show());
    }
}
