package com.songhut.songhut.model;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 文件模型
 * @author Kun
 */
public class File implements Serializable {
    private Integer f_id;
    private String filePath;
    private Integer fileType;
    private Timestamp load_time;
    private String introduce;
    private Integer is_public;


    public Integer getF_id() {
        return f_id;
    }

    public void setF_id(Integer fId) {
        this.f_id = fId;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Timestamp getLoad_time() {
        return load_time;
    }

    public void setLoad_time(Timestamp loadTime) {
        this.load_time = loadTime;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getIs_public() {
        return is_public;
    }

    public void setIs_public(Integer is_public) {
        this.is_public = is_public;
    }

    @Override
    public String toString() {
        return "File{" +
                "f_id=" + f_id +
                ", filePath='" + filePath + '\'' +
                ", fileType=" + fileType +
                ", load_time=" + load_time +
                ", introduce='" + introduce + '\'' +
                ", is_public=" + is_public +
                '}';
    }
}
