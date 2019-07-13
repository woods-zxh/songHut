package com.example.test.util;

import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;

import com.example.test.gsonUtil.GetAllInfoBean;
import com.example.test.gsonUtil.GetFileInfoBean;
import com.example.test.gsonUtil.GetRepositoryInfoBean;
import com.example.test.gsonUtil.GetRepositoryOfUserBean;
import com.example.test.gsonUtil.GetUserRepositoryBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * created by 卢羽帆
 */
public class GetInfoUtil {

    /**
     * 传入缓存中的json，解析得到所有库的名称
     * @param jsonStr
     * @return
     */
    public static Map<Integer, String> GetAllRepositoryName(String jsonStr){
        Map<Integer , String> map = new HashMap<Integer, String>();
        Gson gson = new Gson();
        GetAllInfoBean getAllInfoBean =
                gson.fromJson(jsonStr, GetAllInfoBean.class);
        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean> detailsList = getAllInfoBean.getData().getDetails();
        if(detailsList == null || detailsList.size() < 1){
            return null;
        }
        else {
            for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean detail:detailsList
                 ) {
                map.put(detail.getRepository().getR_id(), detail.getRepository().getName());
            }
        }
        return map;
    }

    /**
     * 通过jsonStr得到token
     * @param jsonStr
     * @return
     */
    public static String GetToken(String jsonStr){
        Gson gson = new Gson();
        GetAllInfoBean getAllInfoBean =
                gson.fromJson(jsonStr, GetAllInfoBean.class);
        String token = getAllInfoBean.getToken();
        return token;
    }

    /**
     * 通过文件类型获得文件列表
     * @param jsonStr
     * @param rid
     * @param type
     * @return
     */
    public static List<Map> GetTypeOfRepository(String jsonStr, int rid, int type){
        List<Map> list = new ArrayList<Map>();
        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean> fileList =
                new ArrayList<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean>();
        //存储需要展示的文件的id
        int iShow = 0;
        Gson gson = new Gson();
        GetAllInfoBean getAllInfoBean =
                gson.fromJson(jsonStr, GetAllInfoBean.class);
        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean> detailsList = getAllInfoBean.getData().getDetails();
        //没有库
        if(detailsList == null || detailsList.size() < 1){
            return null;
        }
        //遍历库的id
        for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean detail:detailsList
        ) {
            if(detail.getRepository().getR_id() == rid){
                //得到对应库下的所有文件信息
                fileList = detail.getFiles();
                //得到该库中需要展示的文件
                try {
                    switch (type) {
                        case 1:
                            if (detail.getRepository().getType_1() != null) {
                                iShow = (int) ((double)detail.getRepository().getType_1());
                            }
                            break;
                        case 2:
                            if (detail.getRepository().getType_2() != null) {
                                Log.d("aaaaaaa", detail.getRepository().getType_2().toString());
                                //iShow = Integer.parseInt((String)detail.getRepository().getType_2().toString());
                                iShow = (int)((double) detail.getRepository().getType_2());
                            }
                            break;
                        case 3:
                            if (detail.getRepository().getType_3() != null) {
                                iShow = (int)((double)detail.getRepository().getType_3());
                            }
                            break;
                        case 4:
                            if (detail.getRepository().getType_4() != null) {
                                iShow = (int)((double)detail.getRepository().getType_4());
                            }
                            break;
                    }
                }
                catch(Exception e){
                    Log.d("exc", e.getMessage());
                }
            }
        }
        //遍历文件，判断文件的type
        for(com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean file:fileList
        ) {
            if (file.getFileType() == type){
                Map map = new HashMap();
                map.put("f_id", file.getF_id());
                map.put("filePath", file.getFilePath());
                map.put("fileType", file.getFileType());
                map.put("load_time", file.getLoad_time());
                map.put("introduce", file.getIntroduce());
                map.put("is_public", file.getIs_public());
                if(file.getF_id() == iShow){
                    map.put("is_show", 1);
                }
                else{
                    map.put("is_show", 0);
                }
                list.add(map);
            }
        }
        return list;
    }



    //获得乐库所需文件

    /**
     * 返回type_n,
     * @param jsonStr
     * @param rid
     * @return
     */
    public static Map<String, String> GetFilesInRepository(String jsonStr, int rid){
        Map<String, String> map = new HashMap<String, String>();
        Gson gson = new Gson();
        GetAllInfoBean getAllInfoBean =
                gson.fromJson(jsonStr, GetAllInfoBean.class);
        Log.d("eeeeeeeeee", "1");
        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean> detailsList = getAllInfoBean.getData().getDetails();
        GetAllInfoBean.DataBean.DetailsBean.RepositoryBean repBean;
        //没有库
        Log.d("eeeeeeeeee", "2");
        if(detailsList == null || detailsList.size() < 1){
            return null;
        }
        //遍历库的id
        for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean detail:detailsList
        ) {
            Log.d("eeeeeeeeee", "3");
            if (detail.getRepository().getR_id() == rid) {
                //得到对应detail下的所有文件信息
                Log.d("eeeeeeeeee", "4");
                int fid1 = (int)((double)detail.getRepository().getType_1());
                for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean file :detail.getFiles()
                     ) {
                    if(file.getF_id() == fid1){
                        map.put("type_1", file.getFilePath());
                    }
                }
                int fid2 = (int)((double)detail.getRepository().getType_2());
                for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean file :detail.getFiles()
                ) {
                    if(file.getF_id() == fid2){
                        map.put("type_2", file.getFilePath());
                    }
                }
                int fid3 = (int)((double)detail.getRepository().getType_3());
                for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean file :detail.getFiles()
                ) {
                    if(file.getF_id() == fid3){
                        map.put("type_3", file.getFilePath());
                    }
                }
                int fid4 = (int)((double)detail.getRepository().getType_4());
                for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean file :detail.getFiles()
                ) {
                    if(file.getF_id() == fid4){
                        map.put("type_4", file.getFilePath());
                    }
                }
            }
        }
        return map;

    }





//
//    /**
//     * 通过文件类型获得文件列表
//     * @param jsonStr
//     * @param rid
//     * @param type
//     * @return
//     */
//    public static Map<String, String> GetTypeOfRepository(String jsonStr, int rid, int type){
//        Map<String, String> map = new HashMap<String, String>();
//        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean> fileList =
//                new ArrayList<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean>();
//        Gson gson = new Gson();
//        GetAllInfoBean getAllInfoBean =
//                gson.fromJson(jsonStr, GetAllInfoBean.class);
//        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean> detailsList = getAllInfoBean.getData().getDetails();
//        //没有库
//        if(detailsList == null || detailsList.size() < 1){
//            return null;
//        }
//        //得到所选库的所有文件
//        for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean detail:detailsList
//        ) {
//            if(detail.getRepository().getR_id() == rid){
//                fileList = detail.getFiles();
//            }
//        }
//        for(com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean file:fileList
//    ) {
//            if (file.getFileType() == type)
//                map.put(file.getFilePath(), file.getIntroduce());
//        }
//        return map;
//    }
//
//
//    /**
//     * 通过文件类型得到展示的文件
//     * @param jsonStr
//     * @param rid
//     * @param type
//     * @return
//     */
//    public static Pair<Integer, String> GetShowType(String jsonStr, int rid, int type){
//
//
//
//
//
//        int fileId = 0;
//        Pair<Integer, String> pair = new Pair<>(1, "");
//        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean> fileList =
//                new ArrayList<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean>();
//        Gson gson = new Gson();
//        GetAllInfoBean getAllInfoBean =
//                gson.fromJson(jsonStr, GetAllInfoBean.class);
//        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean> detailsList = getAllInfoBean.getData().getDetails();
//        //没有库
//        if(detailsList == null || detailsList.size() < 1){
//            return null;
//        }
//        //得到所选库的所有文件
//        for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean detail:detailsList
//        ) {
//            if(detail.getRepository().getR_id() == rid){
//                switch (type){
//                    case 1:
//                        fileId = detail.getRepository().getType_1();
//                        break;
//                    case 2:
//                        fileId = detail.getRepository().getType_2();
//                        break;
//                    case 3:
//                        fileId = detail.getRepository().getType_3();
//                        break;
//                    case 4:
//                        fileId = detail.getRepository().getType_4();
//                        break;
//                }
//                fileList = detail.getFiles();
//            }
//        }
//        for(com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean.FilesBean file:fileList
//        ) {
//            if (file.getF_id() == fileId)
//                pair = new Pair<>(fileId, file.getFilePath());
//        }
//        return pair;
//    }
//
//
//



//    public static int GetRepositoryIdFromName(String name){
//        List<String> list = new ArrayList<String>();
//        Gson gson = new Gson();
//        GetAllInfoBean getAllInfoBean =
//                gson.fromJson(jsonStr, GetAllInfoBean.class);
//        List<com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean> detailsList = getAllInfoBean.getData().getDetails();
//        if(detailsList == null || detailsList.size() < 1){
//            return list;
//        }
//        else {
//            for (com.example.test.gsonUtil.GetAllInfoBean.DataBean.DetailsBean detail:detailsList
//            ) {
//                list.add(detail.getRepository().getName());
//            }
//        }
//        return list;
//    }

}
