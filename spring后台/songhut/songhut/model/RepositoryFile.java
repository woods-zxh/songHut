package com.songhut.songhut.model;

import io.swagger.models.auth.In;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 乐库文件关系模型
 * @author Kun
 */
public class RepositoryFile implements Serializable {
    private Integer f_id;
    private Integer r_id;

    public Integer getF_id() {
        return f_id;
    }

    public void setF_id(Integer f_id) {
        this.f_id = f_id;
    }

    public Integer getR_id() {
        return r_id;
    }

    public void setR_id(Integer r_id) {
        this.r_id = r_id;
    }

    @Override
    public String toString() {
        return "RepositoryFile{" +
                "f_id=" + f_id +
                ", r_id=" + r_id +
                '}';
    }
}
