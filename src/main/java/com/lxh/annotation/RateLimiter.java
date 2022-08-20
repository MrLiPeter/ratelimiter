package com.lxh.annotation;

import com.lxh.enums.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiter {
    /**
     * 限流的key，主要是指前缀
     */
    String key() default "rate_limit";

    /**
     * 限流时间窗 默认60s
     * @return
     */
    int time() default 60;

    /**
     * 在时间窗内的限流次数
     * @return
     */
    int count() default 100;

    /**
     * 限流类型
     * return
     */
    LimitType limitType() default LimitType.DEFAULT;
}
