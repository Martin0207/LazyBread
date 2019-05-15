package com.martin.lazybread.lazyinit;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.martin.lazybread.R;
import com.martin.lazybread.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LazyInitActivity extends AppCompatActivity {

    private ViewPagerAdapter mAdapter;
    private TabLayout tab;
    private ViewPager vp;

    public static void start(Context context) {
        Intent starter = new Intent(context, LazyInitActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_init);

        tab = findViewById(R.id.tab);
        vp = findViewById(R.id.vp);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getFragments());
        mAdapter.setmTitleList(getTitles());
        vp.setAdapter(mAdapter);
        tab.setupWithViewPager(vp);
    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("OneFragment");
        titles.add("TwoFragment");
        titles.add("ThreeFragment");
        titles.add("FourFragment");
        return titles;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new OneFragment());
        fragments.add(new TwoFragment());
        fragments.add(new ThreeFragment());
        fragments.add(new FourFragment());
        return fragments;
    }
}
