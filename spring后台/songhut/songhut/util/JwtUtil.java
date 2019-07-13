package com.songhut.songhut.util;

import com.songhut.songhut.model.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * JWT工具
 * @author Kun
 */
@Component
public class JwtUtil {

    public final static String GENERATE_TIME = "generateTime";
    //开头为jwt加密方式,中间claim为放入信息，末尾为MD5加密签名，"."分隔
    public static String generateToken(Integer uid){
        Date generateTime = new Date();
        HashMap<String,Long> map = new HashMap<>();
        map.put(GENERATE_TIME,(generateTime.getTime()+ JwtConstants.EXPIRATION_TIME));
        String jwt = Jwts.build()
                .setHead(JwtConstants.TOKEN_SECRET)
                .setClaim(map)
                .setSign(uid, JwtConstants.TOKEN_SECRET)
                .contact();
        return jwt;
    }

    public static String generateToken(Integer uid,Long expired_time){
        HashMap<String,Long> map = new HashMap<>();
        map.put(GENERATE_TIME,expired_time);
        String jwt = Jwts.build()
                .setHead(JwtConstants.TOKEN_SECRET)
                .setClaim(map)
                .setSign(uid, JwtConstants.TOKEN_SECRET)
                .contact();
        return jwt;
    }

    /*
        Use this method after checking whether the token is valid
     */
    public static User getUserByToken(String token){
        String sign = Jwts.build().unpack(token).getSign();
        int uid = getUidBySign(sign);
        //todo:select user's info by uid
        User user = new User();
        user.setUid(uid);
        return user;
    }
    public static boolean isValid(String token){
        //return true;
        Jwts jwt = Jwts.build().unpack(token);
        Date now = new Date();
        if(jwt.getHead().equals(JwtConstants.TOKEN_SECRET)){
            Map<String,Long> map = jwt.getClaim();
            System.out.println(now.getTime());
            if(map.get(GENERATE_TIME)>=now.getTime()){
                if(hasExisted(jwt.getSign())){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }



    //数据库查询签名
    public static boolean hasExisted(String sign) {
        if(getUidBySign(sign)==-1)
            return false;
        return true;
    }

    public static int getUidBySign(String sign){
        //todo:find out the uid corresponding to the sign
        int uid = 123;
        return uid;
    }


}
