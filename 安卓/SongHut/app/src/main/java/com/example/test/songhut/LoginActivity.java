package com.example.test.songhut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.util.ErrorCodeConstant;
import com.example.test.util.UploadUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cc.fussen.cache.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.test.util.HttpUtils.JSON;

/**
 * created by 卢羽帆
 */
public class LoginActivity extends AppCompatActivity {

    private TextView tv_main_title; //标题
    private TextView tv_back, tv_register, tv_find_psw; //返回键,注册键，找回密码
    private Button btn_login; //登录按钮
    private String userName, psw; //获取的用户名，密码
    private EditText et_user_name, et_psw; //编辑框
    private JsonObject jsonObject; //服务器传来的json对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    /**
     * 获取界面控件
     */
    private void init() {
        //从main_title_bar.xml中获取的id
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        //从activity_login.xml中获取的id
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);

        //返回键监听器
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

        //立即注册控件监听器
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //找回密码控件监听器
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到找回密码界面（此页面暂未创建）
            }
        });

        //登录按钮监听器
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录，获取控件输入的用户名和密码
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();

                //判断输入是否为空
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                String json = "{\"phone\":\"" + userName + "\",\"password\":\"" + psw + "\"}";
                String url = "http://39.106.183.201:8080/api/user/signIn";
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

                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //判断用户登录情况
                                    if (jsonObject.get("errorCode").toString().equals(ErrorCodeConstant.SUCCESS)) {
                                        //Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        //保存登录状态，在界面保存登录的用户名
                                        saveLoginStatus(true, userName);
                                        //登录成功后关闭此页面进入主页
                                        Intent data = new Intent();
                                        data.putExtra("isLogin", true);
                                        //表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                                        setResult(RESULT_OK, data);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String token = jsonObject.get("token").toString();
                                                String jsonStr = UploadUtil.getAllInfo(token);
                                                Log.d("json", jsonStr);
                                                String cachePath = getApplicationContext().getCacheDir().getPath();
                                                Cache.with(getApplication())
                                                        .path(cachePath)
                                                        .saveCache("info", jsonStr);
                                            }
                                        }).start();
                                        //销毁登录界面
                                        LoginActivity.this.finish();
                                        //跳转到主界面，登录成功的状态传递到 MainActivity 中
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        return;
                                    } else {
                                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
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
        });
    }

    /**
     * 保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status, String userName) {
        //saveLoginStatus(true, userName);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor = sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserName", userName);
        //提交修改
        editor.commit();
    }

    /**
     * 注册成功的数据返回至此
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    //显示数据，onActivityResult
    //startActivityForResult(intent, 1); 从注册界面中获取数据
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //是获取注册界面回传过来的用户名
            String userName = data.getStringExtra("userName");
            if (!TextUtils.isEmpty(userName)) {
                //设置用户名到et_user_name控件
                et_user_name.setText(userName);
                //设置光标位置
                et_user_name.setSelection(userName.length());
            }
        }
    }
}
