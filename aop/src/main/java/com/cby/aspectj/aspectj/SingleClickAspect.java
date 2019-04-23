package com.cby.aspectj.aspectj;

import android.util.Log;
import android.view.View;

import com.cby.aspectj.common.Constant;
import com.cby.aspectj.annotation.SingleClick;
import com.cby.aspectj.util.SingleClickUtil;
import com.cby.aspectj.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SingleClickAspect {
    private static final long DEFAULT_TIME_INTERVAL = 5000;


    @Pointcut("within(@com.cby.aspectj.annotation.SingleClick *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    /**
     * 定义切点，标记切点为所有被@SingleClick注解的方法
     * 注意：execution(@注解类的全路径)
     */
    @Pointcut("execution(@com.cby.aspectj.annotation.SingleClick * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  // 方法切入点


    /**
     * 定义一个切面方法，包裹切点方法
     */
    @Around("method() && @annotation(singleClick)") // 在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, SingleClick singleClick) throws Throwable {
        // 取出方法的参数
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) {
                view = (View) arg;
                break;
            }
        }
        if (view == null) {
            return;
        }
        // 取出方法的注解
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        if (!method.isAnnotationPresent(SingleClick.class)) {
//            return;
//        }
//        SingleClick singleClick = method.getAnnotation(SingleClick.class);
        // 判断是否快速点击
        if (!SingleClickUtil.isFastDoubleClick(view, singleClick.value())) {
            // 不是快速点击，执行原方法
            joinPoint.proceed();
        } else {
            Log.d(Constant.TAG, Utils.getMethodDescribeInfo(joinPoint) + ":发生快速点击，View id:" + view.getId());
        }
    }
}