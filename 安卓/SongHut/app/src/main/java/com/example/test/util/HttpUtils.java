package com.example.test.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * created by 卢羽帆
 */
public class HttpUtils {
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    static JsonObject jsonObject;
    public static Thread t;

    public static JsonObject sendRequest(String url, String json) throws IOException {
        final OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder().url(url).post(body).build();

        if (true) {
            final Call call = httpClient.newCall(request);
            //不能在UI进程中上传。新进程在上传完成后刷新UI
            t = new Thread(new Runnable() {
                public void run() {
                    try {
                        Response response = call.execute();
                        final String jsonData = response.body().string();
                        jsonObject = (JsonObject) new JsonParser().parse(jsonData);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            return jsonObject;
        } else {
            throw new IOException("Unexpected code ");
        }
    }
}
