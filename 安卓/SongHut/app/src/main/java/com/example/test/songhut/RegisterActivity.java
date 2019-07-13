package com.example.test.songhut;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
public class RegisterActivity extends AppCompatActivity {

    private TextView tv_back;//返回按钮
    private Button btn_register;//注册按钮
    private Button btn_captcha;//发送验证码
    //用户名，密码，验证码的控件
    private EditText et_phone_number, et_psw, et_psw2, et_captcha;
    //用户名，密码，验证码的控件的获取值
    private String phoneNumber, psw, psw2, captcha;
    //标题布局
    private RelativeLayout rl_title_bar;
    private JsonObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局 ,注册界面
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    /**
     * 获取界面控件
     */
    private void init() {
        //从main_title_bar.xml页面布局中获取对应的UI控件
        tv_back = findViewById(R.id.tv_back);

        //从activity_register.xml 页面中获取对应的UI控件
        btn_register = findViewById(R.id.btn_register);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_captcha = findViewById(R.id.et_captcha);
        et_psw = findViewById(R.id.et_psw);
        et_psw2 = findViewById(R.id.et_psw2);
        btn_captcha = findViewById(R.id.btn_captcha);

        //返回按钮监听器
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });

        //发送验证码按钮监听器
        btn_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(RegisterActivity.this, "验证码已发送至" + phoneNumber, Toast.LENGTH_SHORT).show();
                    //发送验证码
                }
            }
        });

        //注册按钮监听器
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();

                //判断输入是否为空
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw2)) {
                    Toast.makeText(RegisterActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!psw.equals(psw2)) {
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(captcha)) {
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                String json = "{\"phone\":\"" + phoneNumber + "\",\"captcha\":\"" + captcha + "\",\"password\":\"" + psw + "\"}";
                String url = "http://39.106.183.201:8080/api/user/signUp";
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

                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //判断用户登录情况
                                    if (jsonObject.get("errorCode").toString().equals(ErrorCodeConstant.SUCCESS)) {
                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        //注册成功后把账号传递到LoginActivity.java中，返回值到登录界面显示
                                        Intent data = new Intent();
                                        data.putExtra("userName", phoneNumber);
                                        setResult(RESULT_OK, data);
                                        // 表示此页面下的内容操作成功将data返回到上一页面
                                        RegisterActivity.this.finish();
                                    } else if (jsonObject.get("errorCode").toString().equals(ErrorCodeConstant.LOCAL_CAPTCHA_WRONG)) {
                                        Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (jsonObject.get("errorCode").toString().equals(ErrorCodeConstant.LOCAL_PHONE_USED)) {
                                        Toast.makeText(RegisterActivity.this, "此手机号已经存在", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (jsonObject.get("errorCode").toString().equals(ErrorCodeConstant.LOCAL_CAPTCHA_EXPIRED)) {
                                        Toast.makeText(RegisterActivity.this, "验证码已过期", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (jsonObject.get("errorCode").toString().equals(ErrorCodeConstant.LOCAL_INVALID_PASSWORD)) {
                                        Toast.makeText(RegisterActivity.this, "密码格式有误（）", Toast.LENGTH_SHORT).show();
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
     * 获取控件中的字符串
     */
    private void getEditString() {
        phoneNumber = et_phone_number.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
        psw2 = et_psw2.getText().toString().trim();
        captcha = et_captcha.getText().toString().trim();
    }
}
