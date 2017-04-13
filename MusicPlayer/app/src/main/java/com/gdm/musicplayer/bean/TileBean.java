package com.gdm.musicplayer.bean;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/4/12 0012.
 * fragment_my
 */
public class TileBean {
    private String imgPath;
    private String title;
    private String num;

    public TileBean(String imgPath, String num, String title) {
        this.imgPath = imgPath;
        this.num = num;
        this.title = title;
    }

    public TileBean() {
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TileBean{" +
                "imgPath='" + imgPath + '\'' +
                ", title='" + title + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
