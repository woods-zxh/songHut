package com.example.test.songhut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.test.util.ErrorCodeConstant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.test.util.HttpUtils.JSON;
/**
 * created by 卢羽帆
 */
public class CreateHutActivity extends AppCompatActivity {
    private Button btn_create_hut;
    private ImageButton btn_back;
    private EditText et_hut_name;
    private EditText et_hut_introduce;
    private Switch switch_public;

    private String name;
    private String introduce;
    private int isPublic = 1; //默认乐库为公开状态
    private String token= "RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjU1MzAyNzk4Mjg7.ODExNzJjYjg0YzUwMzZmZGYzYWUyNDQ3MDQ4M2VhNmM=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hut);

        init();

        //返回按钮监听器
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateHutActivity.this.finish();
            }
        });

        //乐库公开开关监听器
        switch_public.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isPublic = 1;
                } else {
                    isPublic = 0;
                }
            }
        });

        //创建乐库按钮监听器
        btn_create_hut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_hut_name.getText().toString().trim();
                introduce = et_hut_introduce.getText().toString().trim();

                //判断输入是否为空
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CreateHutActivity.this, "请输入乐库名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(introduce)) {
                    Toast.makeText(CreateHutActivity.this, "请输入乐库简介", Toast.LENGTH_SHORT).show();
                    return;
                }

                createHut(name, introduce, isPublic, token);
            }
        });
    }

    private void init() {
        btn_create_hut = findViewById(R.id.btn_create_hut);
        et_hut_name = findViewById(R.id.et_hut_name);
        et_hut_introduce = findViewById(R.id.et_hut_introduce);
        switch_public = findViewById(R.id.switch_public);
        btn_back = findViewById(R.id.btn_back);
    }

    private void createHut(String name, String introduce, int isPublic, String token) {
        String json = "{\"token\":\"" + token + "\",\"name\":\"" + name + "\",\"introduce\":\"" + introduce + "\",\"isPublic\":" + isPublic + "}";
        String url = "http://47.106.107.107:8080/api/repository/setMusicRepository";
        final OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder().url(url).post(body).build();

        final Call call = httpClient.newCall(request);
        //不能在UI进程中上传。新进程在上传完成后刷新UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    final String jsonData = response.body().string();
                    final JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonData);

                    CreateHutActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.get("errorCode").toString().equals(ErrorCodeConstant.SUCCESS)) {
                                //创建完后打开我的乐库界面
                                Intent intent = new Intent(CreateHutActivity.this, ShowActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(CreateHutActivity.this, "创建失败，请重试", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
