package com.gdm.musicplayer;

import android.app.Application;

import com.lzy.okhttputils.OkHttpUtils;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpUtils.init(this);
    }
}
