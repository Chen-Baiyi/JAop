package com.cby.aspectj.aspectj;

import com.cby.aspectj.annotation.JPermission;
import com.cby.aspectj.annotation.JTrace;
import com.cby.aspectj.util.DebugLog;
import com.cby.aspectj.util.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @ProjectName: JAop
 * @Package: com.cby.aspectj.aspectj
 * @ClassName: TraceAspect
 * @Description: java 类作用描述
 * @Author: 陈白衣
 * @CreateDate: 2019/8/8 1:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/8 1:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
@Aspect
public class TraceAspect {

    /**
     * 带有 JIntercept 注解的所有连接点
     */
    @Pointcut("within(@com.cby.aspectj.annotation.JTrace *)")
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
    @Pointcut("execution(@com.cby.aspectj.annotation.JTrace * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }

    /**
     * 带有 JIntercept 注解的构造方法
     */
    @Pointcut("execution(@com.cby.aspectj.annotation.JTrace *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    }

    /**
     * 执行
     * 【注】joinPoint.proceed() 表示执行原方法
     * <p>
     * Before 在原方法执行之前执行要插入的代码
     * After 在原方法执行之后执行要插入的代码
     * AfterReturning 在原方法执行后，返回一个结果再执行，如果没结果，用此修辞符修辞是不会执行的
     * AfterThrowing 在原方法执行过程中抛出异常后执行，也就是方法执行过程中，如果抛出异常后，才会执行此切面方法。
     * Around 在原方法执行前后和抛出异常时执行（前面几种通知的综合）
     */
    @Around("(method() || constructor()) && @annotation(trace)")    // 在连接点进行方法替换
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, JTrace trace) throws Throwable {
        // 实例化计时器
        StopWatch stopWatch = new StopWatch();
        // 开始计时
        stopWatch.start();
        // 执行原方法
        Object result = joinPoint.proceed();
        // 结束计时
        stopWatch.stop();

        // 获取方法信息对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取当前对象
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        // 打印日志
        DebugLog.d(className, buildLogMessage(methodName, stopWatch.getTotalTimeMillis()));
        return result;
    }

    /**
     * @return 消息
     * @description 拼接 log 消息
     * @date: 2019/8/8 2:29
     * @author: 陈白衣
     */
    private String buildLogMessage(String methodName, long totalTimeMillis) {
        StringBuilder message = new StringBuilder();
        message.append("JTrace --> ")
                .append(methodName)
                .append(" [")
                .append(totalTimeMillis)
                .append("ms]");
        return message.toString();
    }
}
