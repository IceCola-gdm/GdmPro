package com.gdm.musicplayer.bean;

import java.io.Serializable;

/**
 * Created by 10789 on 2017-04-30.
 */

public class MList implements Serializable{
    private int id;//id
    private int userid;//用户id
    private String name;//列表名字
    private String discription;//列表描述
    private String imgpath;//图片路径
    private int type;//类型

    public MList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
