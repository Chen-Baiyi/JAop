package com.cby.aspectj.aspectj;

import android.util.Log;

import com.cby.aspectj.Aop;
import com.cby.aspectj.annotation.Intercept;
import com.cby.aspectj.common.Constant;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class InterceptAspect {

    // 任意连接点：包括类/对象初始化块,field,方法,构造器
    // @Pointcut 切入点

    /**
     * 带有 Intercept 注解的所有连接点
     */
    @Pointcut("within(@com.cby.aspectj.annotation.Intercept *)")
    public void withinAnnotatedClass() {
    }

    /**
     * 带有 Intercept 注解的所有类，除去 synthetic 修饰的方法
     */
    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    /**
     * 带有 Intercept 注解的所有类，除去 synthetic 修饰的构造方法
     */
    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    /**
     * 带有 Intercept 注解的方法
     */
    @Pointcut("execution(@com.cby.aspectj.annotation.Intercept * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }

    /**
     * 带有 Intercept 注解的构造方法
     */
    @Pointcut("execution(@com.cby.aspectj.annotation.Intercept *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    }

    /**
     * 执行
     * 【注】joinPoint.proceed() 表示执行原方法
     *
     * @Before 在原方法执行之前执行要插入的代码
     * @After 在原方法执行之后执行要插入的代码
     * @AfterReturning 在原方法执行后，返回一个结果再执行，如果没结果，用此修辞符修辞是不会执行的
     * @AfterThrowing 在原方法执行过程中抛出异常后执行，也就是方法执行过程中，如果抛出异常后，才会执行此切面方法。
     * @Around 在原方法执行前后和抛出异常时执行（前面几种通知的综合）
     */
    @Around("(method() || constructor()) && @annotation(intercept)")    // 在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, Intercept intercept) throws Throwable {
        if (Aop.getInterceptor() == null) return joinPoint.proceed(); // 没有拦截器不执行切片拦截
        // 执行拦截操作
        boolean result = proceedIntercept(intercept.value(), joinPoint);
        Log.d(Constant.TAG, "拦截结果:" + result + ", 切片" + (result ? "被拦截！" : "正常执行！"));
        return result ? null : joinPoint.proceed();
    }

    /**
     * 执行拦截操作
     *
     * @param types     拦截的类型集合
     * @param joinPoint 切片
     * @return {@code true}: 拦截切片的执行 <br>{@code false}: 不拦截切片的执行
     */
    private boolean proceedIntercept(int[] types, JoinPoint joinPoint) throws Throwable {
        for (int type : types) {
            if (Aop.getInterceptor().intercept(type, joinPoint)) { // 拦截执行
                return true;
            }
        }
        return false;
    }

}