package com.songhut.songhut.controller;


import com.songhut.songhut.dao.*;
import com.songhut.songhut.model.*;
import com.songhut.songhut.model.File;
import com.songhut.songhut.util.*;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;



/**
 * 乐库相关接口
 * @author Kun
 */
@Controller
@Api(tags = "RepositoryController", description = "乐库管理接口")
@RequestMapping("/api/repository")
public class RepositoryController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RepositoryDAO repositoryDAO;

    @Autowired
    private RepositoryUserDAO repositoryUserDAO;

    @Autowired
    private RepositoryFileDAO repositoryFileDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private FileDAO fileDAO;

    @Autowired
    private FileUserDAO fileUserDAO;

    /**
     * 获取用户所有信息接口
     */
    @RequestMapping(value = "/getAllInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object getAllInfo(@RequestBody Map<String,Object> params) {
        //处理参数
        CommonResultToken result = new CommonResultToken();
        String token = (String)params.get("token");

        if("".equals(token.trim())){
            result.setErrorCode(ErrorCodeConstant.GLOBAL_MISSING_ARGS);
            return result;
        }

        //根据token确定user
        User user = getUserByToken(token);
        System.out.println(user);
        if(user==null){
            result.tokenFail();
        }else{
            //判断token是否过期
            if(user.getExpired_time().getTime()<new Date().getTime()){
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            }else{
                //获取用户名下所有乐库id
                List<RepositoryDetail> repositoryDetails = new ArrayList<>();
                List<Integer>rids = repositoryUserDAO.getRepositoryOfUser(user.getUid());
                if(rids!=null) {
                    //根据乐库Id获取所有乐库信息
                    for (int i = 0; i < rids.size(); i++) {
                        int rid = rids.get(i);
                        List<Repository> repositories = repositoryDAO.getRepository(rid);
                        Repository repository = repositories.get(0);
                        List<File> files = new ArrayList<>();//储存每个乐库对应的所有文件

                        //对于乐库信息，查找该乐库所有文件id
                        List<Integer>fids = repositoryFileDAO.getFileOfRepository(repository.getR_id());
                        if(fids!=null){
                            for(int j=0;j<fids.size();j++){
                                int fid = fids.get(j);
                                List<File>files1 = fileDAO.getFile(fid);
                                File file = files1.get(0);
                                files.add(file);
                            }
                        }

                        //储存乐库信息与该乐库名下所有文件
                        RepositoryDetail repositoryDetail = new RepositoryDetail(repository,files);
                        repositoryDetails.add(repositoryDetail);
                    }
                }

                //更新token
                token = updateToken(user.getUid());

                //返回值
                Map<String,Object> data = new HashMap<>();
                data.put("user",user);
                data.put("details",repositoryDetails);
                result.success(token,data);
            }
        }
        return result;
    }

    /**
     * 用户新建乐库接口
     */
    @RequestMapping(value = "/setMusicRepository", method = RequestMethod.POST)
    @ResponseBody
    public Object setMusicRepository(@RequestBody Map<String,Object> params) {
        //处理参数
        CommonResultToken result = new CommonResultToken();
        String token = (String)params.get("token");
        String name = (String)params.get("name");
        String introduce = (String)params.get("introduce");
        Integer isPublic = (Integer)params.get("isPublic");

        if("".equals(token.trim())||"".equals(name.trim())){
            result.setErrorCode(ErrorCodeConstant.GLOBAL_MISSING_ARGS);
            return result;
        }

        //根据token确定user
        User user = getUserByToken(token);
        System.out.println(user);
        if(user==null){
            result.tokenFail();
        }else{
            //判断token是否过期
            if(user.getExpired_time().getTime()<new Date().getTime()){
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            }else{
                //创建repository
                Repository repository = new Repository();
                repository.setName(name);
                repository.setIntroduce(introduce);
                repository.setCreate_time(new Timestamp(new Date().getTime()));
                repository.setIs_public(isPublic);
                repositoryDAO.addRepository(repository);

                //为用户创建乐库目录
                String dirPath = Constants.FILE_PATH_HEAD+java.io.File.separator+user.getUid()+java.io.File.separator+repository.getR_id();
                java.io.File dir = new java.io.File(dirPath);
                if(!dir.exists()){
                    dir.mkdirs();
                }

                //建立user 和 repository的关系
                repositoryUserDAO.addRepositoryUser(user.getUid(),repository.getR_id());

                //更新token
                token = updateToken(user.getUid());

                //返回值
                Map<String,Integer> data = new HashMap<>();
                data.put("rid",repository.getR_id());
                result.success(token,data);
            }
        }
        return result;
    }

    /**
     * 上传文件到乐库接口
     */
    @RequestMapping(value="/postFileToRepository")
    @ResponseBody
    public Object postFileToRepository(@RequestParam("token") String token,@RequestParam("fileType") Integer fileType,
                                         @RequestParam("upLoadFile") MultipartFile midFile,@RequestParam("rid") Integer rid,
                                         @RequestParam("fileName") String fileName, @RequestParam("introduce") String introduce,
                                         @RequestParam("isPublic") Integer isPublic, @RequestParam("isImg") Integer isImg){
        System.out.println("测试postFileToRepository");
        CommonResultToken result = new CommonResultToken();
        if("".equals(token.trim())||"".equals(fileName.trim())){
            result.setErrorCode(ErrorCodeConstant.GLOBAL_MISSING_ARGS);
            return result;
        }
        //根据token确定user
        User user = getUserByToken(token);
        System.out.println(user);
        if(user==null){
            result.tokenFail();
        }else {
            //判断token是否过期
            if (user.getExpired_time().getTime() < new Date().getTime()) {
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            } else {
                //格式为路径/用户id/乐库id/文件名
                String storePath = Constants.FILE_PATH_HEAD+ java.io.File.separator+user.getUid()+
                        java.io.File.separator+rid+java.io.File.separator+fileName;
                java.io.File targetFile = new java.io.File(storePath);
                FileOutputStream fileOutputStream = null;
                try{
                    fileOutputStream = new FileOutputStream(targetFile);
                    IOUtils.copy(midFile.getInputStream(),fileOutputStream);
                    fileOutputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                    result.setErrorCode(ErrorCodeConstant.FILE_EXECUTE_READ);
                    return result;
                }
                if(isImg==1){
                    List<Repository> repositories = repositoryDAO.getRepository(rid);
                    if(repositories==null||repositories.size()==0){
                        result.setErrorCode(ErrorCodeConstant.REPOSITORY_NOT_EXSIT);
                    }else{
                        Repository repository = repositories.get(0);
                        if(repository.getImg()!=null){
                            fileDAO.updateFile(storePath,new Timestamp(new Date().getTime()),repository.getImg());

                            //更新token
                            token = updateToken(user.getUid());
                            return result;
                        }
                    }
                }

                //存储文件信息在File模型
                File file = new File();
                file.setFilePath(storePath);
                file.setFileType(fileType);
                file.setLoad_time(new Timestamp(new Date().getTime()));
                file.setIntroduce(introduce);
                file.setIs_public(isPublic);
                fileDAO.addFile(file);

                //建立文件与用户联系
                fileUserDAO.addFileUser(user.getUid(),file.getF_id());

                //更新token
                token = updateToken(user.getUid());

                //建立文件与乐库联系
                repositoryFileDAO.addRepositoryFile(file.getF_id(),rid);

                //返回值
                Map<String,Integer> data = new HashMap<>();
                data.put("fid",file.getF_id());
                result.success(token,data);
            }
        }

        return result;
    }

    /**
     * 用户信息获取接口
     */
    @RequestMapping(value="/getUserInfo")
    @ResponseBody
    public Object getUserInfo(@RequestBody Map<String,Object> params){
        //处理参数
        CommonResultToken result = new CommonResultToken();
        String token = (String)params.get("token");

        //根据token确定user
        User user = getUserByToken(token);
        System.out.println(user);
        if(user==null){
            result.tokenFail();
        }else {
            //判断token是否过期
            if (user.getExpired_time().getTime() < new Date().getTime()) {
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            } else {
                //获取用户的repository
                List<Integer> repositories = repositoryUserDAO.getRepositoryOfUser(user.getUid());

                //更新token
                token = updateToken(user.getUid());

                //返回值
                Map<String,Object> data = new HashMap<>();
                data.put("repositories",repositories);
                data.put("user",user);
                result.success(token,data);
            }
        }
        return result;
    }


    /**
     * 乐库信息获取接口
     */
    @RequestMapping(value="/getRepositoryInfo")
    @ResponseBody
    public Object getRepositoryInfo(@RequestBody Map<String,Object> params) {
        //处理参数
        CommonResultToken result = new CommonResultToken();
        String token = (String) params.get("token");
        Integer rid = (Integer) params.get("rid");

        //根据token确定user
        User user = getUserByToken(token);
        if (user == null) {
            result.tokenFail();
        } else {
            //判断token是否过期
            if (user.getExpired_time().getTime() < new Date().getTime()) {
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            } else {
                //获取repository信息
                Repository repository = null;
                List<Repository> repositories = repositoryDAO.getRepository(rid);
                if(repositories==null||repositories.size()==0){
                    result.setErrorCode(ErrorCodeConstant.REPOSITORY_NOT_EXSIT);
                    return result;
                }else{
                    repository = repositories.get(0);
                }

                //获取乐库的files
                List<Integer> files = repositoryFileDAO.getFileOfRepository(rid);

                //更新token
                token = updateToken(user.getUid());

                //返回值
                Map<String,Object> data = new HashMap<>();
                data.put("repository",repository);
                data.put("files",files);
                result.success(token,data);
            }
        }
        return result;
    }

    /**
     * 文件信息获取接口
     */
    @RequestMapping(value="/getFileInfo")
    @ResponseBody
    public Object getFileInfo(@RequestBody Map<String,Object> params) {
        //处理参数
        CommonResultToken result = new CommonResultToken();
        String token = (String) params.get("token");
        Integer fid = (Integer) params.get("fid");

        //根据token确定user
        User user = getUserByToken(token);
        System.out.println(user);
        if (user == null) {
            result.tokenFail();
        } else {
            //判断token是否过期
            if (user.getExpired_time().getTime() < new Date().getTime()) {
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            } else {
                //获取repository信息
                File file = null;
                List<File> files = fileDAO.getFile(fid);
                if(files==null||files.size()==0){
                    result.setErrorCode(ErrorCodeConstant.REPOSITORY_NOT_EXSIT);
                    return result;
                }else{
                    file = files.get(0);
                }

                //更新token
                token = updateToken(user.getUid());

                //返回值
                Map<String,Object> data = new HashMap<>();
                data.put("file",file);
                result.success(token,data);
            }
        }
        return result;
    }

    /**
     * 文件下载接口
     */
    @RequestMapping(value="/downLoadFile")
    public void downLoadFile(@Param("token")String token,@RequestParam("filePath") String filePath,
                             @Param("rid")Integer rid,
                             HttpServletRequest request, HttpServletResponse response){
        //根据token确定user
        User user = getUserByToken(token);
        System.out.println(user);
        if (user == null) {
            return;
        } else {
            //判断token是否过期
            if (user.getExpired_time().getTime() < new Date().getTime()) {
                return;
            } else {
                //验证文件是否
                int uid = user.getUid();

                //下载文件
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=downloadFile");// 设置文件名

                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(filePath);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //更新token
                updateToken(user.getUid());
            }
        }

    }

    /**
     * 文件类型设置
     */
    @RequestMapping(value="/setFileType")
    @ResponseBody
    public Object setFileType(@RequestBody Map<String,Object> params) {
        CommonResultToken result = new CommonResultToken();
        //处理参数
        String token = (String) params.get("token");
        Integer rid = (Integer) params.get("rid");
        Integer fid = (Integer) params.get("fid");
        Integer type = (Integer) params.get("type");

        //根据token确定user
        User user = getUserByToken(token);
        if (user == null) {
            result.tokenFail();
        } else {
            //判断token是否过期
            if (user.getExpired_time().getTime() < new Date().getTime()) {
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            } else {
                //判断rid与用户关系
                List<RepositoryUser> repositoryUsers = repositoryUserDAO.getRelation(user.getUid(),rid);
                if(repositoryUsers==null||repositoryUsers.size()==0){
                    result.setErrorCode(ErrorCodeConstant.GLOBAL_UNAUTHORIZE);
                }else{

                    //判断文件是否存在
                    List<File> files = fileDAO.getFile(fid);
                    if(files==null||files.size()==0){
                        result.setErrorCode(ErrorCodeConstant.FILE_NOT_EXSIT);
                    }else{
                        //判断文件类型是否符合要求
                        File file = files.get(0);
                        if(file.getFileType()!=type){
                            result.setErrorCode(ErrorCodeConstant.FILE_TYPE_ERROR);
                        }else {
                            //对应地更新repository中的同类型演示文件
                            switch (type){
                                case 1:
                                    repositoryDAO.updateFile1(fid,rid);
                                    break;
                                case 2:
                                    repositoryDAO.updateFile2(fid,rid);
                                    break;
                                case 3:
                                    repositoryDAO.updateFile3(fid,rid);
                                    break;
                                case 4:
                                    repositoryDAO.updateFile4(fid,rid);
                                    break;
                            }

                            //更新token
                            token = updateToken(user.getUid());

                            //返回值
                            result.success(token, null);
                        }
                    }
                }
            }
        }
        return result;
    }



    @RequestMapping(value="/mergeRepository")
    @ResponseBody
    public Object mergeRepository(@RequestBody Map<String,Object> params) {
        CommonResultToken result = new CommonResultToken();


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
