package com.gdm.musicplayer.bean;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/4/12 0012.
 * fragment_my
 */
public class MusicList {
        private int imgPath;
        private String title;
        private String num;
        private Class mClass;
        private int type;//头部为0 内容为1
    public void setmClass(Class mClass) {
        this.mClass = mClass;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Class getmClass() {
        return mClass;
    }
    public int getImgPath() {
            return imgPath;
        }

    public void setImgPath(int imgPath) {
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

}
