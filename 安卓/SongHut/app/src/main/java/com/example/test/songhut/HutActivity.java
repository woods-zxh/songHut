package com.example.test.songhut;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.test.adapter.HutAdapter;
import com.example.test.gsonUtil.GetAllInfoBean;
import com.example.test.gsonUtil.HutBean;
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
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * created by 卢羽帆
 */
public class HutActivity extends AppCompatActivity {
    private String[] name;
    private String token = "RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjUwOTQ1MjQwNjA7.MGI1NWU4NGFmMzIyODJjNTY2OTdmZTVlNmVkYzhjMTk=";
    private int rid;
    private int[] repositories = null;
    private int dispDefault = 0;
    private Details[] hutInfo;
    private String filePath;
    List<HutBean> huts = new ArrayList<>();

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    private GridView hut_view;
    private Handler mHandler;
    private ImageButton btn_back;
    private ImageButton btn_sort;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hut);

        //初始化
        init();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        HutBean hutBean = new HutBean(hutInfo[msg.arg1].getRepository().getName(), (Drawable) msg.obj);
                        huts.add(hutBean);
                        break;
                    default:
                        break;
                }

                if (msg.arg1 == hutInfo.length - 1) {

                }
            }
        };

        getInfo();
        HutAdapter dispHut = new HutAdapter(huts);
        hut_view = findViewById(R.id.grid_view);
        hut_view.setAdapter(dispHut);
    }

    private void init() {
        btn_sort = findViewById(R.id.btn_sort);
        btn_back = findViewById(R.id.btn_back);
        spinner = (Spinner) findViewById((R.id.spinner));


        //排序方式按钮监听器
        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        //返回按钮监听器
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HutActivity.this.finish();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setVisibility(View.VISIBLE);
                String result = HutActivity.this.getResources().getStringArray(R.array.ctype)[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner.setVisibility(View.GONE);
            }
        });

    }

    private void getInfo() {
        //读取Cache
        String cachePath = getApplicationContext().getCacheDir().getPath();
        String jsonStr = Cache.with(getApplicationContext())
                .path(cachePath)
                .getCache("info", String.class);

        Gson gson = new Gson();
        GetAllInfoBean getAllInfoBean =
                gson.fromJson(jsonStr, GetAllInfoBean.class);
        List<GetAllInfoBean.DataBean.DetailsBean> details = getAllInfoBean.getData().getDetails();
//        for (GetAllInfoBean.DataBean.DetailsBean detail:details
//             ) {
//            String filePath = null;
//            //遍历乐库中所有文件，在其中找到与乐库封面文件id一致的文件对应储存路径
//            if (detail.getRepository().getImg() == null) {
//                HutBean hutBean = new HutBean(detail.getRepository().getName(), getResources().getDrawable(R.drawable.hut_cover_default));
//                huts.add(hutBean);
//            } else {
//                //下载封面图片
//                filePath = (String)detail.getRepository().getImg();
//                downloadImg(token, filePath, detail.getRepository().getR_id(), i);
//            }
//
//        }

        for(int i = 0; i < details.size(); i++){
            GetAllInfoBean.DataBean.DetailsBean detail = details.get(i);
            String filePath = null;
            //遍历乐库中所有文件，在其中找到与乐库封面文件id一致的文件对应储存路径
            if (detail.getRepository().getImg() == null) {
                HutBean hutBean = new HutBean(detail.getRepository().getName(), getResources().getDrawable(R.drawable.hut_cover_default));
                huts.add(hutBean);
            } else {
                //下载封面图片
                filePath = (String)detail.getRepository().getImg();
                downloadImg(token, filePath, detail.getRepository().getR_id(), i);
            }
        }





//        Log.d("Huuuuuut", jsonStr);
//        Gson gson = new Gson();
//        GetAllInfoBean getAllInfoBean =
//                gson.fromJson(jsonStr, GetAllInfoBean.class);
//        //List hutInfo = getAllInfoBean.getData().getDetails().
//
//
//        JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonStr);
//        JsonArray details = jsonObject.getAsJsonObject("data").getAsJsonArray("details");
//
//
//        //获得用户的所有乐库及文件相关信息
//        //repositories = new int[details.size()];
//        for (int i = 0; i < details.size(); i++) {
//            JsonObject item = details.get(i).getAsJsonObject();
//            hutInfo[i] = new Gson().fromJson(item, Details.class);
//            String filePath = null;
//
//            //遍历乐库中所有文件，在其中找到与乐库封面文件id一致的文件对应储存路径
//            for (int j = 0; j < hutInfo[i].getFiles().length; j++) {
//                if (hutInfo[i].getRepository().getImg() == hutInfo[i].getFiles()[j].getF_id()) {
//                    filePath = hutInfo[i].getFiles()[j].getFilePath();
//                    break;
//                }
//            }
//
//            if (filePath == null) {
//                HutBean hutBean = new HutBean(hutInfo[i].getRepository().getName(), getResources().getDrawable(R.drawable.hut_cover_default));
//                huts.add(hutBean);
//            } else {
//                //下载封面图片
//                downloadImg(token, filePath, hutInfo[i].getRepository().getR_id(), i);
//            }
//
//        }

    }

    private void downloadImg(String userToken, String filePath, int rid, int index) {

        final int i = index;
        String url = "http://39.106.183.201:8080/api/repository/downLoadFile";
        final OkHttpClient httpClient = new OkHttpClient();

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", userToken)
                .addFormDataPart("filePath", filePath)
                .addFormDataPart("rid", rid + "")
                .build();

        final Request request = new Request.Builder().url(url).post(body).build();
        final Call call = httpClient.newCall(request);

        //不能在UI进程中上传。新进程在上传完成后刷新UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    final byte[] imgData = response.body().bytes();

                    Bitmap map = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                    final BitmapDrawable bd = new BitmapDrawable(map);

                    Message msg = new Message();
                    msg.obj = bd;
                    msg.what = 1;
                    msg.arg1 = i;

                    mHandler.sendMessage(msg);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

