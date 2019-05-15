package com.martin.lazybread.lazycycle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.martin.annotation.LazyCycle;
import com.martin.core.LazyBread;
import com.martin.lazybread.R;

public class LazyCycleActivity extends AppCompatActivity {

    private static final String TAG = LazyCycleActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, LazyCycleActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_cycle);

        LazyBread.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LazyBread.resume(this);
    }

    @LazyCycle(interval = 1000)
    public void cycle() {
        Log.e(TAG, "cycle: 循环的内容");
    }

    @LazyCycle(interval = 2000, times = 3)
    public void cycleForCount() {
        Log.e(TAG, "cycleForCount: 带有次数的循环");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LazyBread.pause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LazyBread.unbind(this);
    }
}
