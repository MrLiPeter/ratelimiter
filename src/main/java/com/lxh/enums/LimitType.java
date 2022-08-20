package com.lxh.enums;

/**
 * 限流类型的枚举类
 */
public enum LimitType {
    /**
     * 默认的限流策略，针对某一个接口进行限流
     */
    DEFAULT,

    /**
     * 针对某一个IP进行限流
     */
    IP
}
