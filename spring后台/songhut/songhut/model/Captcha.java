package com.songhut.songhut.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 验证码模型
 * @author Kun
 */
public class Captcha implements Serializable {
    private String phone;
    private String captcha;
    private Timestamp expiredTime;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return "Captcha{" +
                "phone='" + phone + '\'' +
                ", captcha='" + captcha + '\'' +
                ", expired_time=" + expiredTime +
                '}';
    }

    public Timestamp getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Timestamp expiredTime) {
        this.expiredTime = expiredTime;
    }
}
