package com.cby.aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: JAop
 * @Package: com.cby.aspectj.annotation
 * @ClassName: JTrace
 * @Description: 性能监测
 * @Author: 陈白衣
 * @CreateDate: 2019/8/8 1:11
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/8 1:11
 * @UpdateRemark: 更新说明
 * @Version: 1.0.5
 */

@Retention(RetentionPolicy.RUNTIME) // 作用于：运行时
@Target({ElementType.METHOD, ElementType.TYPE})   //作用于：方法、类
public @interface JTrace {
}
