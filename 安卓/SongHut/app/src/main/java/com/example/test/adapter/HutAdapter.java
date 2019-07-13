package com.example.test.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.test.gsonUtil.HutBean;
import com.example.test.songhut.R;
import com.example.test.util.BaseCommAdapter;
import com.example.test.util.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import cc.fussen.cache.Cache;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * created by 卢羽帆
 */
public class HutAdapter extends BaseCommAdapter<HutBean> {

    public HutAdapter(List<HutBean> data) {
        super(data);
    }

    @Override
    protected void setUI(ViewHolder holder, int position, Context context) {
        HutBean item = getItem(position);

        TextView tv_name = holder.getItemView(R.id.tv_name);
        tv_name.setText(item.getName());

        ImageView iv_head = holder.getItemView(R.id.image);
        iv_head.setImageDrawable(item.getImg());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.hut;
    }
}
