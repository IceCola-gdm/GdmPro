package com.gdm.musicplayer;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.gdm.musicplayer.activities.MainActivity;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.MusicList;
import com.gdm.musicplayer.utils.MusicUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class MyApplication extends Application {
    public static boolean isLogin=false;
    private ArrayList<Music> musics;
    public static final String CHANGELIST="com.gdm.listchange";
    @Override
    public void onCreate() {
        super.onCreate();
        musics=new ArrayList<>();
        musics.addAll((ArrayList<Music>) MusicUtil.getAllSongs(this,"song"));
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
