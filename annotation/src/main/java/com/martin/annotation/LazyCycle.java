package com.martin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/15 0015
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LazyCycle {

    /**
     * 循环间隔
     * 单位: 毫秒
     */
    int interval();

    /**
     * 循环次数
     * times <= 0 时,为无限循环
     */
    int times() default 0;

}
