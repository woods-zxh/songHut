package com.example.test.util;


import android.util.Log;

import com.example.test.gsonUtil.GetRepositoryInfoBean;
import com.example.test.gsonUtil.GetRepositoryOfUserBean;
import com.example.test.gsonUtil.GetUserRepositoryBean;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/***
 * created by 李军邑
 * 与后台交互的工具类
 * 返回结果正确的json字符串或""
 * 需在前端验证是否为空或list是否为空
 */
public class UploadUtil {
    final static private String baseUrl = "http://39.106.183.201:8080/";
    final static private String baseUrl2 = "http://47.106.107.107:8080/";
    final static private String postFileToRepositoryUrl = baseUrl2 + "api/repository/postFileToRepository";

    final static private String getRepositoryOfUserUrl = baseUrl2 + "api/repository/getRepositoryOfUser";
    final static private String getRepositoryInfoUrl = baseUrl2 + "api/repository/getRepositoryInfo";

    final static private String getAllInfoUrl = baseUrl2 + "api/repository/getAllInfo";

    final static private String setFileTypeUrl = baseUrl2 + "api/repository/setFileType";
    final static private String setTransferTaskUrl = baseUrl2 + "api/external/setTransferTask";
    final static private String checkTaskStateUrl = baseUrl2 + "api/external/checkTaskState";

    /**
     * 同步请求，根据token获得名下所有乐库
     * @param token
     * @return
     */
    public static String getRepositoryOfUser(String token){
        String getRepositoryOfUserJson = "";
        String getRepositoryOfUserJsonErr = "";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        MediaType tokenJson = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        String jsonStr = "{\"token\":\""+token +"\"}";
        RequestBody body = RequestBody.create(tokenJson, jsonStr);
        final Request request = new Request.Builder().url(getRepositoryOfUserUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            final String json = response.body().string();
            //解析json
            Gson gson = new Gson();
            GetRepositoryOfUserBean getRepositoryOfUserBean =
                    gson.fromJson(json, GetRepositoryOfUserBean.class);
            //判断是否解析成功
            if(getRepositoryOfUserBean != null){
                String errorCode = getRepositoryOfUserBean.getErrorCode();
                String returnToken = getRepositoryOfUserBean.getToken();
                if(errorCode.equals("0000")){
                    //返回结果正确
                    getRepositoryOfUserJson = json;
                }
                else {
                    getRepositoryOfUserJsonErr = json;
                    Log.d("SongHut:UploadUtil",
                            "getRepositoryInfo:" + "getUserRepositoryJsonErr" + getRepositoryOfUserJsonErr);
                }
            }
        } catch (final Exception e) {
            Log.d("SongHut:UploadUtil", "getRepositoryInfo:" + e.getMessage());
        }finally {
            return getRepositoryOfUserJson;
        }
    }



    /**
     * 同步请求，根据乐库id获取乐库信息
     * @param token
     * @param rid
     * @return
     */
    public static String getRepositoryInfo(String token, long rid){
        String getUserRepositoryJson = "";
        String getUserRepositoryJsonErr = "";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        MediaType tokenJson = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        String jsonStr = "{\"token\":\""+token +"\",\"rid\":"+rid+"}";
        RequestBody body = RequestBody.create(tokenJson, jsonStr);
        final Request request = new Request.Builder().url(getRepositoryInfoUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            final String json = response.body().string();
            //解析json
            Gson gson = new Gson();
            GetRepositoryInfoBean getRepositoryInfoBean =
                    gson.fromJson(json, GetRepositoryInfoBean.class);
            //判断是否解析成功
            if(getRepositoryInfoBean != null){
                String errorCode = getRepositoryInfoBean.getErrorCode();
                String returnToken = getRepositoryInfoBean.getToken();
                if(errorCode.equals("0000")){
                    //返回结果正确
                    getUserRepositoryJson = json;
                }
                else {
                    getUserRepositoryJsonErr = json;
                    Log.d("SongHut:UploadUtil",
                            "getRepositoryInfo:" + "getUserRepositoryJsonErr" + getUserRepositoryJsonErr);
                }
            }
            //return getUserRepositoryJson;
        } catch (final Exception e) {
            Log.d("SongHut:UploadUtil", "getRepositoryInfo:" + e.getMessage());
        }finally {
            return getUserRepositoryJson;
        }
    }

    /**
     * 同步请求，根据文件id获取文件信息
     * @param token
     * @param fid
     * @return
     */
    public static String getFileInfo(String token, long fid){
        String getFileInfoJson = "";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        MediaType tokenJson = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        String jsonStr = "{\"token\":\""+token +"\",\"fid\":"+fid+"}";
        RequestBody body = RequestBody.create(tokenJson, jsonStr);
        final Request request = new Request.Builder().url(getRepositoryInfoUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            getFileInfoJson = response.body().string();
        } catch (final Exception e) {
            Log.d("SongHut:UploadUtil", "getFileInfo:" + e.getMessage());
        }finally {
            return getFileInfoJson;
        }
    }

    /**
     *
     * @param token
     * @param upLoadFilePath
     * @param introduce
     * @param fileName
     * @param rid
     * @param fileType
     * @param isPublic
     * @return json格式 errorCode, token, data
     */
    public static String postFileToRepository(String token, String upLoadFilePath, String introduce,
                                       String fileName, int rid, int fileType, int isPublic, int isImg){
        String json = "";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        File file = new File(upLoadFilePath);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("upLoadFile", fileName, RequestBody.create(MediaType.parse("audio/wav"), file))
                .addFormDataPart("introduce", introduce)
                .addFormDataPart("fileName", fileName)
                .addFormDataPart("rid", rid + "")
                .addFormDataPart("fileType", fileType +"")
                .addFormDataPart("isPublic", isPublic + "")
                .addFormDataPart("isImg", isImg + "")
                .build();
        //Log.d("posttttttttt", String(body));
        final Request request = new Request.Builder().url(postFileToRepositoryUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            json = response.body().string();
        } catch (final Exception e) {
            Log.d("SongHut:UploadUtil", "postFileToRepository:" + e.getMessage());
        }finally {
            Log.d("jsonnnnnnnn", json);
            return json;
        }
    }

    public static String downLoadFile(String token, String filePath, int rid) {
        return "";
    }


    public static String getAllInfo(String token){
        String json = "";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        MediaType tokenJson = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        String jsonStr = "{\"token\":"+token +"}";
        RequestBody body = RequestBody.create(tokenJson, jsonStr);
        final Request request = new Request.Builder().url(getAllInfoUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            json = response.body().string();
        } catch (final Exception e) {
            Log.d("SongHut:UploadUtil", "getAllInfo:" + e.getMessage());
        }finally {
            return json;
        }
    }

    /**
     * 同步请求 设置展示文件
     * @param token
     * @param fid
     * @param rid
     * @param type
     * @return
     */
    public static String setFileType(String token, int fid, int rid, int type) {
        String json = "";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        MediaType tokenJson = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        String jsonStr = "{\"token\":\""+token +"\",\"fid\":"+fid+",\"rid\":"+rid+",\"type\":"+type+"}";
        Log.d("jjjjjjj", jsonStr);
        RequestBody body = RequestBody.create(tokenJson, jsonStr);
        final Request request = new Request.Builder().url(setFileTypeUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            json = response.body().string();
        } catch (final Exception e) {
            Log.d("SongHut:UploadUtil", "setFileType:" + e.getMessage());
        }finally {
            return json;
        }
    }


    /**
     * 同步请求 开启转换任务
     * @param token
     * @param fid
     * @param rid
     * @param type
     * @return
     */
    public static String setTransferTask(String token, int fid, int rid, int type, int chord){
        String json = "";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        MediaType tokenJson = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        //String jsonStr = "{\"token\":\""+token +"\",\"fid\":"+fid+",\"rid\":"+rid+",\"type\":"+type+"}";
        String jsonStr = "{\"token\":\""+token +"\",\"fid\":"+fid+",\"rid\":"+rid+",\"type\":"+type+",\"isDrum\":0,\"isChord\":"+chord+",\"isBass\":0}";
        RequestBody body = RequestBody.create(tokenJson, jsonStr);
        final Request request = new Request.Builder().url(setTransferTaskUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            json = response.body().string();
        } catch (final Exception e) {
            Log.d("SongHut:UploadUtil", "setTransferTask:" + e.getMessage());
        }finally {
            return json;
        }
    }

    /**
     * 同步请求 检查任务状态
     * @param token
     * @param fid
     * @param rid
     * @return
     */
    public static String checkTaskState(String token, long fid,long rid){
        String json = "";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        MediaType tokenJson = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
        String jsonStr = "{\"token\":\""+token +"\",\"fid\":"+fid+",\"rid\":"+rid+"}";

        RequestBody body = RequestBody.create(tokenJson, jsonStr);
        final Request request = new Request.Builder().url(checkTaskStateUrl).post(body).build();
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            json = response.body().string();
        } catch (final Exception e) {
            Log.d("SongHut:UploadUtil", "checkTaskState:" + e.getMessage());
        }finally {
            return json;
        }
    }

}
