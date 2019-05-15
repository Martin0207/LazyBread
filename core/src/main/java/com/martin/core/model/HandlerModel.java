package com.martin.core.model;

import android.os.Handler;
import android.os.Message;

import com.martin.core.interfaces.OnCycleCallBackListener;

import java.util.List;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/6 0006
 */
public class HandlerModel {

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (mListener.onCallBack(msg.what)) {
                mHandler.sendEmptyMessageDelayed(msg.what, msg.what);
            }
            return true;
        }
    });

    /**
     * 循环间隔集合
     */
    private List<Integer> mList;

    private OnCycleCallBackListener mListener;

    public HandlerModel(List<Integer> list, OnCycleCallBackListener listener) {
        mList = list;
        mListener = listener;
    }

    /**
     * 开启循环
     */
    public void resume() {
        for (int key : mList) {
            mHandler.sendEmptyMessageDelayed(key, key);
        }
    }

    /**
     * 停止循环
     */
    public void pause() {
        for (int key : mList) {
            mHandler.removeMessages(key);
        }
    }

}
