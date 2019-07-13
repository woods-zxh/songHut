package com.example.test.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test.songhut.R;

import java.util.List;
/**
 * created by 卢羽帆
 */
public class LyricsAdapter extends BaseCommAdapter<String> {

    public LyricsAdapter(List<String> data) {
        super(data);
    }

    @Override
    protected void setUI(ViewHolder holder, int position, Context context) {

        String item = getItem(position);

        TextView tv_lyrics = holder.getItemView(R.id.tv_lyrics);
        tv_lyrics.setText(item);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.lyrics_line;
    }

}
