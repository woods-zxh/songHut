package com.songhut.songhut.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 评论模型
 * @author Kun
 */
public class Comment implements Serializable {
    private Integer cId;
    private Integer uId;
    private String content;
    private Timestamp createTime;

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "cId=" + cId +
                ", uId=" + uId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
