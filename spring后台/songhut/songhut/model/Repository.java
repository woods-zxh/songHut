package com.songhut.songhut.model;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 乐库模型
 * @author Kun
 */
public class Repository implements Serializable {
    private Integer r_id;
    private String name;
    private Timestamp create_time;
    private String introduce;
    private Integer is_public;
    private Integer img;
    private Integer type_1;
    private Integer type_2;
    private Integer type_3;
    private Integer type_4;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "r_id=" + r_id +
                ", name='" + name + '\'' +
                ", create_time=" + create_time +
                ", introduce='" + introduce + '\'' +
                ", is_public=" + is_public +
                '}';
    }

    public Integer getIs_public() {
        return is_public;
    }

    public void setIs_public(Integer is_public) {
        this.is_public = is_public;
    }

    public Integer getR_id() {
        return r_id;
    }

    public void setR_id(Integer r_id) {
        this.r_id = r_id;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public Integer getType_1() {
        return type_1;
    }

    public void setType_1(Integer type_1) {
        this.type_1 = type_1;
    }

    public Integer getType_2() {
        return type_2;
    }

    public void setType_2(Integer type_2) {
        this.type_2 = type_2;
    }

    public Integer getType_3() {
        return type_3;
    }

    public void setType_3(Integer type_3) {
        this.type_3 = type_3;
    }

    public Integer getType_4() {
        return type_4;
    }

    public void setType_4(Integer type_4) {
        this.type_4 = type_4;
    }
}
