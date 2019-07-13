package com.songhut.songhut.model;


/**
 * 乐库用户关系模型
 * @author Kun
 */
public class RepositoryUser {
    private Integer r_id;
    private Integer u_id;

    public Integer getR_id() {
        return r_id;
    }

    public void setR_id(Integer r_id) {
        this.r_id = r_id;
    }

    public Integer getU_id() {
        return u_id;
    }

    public void setU_id(Integer u_id) {
        this.u_id = u_id;
    }

    @Override
    public String toString() {
        return "RepositoryUser{" +
                "r_id=" + r_id +
                ", u_id=" + u_id +
                '}';
    }
}
