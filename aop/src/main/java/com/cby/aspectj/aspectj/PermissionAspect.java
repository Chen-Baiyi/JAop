package com.cby.aspectj.aspectj;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.cby.aspectj.Aop;
import com.cby.aspectj.annotation.JPermission;
import com.cby.aspectj.util.PermissionUtils;
import com.cby.aspectj.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

@Aspect
public class PermissionAspect {

    /**
     * 带有 JIntercept 注解的所有连接点
     */
    @Pointcut("within(@com.cby.aspectj.annotation.JPermission *)")
    public void withinAnnotatedClass() {
    }

    /**
     * 带有 JIntercept 注解的所有类，除去 synthetic 修饰的方法
     */
    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    /**
     * 带有 JIntercept 注解的所有类，除去 synthetic 修饰的构造方法
     */
    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    /**
     * 带有 JIntercept 注解的方法
     */
    @Pointcut("execution(@com.cby.aspectj.annotation.JPermission * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }

    /**
     * 带有 JIntercept 注解的构造方法
     */
    @Pointcut("execution(@com.cby.aspectj.annotation.JPermission *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    }

    /**
     * 执行
     * 【注】joinPoint.proceed() 表示执行原方法
     *
     * Before 在原方法执行之前执行要插入的代码
     * After 在原方法执行之后执行要插入的代码
     * AfterReturning 在原方法执行后，返回一个结果再执行，如果没结果，用此修辞符修辞是不会执行的
     * AfterThrowing 在原方法执行过程中抛出异常后执行，也就是方法执行过程中，如果抛出异常后，才会执行此切面方法。
     * Around 在原方法执行前后和抛出异常时执行（前面几种通知的综合）
     */
    @Around("(method() || constructor()) && @annotation(permission)")    // 在连接点进行方法替换
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, JPermission permission) throws Throwable {
        FragmentManager fragmentManager = null;
        // 根据当前上下文对象是，获取对应的 FragmentManager 对象
        if (joinPoint.getThis() instanceof Fragment) {
            // fragment
            Fragment fragment = (Fragment) joinPoint.getThis();
            fragmentManager = fragment.getChildFragmentManager();
        } else if (joinPoint.getThis() instanceof FragmentActivity) {
            // activity
            FragmentActivity activity = (FragmentActivity) joinPoint.getThis();
            fragmentManager = activity.getSupportFragmentManager();
        }
        // 请求权限
        PermissionUtils.permission(permission.value())
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        Log.e("aop -> ", "权限申请被拒绝:" + Utils.listToString(permissionsDenied));
                        if (Aop.getOnPermissionDeniedListener() != null) {
                            Aop.getOnPermissionDeniedListener().onDenied(permissionsDenied);
                        }
                    }
                })
                .request(fragmentManager);
    }
}
