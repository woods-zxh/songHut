package com.songhut.songhut.controller;


import com.songhut.songhut.dao.FileDAO;
import com.songhut.songhut.dao.FileUserDAO;
import com.songhut.songhut.dao.UserDAO;
import com.songhut.songhut.model.CommonResultToken;
import com.songhut.songhut.model.File;
import com.songhut.songhut.model.User;
import com.songhut.songhut.util.*;
import com.yunpian.sdk.YunpianException;
import io.swagger.annotations.Api;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static com.yunpian.sdk.util.HttpUtil.post;

/**
 * 用户相关接口
 * @author Kun
 */
@Controller
@Api(tags = "UserController", description = "用户管理接口")
@RequestMapping("/api/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private FileDAO fileDAO;

    @Autowired
    private FileUserDAO fileUserDAO;


    /**
     * 用户登录接口
     */
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    @ResponseBody
    public Object signIn(@RequestBody Map<String,Object> params) {
        String phone = (String)params.get("phone");
        String password = (String)params.get("password");
        logger.info("测试signIn");
        CommonResultToken result = new CommonResultToken();
        if("".equals(phone.trim())||"".equals(password.trim())){
            result.setErrorCode(ErrorCodeConstant.GLOBAL_MISSING_ARGS);
            return result;
        }

        List<User>users = userDAO.selectUserByPhone(phone);
        if(users.size()==0){
            result.setErrorCode(ErrorCodeConstant.LOCAL_WRONG_LOGIN);

        }else{
            User user = users.get(0);
            String md5_password = Md5.md5(password);
            if(user.getPassword().equals(md5_password)){
                logger.info(user.toString());
                String token = updateToken(user.getUid());
                result.success(token,null);
            }else{
                result.setErrorCode(ErrorCodeConstant.LOCAL_WRONG_LOGIN);
            }
        }
        return result;
    }

    /**
     * 用户注册接口
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    @ResponseBody
    public Object signUp(@RequestBody Map<String,Object> params) {
        String reqIP = IPUtil.getRealIP(request);
        logger.info("来自ip {} 请求注册", reqIP);
        String phone = (String)params.get("phone");
        String password = (String)params.get("password");
        String captcha = (String)params.get("captcha");
        logger.info("测试signUp");
        CommonResultToken result = new CommonResultToken();
        if("".equals(phone.trim())||"".equals(password.trim())||"".equals(captcha.trim())){
            result.setErrorCode(ErrorCodeConstant.GLOBAL_MISSING_ARGS);
            return result;
        }
        if(!isValidPassword(password)){
            result.setErrorCode(ErrorCodeConstant.LOCAL_INVALID_PASSWORD);
            return result;
        }
        if(userDAO.selectCount(phone)!=0){
            result.setErrorCode(ErrorCodeConstant.LOCAL_PHONE_USED);
            return result;
        }

        if(phoneMatchCaptcha(phone,captcha,result)){
            password = Md5.md5(password);
            User user = new User();
            user.setPhone(phone);
            user.setPassword(password);
            user.setRegister_time(new Timestamp(new Date().getTime()));
            userDAO.addUser(user);

            List<User> users = userDAO.selectUserByPhone(phone);
            if(users.size()!=1) {
                result.setErrorCode(ErrorCodeConstant.GLOBAL_UNAUTHORIZE);
                return result;
            }else{
                user = users.get(0);
            }

            //为用户创建文件夹
            String dirPath = Constants.FILE_PATH_HEAD+java.io.File.separator+user.getUid();
            java.io.File dir = new java.io.File(dirPath);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String token = addTokenByUid(user.getUid());
            result.success(token,null);
            return result;
        }else{
            return result;
        }
    }

    /**
     * 用户信息获取
     */
    @RequestMapping(value="/setUserInfo")
    @ResponseBody
    public Object setUserInfo(@RequestBody Map<String,Object> params) {
        //处理参数
        CommonResultToken result = new CommonResultToken();
        String token = (String) params.get("token");
        String email = (String) params.get("email");
        String nickname = (String) params.get("nickname");

        //根据token确定user
        User user = getUserByToken(token);
        if (user == null) {
            result.tokenFail();
        } else {
            //判断token是否过期
            if (user.getExpired_time().getTime() < new Date().getTime()) {
                result.setErrorCode(ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED);
            } else {
                //更新用户信息
                User userInfo = new User();
                userInfo.setEmail(email);
                userInfo.setNickname(nickname);
                userInfo.setUid(user.getUid());
                userDAO.updateUser(userInfo);

                //更新token
                token = updateToken(user.getUid());

                result.success(token,null);
            }
        }
        return result;
    }

    /**
     * 用户头像上传
     */
    @RequestMapping(value="/postProfile")
    @ResponseBody
    public Object postProfile(@RequestParam("token") String token,
                              @RequestParam("fileName") String fileName,@RequestParam("upLoadFile") MultipartFile midFile){
        System.out.println("测试postProfile");
        CommonResultToken result = new CommonResultToken();
        if("".equals(token.trim())){
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
                        java.io.File.separator+fileName;
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
                File file;
                if(user.getImg()==null) {
                    //存储文件信息在File模型
                    file = new File();
                    file.setFilePath(storePath);
                    file.setFileType(Constants.IMG);
                    file.setLoad_time(new Timestamp(new Date().getTime()));
                    file.setIntroduce("");
                    file.setIs_public(Constants.PUBLIC);
                    fileDAO.addFile(file);

                    //建立文件与用户联系
                    fileUserDAO.addFileUser(user.getUid(), file.getF_id());

                    //修改用户img
                    userDAO.updateImg(file.getF_id(),user.getUid());
                }else{
                    List<File> files = fileDAO.getFile(user.getImg());
                    file = files.get(0);
                    file.setFilePath(storePath);
                }

                //更新token
                token = updateToken(user.getUid());

                //返回值
                Map<String,Integer> data = new HashMap<>();
                data.put("fid",file.getF_id());
                result.success(token,data);
            }
        }

        return result;
    }

    /**
     * 智能匹配模版接口发短信
     *
     * @param apikey    apikey
     * @param text   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    private String sendSms(String apikey, String text, String mobile) throws IOException, YunpianException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", text);
        params.put("mobile", mobile);
        //智能匹配模版发送接口的http地址
        String URI_SEND_SMS = "https://sms.yunpian.com/v2/sms/single_send.json";
        return post(URI_SEND_SMS, params);
    }

    /**
     * 生成由0-9组成的数字验证码
     * @param length 生成长度
     * @return 验证码
     */
    private String generateCaptcha(int length) {
        char[] captcha = new char[length];
        Random random = new Random();
        int number;
        for (int i=0; i<length; i++) {
            number = random.nextInt(10);
            captcha[i] = (char) ('0'+number);
        }
        return new String(captcha);
    }


    /**
     * 判断密码是否合法
     * @param password
     * @return
     */
    private boolean isValidPassword(String password) {
        if(password.length()<7){
            return false;
        }
        return true;
    }

    /**
     * 判断手机号是否匹配验证码
     * @param phone
     * @param captcha
     * @param result
     * @return
     */
    private boolean phoneMatchCaptcha(String phone, String captcha, CommonResultToken result) {
//        List<PhoneCaptcha> phoneCaptchaList = phoneCaptchaDAO.selectPhoneCaptcha(phone, captcha);
//        if (phoneCaptchaList.isEmpty()) {
//            result.setErrorCode(ErrorCodeConstant.LOCAL_CAPTCHA_WRONG);
//            return false;
//        }else {
//            PhoneCaptcha phoneCaptcha = phoneCaptchaList.get(0);
//            System.out.println(phoneCaptcha);
//            if (isCaptchaExpired(phoneCaptcha.getInTime())){
//                result.setErrorCode(ErrorCodeConstant.LOCAL_CAPTCHA_EXPIRED);
//                return false;
//            }
//            return true;
//        }
        return true;
    }

//    /**
//     * 判断验证码是否过期
//     * @param inTime 验证码的发送时间
//     */
//    private boolean isCaptchaExpired(Timestamp inTime) {
//        long currentTime=System.currentTimeMillis();
//        System.out.println((currentTime - inTime.getTime())/(1000*60));
//        return (currentTime - inTime.getTime())/(1000*60)>CAPTCHA_DURATION_MINUTE;
//    }

    private User findUserByPhone(String phone) {
        List<User> users= userDAO.selectUserByPhone(phone);
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }


    /**
     * 添加用户的signature
     * @param uid
     * @return
     */
    private String addTokenByUid(Integer uid) {
        String token = JwtUtil.generateToken(uid);
        logger.info(token);
        Jwts jwt = Jwts.build().unpack(token);
        userDAO.updateToken(jwt.getSign(),uid);
        userDAO.updateExpiredTime(new Timestamp(
                jwt.getClaim().get(JwtUtil.GENERATE_TIME)),uid);
        return token;
    }

    /**
     * 更新用户token，接收用户id，返回更新后的token
     * @param uid
     * @return
     */
    public String updateToken(int uid){
        Long expiredTime = new Date().getTime()+JwtConstants.EXPIRATION_TIME;
        userDAO.updateExpiredTime(new Timestamp(expiredTime),uid);
        return JwtUtil.generateToken(uid,expiredTime);
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
}
