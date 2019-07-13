package com.songhut.songhut.controller;

import com.songhut.songhut.dao.*;
import com.songhut.songhut.model.*;
import com.songhut.songhut.util.*;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包含外部调用功能的接口
 * @author Kun
 */
@Controller
@Api(tags = "ExternalController", description = "乐库管理接口")
@RequestMapping("/api/external")
public class ExternalController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private FileUserDAO fileUserDAO;

    @Autowired
    private TransferTaskDAO transferTaskDAO;

    @Autowired
    private RepositoryUserDAO repositoryUserDAO;

    @Autowired
    private RepositoryFileDAO repositoryFileDAO;

    @Autowired
    private FileDAO fileDAO;


    /**
     * 设置转换任务
     */
    @RequestMapping(value="/setTransferTask")
    @ResponseBody
    public Object setTransferTask(@RequestBody Map<String,Object> params) {
        CommonResultToken result = new CommonResultToken();
        int fid = (Integer)params.get("fid");
        int rid = (Integer)params.get("rid");
        int type = (Integer)params.get("type");
        int isDrum = (Integer)params.get("isDrum");
        int isChord = (Integer)params.get("isChord");
        int isBass = (Integer)params.get("isBass");
        String token = (String)params.get("token");

        //根据token确定user
        User user = getUserByToken(token);
        if (user == null) {
            result.tokenFail();
        } else {
            //判断token是否过期
            if (user.getExpired_time().getTime() < new Date().getTime()) {
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            } else {
                TransferTask task = new TransferTask();
                task.setF_id(fid);
                task.setR_id(rid);
                task.setU_id(user.getUid());

                //判断uid与rid之间是否存在关系
                List<RepositoryUser> repositoryUsers = repositoryUserDAO.getRelation(user.getUid(),rid);
                if(repositoryUsers==null || repositoryUsers.size()==0){
                    //不存在关系，返回错误，权限不足
                    result.setErrorCode(ErrorCodeConstant.GLOBAL_UNAUTHORIZE);
                }else {
                    //数据库中注册任务，若任务已经存在，则返回错误码
                    List<TransferTask> tasks = transferTaskDAO.selectTask(task);

                    if (tasks == null || tasks.size() == 0) {
                        //获取文件信息
                        List<File> files = fileDAO.getFile(fid);
                        if (files == null || files.size() == 0) {
                            //不存在该文件
                            result.setErrorCode(ErrorCodeConstant.FILE_NOT_EXSIT);
                        } else {
                            /**
                             * 成功出口
                             */
                            File file = files.get(0);

                            //todo:调用张新豪的接口
                            logger.info("调用转伴奏接口");

                            RestTemplate restTemplate = RestTemplateUtil.getInstance("utf-8");

                            MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
                            request.add("filePath", file.getFilePath());
                            request.add("token", token);
                            request.add("rid", rid);
                            request.add("fid",fid);
                            request.add("type",type);
                            request.add("isDrum",isDrum);
                            request.add("isChord",isChord);
                            request.add("isBass",isBass);
                            String url = Constants.TRANSFER_URL;
                            URI uri = URI.create(url);
                            String ans = restTemplate.postForObject(uri, request, String.class);
                            logger.info(ans);


                            //添加任务
                            transferTaskDAO.addTask(task);


                            //更新token
                            token = updateToken(user.getUid());
                            result.success(token,null);
                        }
                    } else {
                        result.setErrorCode(ErrorCodeConstant.TASK_EXISTED);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 修改任务状态为已完成
     */
    @RequestMapping(value="/completeTask")
    @ResponseBody
    public Object completeTask(@RequestParam("fid") Integer fid,
                               @RequestParam("rid") Integer rid, @RequestParam("token") String token,
                               @RequestParam("filePath")String filePath) {
        CommonResultToken result = new CommonResultToken();
        logger.info("任务状态更新");
        //根据token确定user
        User user = getUserByToken(token);
        if (user == null) {
            result.tokenFail();
        } else {
            TransferTask task = new TransferTask();
            task.setF_id(fid);
            task.setR_id(rid);
            task.setU_id(user.getUid());
            task.setFile_path(filePath);
            task.setIs_done(1);
            //数据库中注册任务，若任务已经存在，则返回错误码
            List<TransferTask> tasks = transferTaskDAO.selectTask(task);
            if(tasks==null || tasks.size()==0){
                result.setErrorCode(ErrorCodeConstant.TASK_NOT_EXIST);
            }else {

                //更新任务状态
                transferTaskDAO.updateTask(task);

                //获取哼唱文件用来填写旋律文件信息
                List<File> files = fileDAO.getFile(fid);
                File file = files.get(0);

                //添加旋律文件
                File new_file = new File();
                new_file.setFilePath(filePath);
                new_file.setIntroduce(file.getIntroduce());
                new_file.setLoad_time(new Timestamp(new Date().getTime()));
                new_file.setFileType(file.getFileType());
                fileDAO.addFile(new_file);

                //建立旋律文件与用户关系
                fileUserDAO.addFileUser(user.getUid(), new_file.getF_id());

                //建立旋律文件与乐库关系
                repositoryFileDAO.addRepositoryFile(new_file.getF_id(), rid);

                //更新token
                token = updateToken(user.getUid());
                result.success(token, null);
            }
        }
        return result;
    }

    /**
     * 检查任务状态，若完成，则得到任务产生文件的id
     */
    @RequestMapping(value="/checkTaskState")
    @ResponseBody
    public Object checkTaskState(@RequestBody Map<String,Object> params) {
        CommonResultToken result = new CommonResultToken();
        int fid = (Integer)params.get("fid");
        int rid = (Integer)params.get("rid");
        String token = (String)params.get("token");

        //根据token确定user
        User user = getUserByToken(token);
        if (user == null) {
            result.tokenFail();
        } else {
            TransferTask task = new TransferTask();
            task.setF_id(fid);
            task.setR_id(rid);
            task.setU_id(user.getUid());

            List<TransferTask> tasks = transferTaskDAO.selectTask(task);
            if(tasks==null || tasks.size()==0){
                //任务还未创建
                result.setErrorCode(ErrorCodeConstant.TASK_NOT_EXIST);
            }else{
                task = tasks.get(0);
                if(task.getIs_done()==0){
                    //任务还未完成
                    result.setErrorCode(ErrorCodeConstant.TASK_NOT_DONE);
                }else{
                    String filePath = task.getFile_path();

                    //任务完成，删除任务
                    TransferTask transferTask = new TransferTask();
                    transferTask.setU_id(user.getUid());
                    transferTask.setR_id(rid);
                    transferTask.setF_id(fid);
                    transferTaskDAO.deleteTask(transferTask);

                    //更新token
                    token = updateToken(user.getUid());

                    //处理返回值
                    Map<String,String> data = new HashMap<>();
                    data.put("filePath",filePath);
                    result.success(token,data);
                }
            }

        }
        return result;
    }

    public User getUserByToken(String token) {
        Jwts jwt = Jwts.build().unpack(token);
        List<User> users = userDAO.selectUserBySign(jwt.getSign());
        if(users.size()==0||users==null){
            return null;
        }else{
            return users.get(0);
        }
    }

    public String updateToken(int uid){
        Long expiredTime = new Date().getTime()+ JwtConstants.EXPIRATION_TIME;
        userDAO.updateExpiredTime(new Timestamp(expiredTime),uid);
        return JwtUtil.generateToken(uid,expiredTime);
    }
}
