package com.martin.annotation.model;


/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/15 0015
 */
public class CycleModel implements Comparable<CycleModel> {
    /**
     * 循环间隔
     */
    public int interval;

    /**
     * 循环次数
     * <=0 时为无限循环
     */
    public int times;

    /**
     * 已完成次数
     * 无限循环时不用
     */
    public int completionTimes = 0;

    public CycleModel(int interval, int times) {
        this.interval = interval;
        this.times = times;
    }

    @Override
    public int compareTo(CycleModel o) {
        return interval-o.interval;
    }
}
