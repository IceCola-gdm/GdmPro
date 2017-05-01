package com.gdm.musicplayer;

import android.app.Application;
import android.util.Log;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class MyApplication extends Application {
    public static boolean isLogin=false;
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
