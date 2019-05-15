package com.martin.lazybread.lazyinit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martin.annotation.LazyInit;
import com.martin.core.LazyBread;
import com.martin.lazybread.R;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/5 0005
 */
public class FourFragment extends Fragment {

    private TextView mTv;

    private static final String TAG = FourFragment.class.getSimpleName();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LazyBread.onUserVisibleHint(this, isVisibleToUser);
        Log.e(TAG, "setUserVisibleHint: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LazyBread.bind(this);
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.normal_fragment, container, false);
        mTv = inflate.findViewById(R.id.tv);
        Log.e(TAG, "onCreateView: ");
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @LazyInit
    public void lazy() {
        mTv.setText(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LazyBread.unbind(this);
    }
}