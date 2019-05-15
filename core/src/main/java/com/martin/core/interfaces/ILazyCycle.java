package com.martin.core.interfaces;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/15 0015
 */
public interface ILazyCycle<T> {

    /**
     * 初始化
     */
    void init(T t);

    /**
     * 停止循环
     */
    void resume();

    /**
     * 开始循环
     */
    void pause();

}
