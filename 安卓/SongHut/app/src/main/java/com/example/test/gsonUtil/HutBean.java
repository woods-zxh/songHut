package com.example.test.gsonUtil;


import android.graphics.drawable.Drawable;

/**
 * created by 卢羽帆
 */
public class HutBean {

    private String name;
    private Drawable img;

    public HutBean(String name, Drawable img){
        this.name = name;
        //this.createTime = createTime;
        this.img = img;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Drawable getImg(){
        return img;
    }

    public void setImg(Drawable img){
        this.img = img;
    }

}
