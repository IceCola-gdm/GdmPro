package com.gdm.musicplayer;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.MusicUtil;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class MyApplication extends Application {
    private boolean isLogin=false;
    private ArrayList<Music> musics;
    public static final String CHANGELIST="com.gdm.listchange";
    private  User user=null;
    private SharedPreferences sp;
    @Override
    public void onCreate() {
        super.onCreate();
        musics=new ArrayList<>();
        musics.addAll((ArrayList<Music>) MusicUtil.getAllSongs(this,"song"));
        sp=getSharedPreferences("myaccount",MODE_PRIVATE);
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
