package com.martin.lazybread.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.martin.lazybread.R;

import java.util.List;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/5 0005
 */
public class MainAdapter extends BaseAdapter {

    private List<String> mList;
    private LayoutInflater mInflater;
    private Context context;

    public MainAdapter(List<String> mList, Context context) {
        this.mList = mList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_main, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(getItem(position));
        return convertView;
    }

    class ViewHolder {
        TextView tv;

        public ViewHolder(View view) {
            this.tv = view.findViewById(R.id.tv);
        }
    }
}
