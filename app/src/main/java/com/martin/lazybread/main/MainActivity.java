package com.martin.lazybread.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.martin.lazybread.R;
import com.martin.lazybread.adapter.MainAdapter;
import com.martin.lazybread.lazycycle.LazyCycleActivity;
import com.martin.lazybread.lazyinit.LazyInitActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mAdapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);
        mAdapter = new MainAdapter(getItems(), this);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        LazyInitActivity.start(MainActivity.this);
                        break;
                    case 1:
                        LazyCycleActivity.start(MainActivity.this);
                        break;
                }
            }
        });
    }

    private List<String> getItems() {
        List<String> items = new ArrayList<>();
        items.add("LazyInit demo");
        items.add("LazyCycle demo");
        return items;
    }

}
