package com.martin.core.interfaces;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/15 0015
 */
public interface OnCycleCallBackListener {

    /**
     * 当循环调用回调
     *
     * @param key 回调标识
     * @return 是否继续循环
     */
    boolean onCallBack(int key);

}
