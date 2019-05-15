package com.martin.lazybread.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/5 0005
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragmentList = list;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null && mTitleList.size() > position) {
            return mTitleList.get(position);
        } else {
            return "";
        }
    }

    public List<String> getmTitleList() {
        return mTitleList;
    }

    public void setmTitleList(List<String> mTitleList) {
        this.mTitleList = mTitleList;
    }
}
