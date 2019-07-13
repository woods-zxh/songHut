package com.songhut.songhut.model;

import com.songhut.songhut.util.ErrorCodeConstant;


/**
 * 返回值模型
 * @author Kun
 */
public class CommonResultToken {
    private String errorCode;
    private String token;
    private Object data;

    /**
     * 普通成功返回
     *
     * @param data 获取的数据
     */
    public CommonResultToken success(String token,Object data) {
        this.errorCode = ErrorCodeConstant.SUCCESS;
        this.token = token;
        this.data = data;
        return this;
    }

    public CommonResultToken tokenFail() {
        this.errorCode = ErrorCodeConstant.GLOBAL_TOKEN_EXPIRED;
        return this;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }



    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
