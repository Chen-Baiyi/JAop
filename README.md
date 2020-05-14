# JAop

#### 介绍
你可以通过添加注解的方式实现以下功能：
1. 点击事件防抖
2. 拦截
3. 动态权限请求
4. 性能监控

易适配 AndroidX

#### 依赖

1. 在项目下的 build.gradle 中添加如下依赖
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        // aspectj 插件
        classpath 'com.cby.aop:aspectj-plugin:1.0.0'
    }
}

allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}	
```
2. 在 module 下的 build.gradle 中
```
// aspectj 插件
apply plugin: 'aspectj-plugin'
dependencies {
    // androidx 版
    implementation 'com.github.Chen-Baiyi:JAop:1.1.0'
    // support 版
    // implementation 'com.github.Chen-Baiyi:JAop:1.0.6'
}
```

#### 使用

1. 在Application的onCreate里初始化
```
    /**
     * 初始化 Aop
     */
    private void initAop() {
        // 初始化 aop
        Aop.init(this);
        // 开启 debug 模式
        Aop.setDebug(true);
        // 配置拦截操作，拦截成功时 return true，否则 return false。
        Aop.setInterceptor((type, joinPoint) -> {
            switch (type) {
                case 0:
                    // 未登录拦截，前往登录
                    if (!isLogin) {
                        Intent intent = new Intent(instance.getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    }
                case 1:
                    // 拦截
                    Toast.makeText(instance, "拦截消息", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        });
        // 配置权限拒绝的操作
        Aop.setOnPermissionDeniedListener(permissionsDenied -> {
            Toast.makeText(instance, "拒绝权限 -> " + Utils.listToString(permissionsDenied), Toast.LENGTH_SHORT).show();
        });
    }
```
2. 单击事件防抖
在点击事件方法上添加注解 @JSingleClick
```
    // 默认1000ms
    @JSingleClick(2000)
    public void onClick(View v) {
        
    }
```
3. 权限申请
在对应的方法上添加注解 @JPermission(String[])
```
    @JPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE})
    private void method() {
    }
```
4. 拦截
在对应的方法上添加注解 @JIntercept(int[])
```
    @JIntercept(0)
    private void method() {
    }
```
5. 性能监控
在对应的方法上添加注解 @JTrace
```
    @JTrace
    private void method() {
    }
```

#### 混淆
```
-keep @com.cby.aspectj.annotation.* class * {*;}
-keep class * {
    @com.cby.aspectj.annotation.* <fields>;
}
-keepclassmembers class * {
    @com.cby.aspectj.annotation.* <methods>;
}
```

[![](https://jitpack.io/v/Chen-Baiyi/JAop.svg)](https://jitpack.io/#Chen-Baiyi/JAop)
