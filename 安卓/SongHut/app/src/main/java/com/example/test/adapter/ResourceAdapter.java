package com.example.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test.baseclass.Resource;
import com.example.test.songhut.R;

import java.util.List;

/**
 * created by 卢羽帆
 */
public class ResourceAdapter extends BaseAdapter {

    private List<Resource> resList;
    private LayoutInflater inflater;

    public ResourceAdapter(Context context, List<Resource> resList) {
        this.inflater=LayoutInflater.from(context);
        this.resList = resList;
    }

    @Override
    public int getCount() {
        return resList==null?0:resList.size();
    }

    @Override
    public Resource getItem(int position) {
        return resList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //加载布局为一个视图
        View view = inflater.inflate(R.layout.list_item_resource, null);
        Resource resource = getItem(position);

        //在view视图中查找id为image_photo的控件
        //ImageView image_photo = (ImageView) view.findViewById(R.id.img);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_creators = (TextView) view.findViewById(R.id.tv_creators);
        TextView tv_bio = (TextView) view.findViewById(R.id.tv_bio);
        TextView tv_favorites = (TextView) view.findViewById(R.id.tv_favorites);
        TextView tv_forks = (TextView) view.findViewById(R.id.tv_forks);
        //image_photo.setImageResource(resource.getPhoto());
        tv_name.setText(resource.getName());
        tv_creators.setText(String.valueOf(resource.getCreators()[0].toString()));
        tv_bio.setText(resource.getBio());
        tv_favorites.setText(String.valueOf(resource.getFavorites()));
        tv_forks.setText(String.valueOf(resource.getForks()));

        return view;
    }
}
