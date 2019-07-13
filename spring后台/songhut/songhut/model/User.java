package com.songhut.songhut.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户模型
 * @author Kun
 */
public class User implements Serializable {
    private Integer u_id;
    private String phone;
    private String password;
    private String email;
    private String nickname;
    private String signature;
    private Integer img;
    private Timestamp expired_time;
    private Timestamp register_time;

    public Integer getUid() {
        return u_id;
    }

    public void setUid(Integer u_id) {
        this.u_id = u_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public Timestamp getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Timestamp expired_time) {
        this.expired_time = expired_time;
    }

    public Timestamp getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Timestamp register_time) {
        this.register_time = register_time;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "User{" +
                "u_id=" + u_id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", signature='" + signature + '\'' +
                ", img=" + img +
                ", expired_time=" + expired_time +
                ", register_time=" + register_time +
                '}';
    }
}