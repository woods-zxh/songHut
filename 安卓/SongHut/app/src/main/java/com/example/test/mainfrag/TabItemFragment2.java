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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.songhut.Convert2MelodyActivity;
import com.example.test.songhut.R;
import com.example.test.util.GetInfoUtil;
import com.example.test.util.UploadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.fussen.cache.Cache;

/**
 * created bt 李军邑
 * 从activity中得到jsonStr和rid
 */
public class TabItemFragment2 extends Fragment {

    private String jsonStr;
    private String token = "";
    private int rid = 1;
    private int fragType = 2; //文件的格式
    private int pos = -1; //视图中为绿色字体的位置

    List<Map> list = new ArrayList<Map>(); //该fragment显示的所有文件列表

    /* 定义适配器 */
    private TabItemFragment2.MyAdapter adapter;
    private ListView tabItemLV2;
    private Toast toast;

    public TabItemFragment2() {
        // Required empty public constructor
    }

//    //接受多线程信息，安卓中不允许主线程实现UI更新
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            adapter = new TabItemFragment2.MyAdapter(getActivity(), list);
//            tabItemLV2.setAdapter(adapter);
//        }
//    };

    private Handler showHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            toast.show();
        }
    };

    /* 这个方法适合初始化数据 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        rid = bundle.getInt("rid");
        jsonStr = bundle.getString("jsonStr");
        token = GetInfoUtil.GetToken(jsonStr);
        toast = Toast.makeText(getActivity(), "设置成功" ,Toast.LENGTH_SHORT);

        //获得页面数据list并绑定
        list = GetInfoUtil.GetTypeOfRepository(jsonStr, rid , fragType);
        adapter = new TabItemFragment2.MyAdapter(this.getActivity(), list);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_item_fragment2, container, false);
        tabItemLV2 = view.findViewById(R.id.tabItemLV2);
        tabItemLV2.setAdapter(adapter);
        tabItemLV2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //不能页面滑动，可能会选择到其他界面！！
                //将绿色的地方改为黑色
                if(pos != -1){
                    View viewOld = tabItemLV2.getChildAt(pos);
                    TextView tv_fileName_old = viewOld.findViewById(R.id.tv_fileName);
                    TextView tv_fileBio_old = viewOld.findViewById(R.id.tv_fileBio);
                    tv_fileName_old.setTextColor(getResources().getColor(R.color.text_balck));
                    tv_fileBio_old.setTextColor(getResources().getColor(R.color.text_balck));
                    list.get(pos).put("is_show", 0);
                }
                pos = position;
                TextView tv_fileName = view.findViewById(R.id.tv_fileName);
                TextView tv_fileBio = view.findViewById(R.id.tv_fileBio);
                tv_fileName.setTextColor(getResources().getColor(R.color.text_green));
                tv_fileBio.setTextColor(getResources().getColor(R.color.text_green));
                list.get(pos).put("is_show", 1);

                //新线程上传数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int fid = (int)(list.get(pos).get("f_id"));
                        UploadUtil.setFileType(token, fid, rid, fragType);
                        showHandler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
        return view;
    }

    private class MyAdapter extends BaseAdapter {

        private List<Map> list;
        private LayoutInflater inflater;

        MyAdapter(Context context, List<Map> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public Map getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //加载布局为一个视图
            View view = inflater.inflate(R.layout.list_item_wav, null);

            Map map = getItem(position);

            TextView tv_fileName = view.findViewById(R.id.tv_fileName);
            TextView tv_fileBio = view.findViewById(R.id.tv_fileBio);
            tv_fileName.setText(map.get("filePath").toString());
            tv_fileBio.setText(map.get("introduce").toString());

            //设置播放的文件为绿色
            if((int)(map.get("is_show")) == 1){
                pos = position;
                tv_fileName.setTextColor(getResources().getColor(R.color.text_green));
                tv_fileBio.setTextColor(getResources().getColor(R.color.text_green));
            }
            return view;
        }
    }
}
