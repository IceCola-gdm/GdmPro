package com.gdm.musicplayer.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.MusicUtil;
import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class MyApplication extends Application {
    public static boolean playFlag=false;
    private boolean isLogin=false;
    private ArrayList<Music> musics;
    public static final String CHANGELIST="com.gdm.listchange";
    private  User user=null;
    private SharedPreferences sp;
    private static MyApplication instance;
    public static int cid;
    public static int lid;
    public static final String BASEPATH="http://120.24.220.119:8080/music/";
    public static final String BASEMUSICPATH="http://120.24.220.119:8080/music/data/music/";  //音乐文件
    public static final String BASEMUSICIIMGPATH="http://120.24.220.119:8080/music/data/music/img/"; //音乐图片
    public static final String BASELRCPATH="http://120.24.220.119:8080/music/data/music/lrc/";//歌词文件
    public static final String BASEMVPATH="http://120.24.220.119:8080/music/data/mv/";//mv文件
    @Override
    public void onCreate() {
        super.onCreate();
        musics=new ArrayList<>();
        musics.addAll((ArrayList<Music>) MusicUtil.getAllSongs(this,"song"));
        sp=getSharedPreferences("myaccount",MODE_PRIVATE);
        user=new User();
        instance=this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public void setSp(SharedPreferences sp) {
        this.sp = sp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        sendBroadcast(new Intent("userinfochange"));
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
        Intent intent = new Intent(CHANGELIST);
        sendBroadcast(intent);
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }
}
