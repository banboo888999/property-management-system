package com.tianyu.property.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    /**
     * 所需权限标识
     */
    String[] value();

    /**
     * 是否需要所有权限（默认任一权限即可）
     */
    boolean requireAll() default false;
}