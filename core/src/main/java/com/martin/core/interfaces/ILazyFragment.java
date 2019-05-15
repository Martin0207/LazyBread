package com.martin.core.interfaces;

import android.support.v4.app.Fragment;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/5 0005
 */
public interface ILazyFragment<T extends Fragment> {

    /**
     * 初始化
     */
    void init(T fragment);

    /**
     * 调用
     */
    void call();

    /**
     * 重置
     * 在Fragment被销毁视图时重置
     */
    void reset();

}
