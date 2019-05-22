package com.cby.aspectj.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;

/**
 * @Desc 定义多个 @Aspect 的顺序（优先级）
 * @Author cby
 * @Time 2019/5/22 11:41
 */
@Aspect
@DeclarePrecedence("com.cby.aspectj.aspectj.SingleClickAspect," +
        "com.cby.aspectj.aspectj.PermissionAspect," +
        "com.cby.aspectj.aspectj.InterceptAspect")
public class Precedence {
}
