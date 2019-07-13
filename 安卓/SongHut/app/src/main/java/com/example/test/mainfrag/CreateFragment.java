package com.example.test.mainfrag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.songhut.CreateHutActivity;
import com.example.test.songhut.CreateLyricsActivity;

import com.example.test.songhut.R;
import com.example.test.songhut.RecordActivity;
import com.example.test.songhut.RecordMusicActivity;
import com.example.test.songhut.RecordSingActivity;

import com.example.test.util.popupwindow.*;

import java.util.ArrayList;
import java.util.List;

/**
 * created by 卢羽帆
 */
public class CreateFragment extends Fragment {

    public static View btn_create;
    public static PathPopupWindow popupWindow = null;
    public static int num = 0;
    private List<com.example.test.util.popupwindow.PathItem> pathItemList;

    public CreateFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        btn_create = view.findViewById(R.id.btn_create);

        pathItemList = new ArrayList<>();
        pathItemList.add(new com.example.test.util.popupwindow.PathItem().name("创建库").imageResId(R.drawable.create_hut).backgroundResId(R.drawable.bg_blue_oval));
        pathItemList.add(new com.example.test.util.popupwindow.PathItem().name("曲").imageResId(R.drawable.create_melody).backgroundResId(R.drawable.bg_blue_oval));
        pathItemList.add(new com.example.test.util.popupwindow.PathItem().name("伴").imageResId(R.drawable.create_accompany).backgroundResId(R.drawable.bg_blue_oval));
        pathItemList.add(new com.example.test.util.popupwindow.PathItem().name("唱").imageResId(R.drawable.create_sing).backgroundResId(R.drawable.bg_blue_oval));
        pathItemList.add(new com.example.test.util.popupwindow.PathItem().name("词").imageResId(R.drawable.create_lyrics).backgroundResId(R.drawable.bg_blue_oval));

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0) {
                    popupWindow = new PathPopupWindow(getActivity(), pathItemList);
                    popupWindow.setOnPathItemClickListener(new PathPopupWindow.OnPathItemClickListener() {
                        @Override
                        public void onItemClick(int position, com.example.second.popupwindow.PathItem item) {
                            if (item.name == "词") {
                                Intent intent1 = new Intent(getActivity(), CreateLyricsActivity.class);
                                startActivity(intent1);
                            } else if (item.name == "伴") {
                                Intent intent2 = new Intent(getActivity(), RecordActivity.class);
                                startActivity(intent2);
                            } else if (item.name == "曲") {
                                Intent intent3 = new Intent(getActivity(), RecordMusicActivity.class);
                                startActivity(intent3);
                            } else if (item.name == "唱") {
                                Intent intent4 = new Intent(getActivity(), RecordSingActivity.class);
                                startActivity(intent4);
                            }else if(item.name == "创建库"){
                                Intent intent4 = new Intent(getActivity(), CreateHutActivity.class);
                                startActivity(intent4);
                            }
                        }
                    });
                    popupWindow.setOutsideTouchable(false);
                    popupWindow.setFocusable(false);
                    num++;
                    popupWindow.show(v);

                }
            }
        });

        if (num == 0) {
            btn_create.performClick();
        }

        return view;
    }
}
