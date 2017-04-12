package com.gdm.musicplayer.bean;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/4/12 0012.
 */
public class TileBean {
    private String imgpath;
    private String name;
    private Integer count;
    private Class aClass;
    public void test(){
        Context context = null;
        context.startActivity(new Intent(context,aClass));
    }
}
