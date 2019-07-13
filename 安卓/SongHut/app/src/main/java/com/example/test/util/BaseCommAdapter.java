package com.example.test.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * created by 卢羽帆
 */
public abstract class BaseCommAdapter<T> extends BaseAdapter {
    private List<T> mData;

    public BaseCommAdapter(List<T> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder
                .newsInstance(convertView, parent.getContext(), getLayoutId());

        setUI(holder,position,parent.getContext());

        return holder.getConverView();
    }

    protected abstract void setUI(ViewHolder holder, int position, Context context);

    protected abstract int getLayoutId();

}
