package com.songhut.songhut.util;


/**
 * 错误码定义
 * @author Kun
 */
public class ErrorCodeConstant {
    private ErrorCodeConstant(){}

    /**
     * 成功
      */
    public static final String SUCCESS = "0000";


    public static final String CAPTCHA_GET_FAIL = "0104";


    /**
     * 用户注册错误
     */
    public static final String LOCAL_PHONE_USED = "0101";
    public static final String LOCAL_CAPTCHA_WRONG = "0102";
    public static final String LOCAL_CAPTCHA_EXPIRED = "0103";
    public static final String LOCAL_INVALID_PASSWORD = "0104";

    /**
     * 用户登录错误
     */
    public static final String LOCAL_WRONG_LOGIN = "0201";


    public static final String LOCAL_STUID_WRONG = "0301";

    public static final String LOCAL_UNMATCHED_PHONE = "0401";

    /**
     * 全局错误
     */
    public static final String GLOBAL_UNAUTHORIZE = "0001";
    public static final String GLOBAL_TOKEN_EXPIRED = "0002";
    public static final String GLOBAL_MISSING_ARGS = "0003";


    /**
     * 乐库相关错误
     */
    public static final String FILE_EXECUTE_READ = "0501";
    public static final String REPOSITORY_NOT_EXSIT = "0502";
    public static final String FILE_NOT_EXSIT = "0503";
    public static final String FILE_TYPE_ERROR = "0504";


    /**
     * 哼唱转旋律错误
     */
    public static final String TASK_EXISTED = "0601";
    public static final String TASK_NOT_EXIST = "0602";
    public static final String TASK_NOT_DONE = "0603";

    public static final String PROJECT_ADD_MEMBER_FULL = "0502";
    public static final String PROJECT_ADD_MEMBER_EXISTED = "0503";
    public static final String PROJECT_DELETE_MEMBER_NONEXISTED = "0504";
    public static final String PROJECT_ADD_TEACHER_FULL = "0505";

    public static final String USER_NONEXISTED = "0601";
    public static final String USER_IS_OWNER = "0602";


}
