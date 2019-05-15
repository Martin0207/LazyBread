package com.martin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/4 0004
 * @description 实现Fragment的懒加载功能
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LazyInit {

    /**
     * 是否重复
     * 默认情况下，Fragment只需要调用一次初始化方法
     * @return 默认不重复
     */
    boolean isCycle() default false;

    /**
     * 优先值
     * 同一个Fragment中，若拥有多个方法被注解，
     * 则按照优先值由大到小的顺序调用
     * @return 默认为1
     */
    int priority() default 1;

}
