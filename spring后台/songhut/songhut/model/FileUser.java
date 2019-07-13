package com.songhut.songhut.model;

/**
 * 文件用户关系模型
 * @author Kun
 */
public class FileUser {
    private Integer f_id;
    private Integer u_id;

    public Integer getF_id() {
        return f_id;
    }

    public void setF_id(Integer f_id) {
        this.f_id = f_id;
    }

    public Integer getU_id() {
        return u_id;
    }

    public void setU_id(Integer u_id) {
        this.u_id = u_id;
    }

    @Override
    public String toString() {
        return "FileUser{" +
                "f_id=" + f_id +
                ", u_id=" + u_id +
                '}';
    }
}
