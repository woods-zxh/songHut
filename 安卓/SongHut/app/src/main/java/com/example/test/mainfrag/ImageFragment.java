package com.example.test.mainfrag;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test.songhut.R;
import com.example.test.util.GetInfoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * created by 李军邑
 */
public class ImageFragment extends Fragment {

    public ImageFragment() {
        // Required empty public constructor
    }

    /* 这个方法适合初始化数据 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        return view;
    }


}
