package com.songhut.songhut.model;

import java.io.Serializable;


/**
 * 转换任务模型
 * @author Kun
 */
public class TransferTask implements Serializable {
    private Integer r_id;
    private Integer u_id;
    private Integer f_id;
    private int is_done;
    private String file_path;

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

    public Integer getF_id() {
        return f_id;
    }

    public void setF_id(Integer f_id) {
        this.f_id = f_id;
    }

    public int getIs_done() {
        return is_done;
    }

    public void setIs_done(int is_done) {
        this.is_done = is_done;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    @Override
    public String toString() {
        return "TransferTask{" +
                "r_id=" + r_id +
                ", u_id=" + u_id +
                ", f_id=" + f_id +
                ", is_done=" + is_done +
                ", file_path='" + file_path + '\'' +
                '}';
    }
}
