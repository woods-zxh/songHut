package com.example.test.mainfrag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.test.songhut.HutActivity;
import com.example.test.songhut.R;
import com.example.test.util.MyScrollView;

/**
 * created by 卢羽帆
 */
public class AccountFragment extends Fragment implements MyScrollView.OnScrollListener{

    private MyScrollView myScrollView;
    private int searchLayoutTop;
    private ImageView iv_img;
    private View ll_sus_tab, title_divider, view1;
    private ImageButton btn_hut;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        iv_img = view.findViewById(R.id.iv_img);
        ll_sus_tab = view.findViewById(R.id.ll_sus_tab);
        title_divider = view.findViewById(R.id.title_divider);
        btn_hut = view.findViewById(R.id.btn_hut);
        view1 = view.findViewById(R.id.view1);

        myScrollView = view.findViewById(R.id.scrollView);
        myScrollView.setOnScrollListener(this);

        //我的乐库按钮监听器
        btn_hut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到我的乐库界面
                Intent intent = new Intent(getActivity(), HutActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    //监听滚动Y值变化，通过addView和removeView来实现悬停效果
    @Override
    public void onScroll(int scrollY) {
        searchLayoutTop = view1.getBottom();//获取searchLayout的顶部位置
        if (scrollY >= searchLayoutTop) {
            ll_sus_tab.setVisibility(View.VISIBLE);
            iv_img.setVisibility(View.INVISIBLE);
            title_divider.setVisibility(View.VISIBLE);
        } else {
            ll_sus_tab.setVisibility(View.INVISIBLE);
            iv_img.setVisibility(View.VISIBLE);
            title_divider.setVisibility(View.GONE);
        }
    }
}
