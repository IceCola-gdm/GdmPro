package com.gdm.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.gdm.musicplayer.bean.MusicBean;

import java.io.IOException;
import java.util.ArrayList;

public class Player extends Service implements MediaPlayer.OnBufferingUpdateListener {
    private static MediaPlayer player;
    private ArrayList<MusicBean> musicList;
    public static int SHUNXU=0;
    public static int LIEBIAOXUNHUAN=1;
    public static int SUIJI=2;
    public static int DANQU=3;
    private int state=SHUNXU;
    private int pos=0;
    public Player() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player=new MediaPlayer();
        player.setOnBufferingUpdateListener(this);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });
    }

    private void nextMusic() {
        try{
            player.stop();
            if (state==SHUNXU) {
                pos=pos+1;
                if(pos==musicList.size()){
                    player.setDataSource(musicList.get(pos-1).getPath());
                    return;
                }else {
                    player.setDataSource(musicList.get(pos).getPath());
                    player.prepareAsync();
                }
            }else if(state==LIEBIAOXUNHUAN){
                pos=pos+1;
                if(pos==musicList.size()){
                    pos=0;
                    player.setDataSource(musicList.get(pos).getPath());
                }else {
                    player.setDataSource(musicList.get(pos).getPath());

                }
                player.prepareAsync();
            }
        }catch (IOException e){

        }
    }

    /**
     * 开始播放
     */
    public static void startMusic(){
        if (player.isPlaying()) {
            return;
        }
        else {
            player.prepareAsync();
        }
    }
    public void next(){

    }
    /**
     * 停止
     */
    public static void stop(){
        if (player.isPlaying()) {
            player.stop();
        }
    }
    /**
     * 暂停
     */
    public static void pause(){
        if (player.isPlaying()) {
            player.pause();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }
}
