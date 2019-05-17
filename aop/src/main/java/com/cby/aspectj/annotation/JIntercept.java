package com.cby.aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义拦截注解
 */
@Retention(RetentionPolicy.RUNTIME) // 作用于：运行时
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})   //作用于：字段声明、方法、类
public @interface JIntercept {

    /**
     * @return 拦截类型
     */
    int[] value();
}
