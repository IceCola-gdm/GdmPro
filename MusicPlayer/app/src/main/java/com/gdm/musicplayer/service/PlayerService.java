package com.gdm.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.MusicBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class PlayerService extends Service implements MediaPlayer.OnBufferingUpdateListener {
    public static MediaPlayer player;
    private ArrayList<Music> musicList=new ArrayList<>();
    public static int SHUNXU=0;
    public static int LIEBIAOXUNHUAN=1;
    public static int SUIJI=2;
    public static int DANQU=3;
    private int state=SHUNXU;
    private int pos=0;
    private String cmd="";
    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        cmd = intent.getStringExtra("cmd");
//        pos=intent.getIntExtra("position",-1);
//        ArrayList<Music>  musics= (ArrayList<Music>) intent.getSerializableExtra("data");
//        musicList.addAll(musics);
//        switch (cmd){
//            case "play":
//                startMusic();
//                break;
//            case "stop":
//                stop();
//                break;
//            case "last":
//
//                break;
//            case "next":
//                nextMusic();
//                break;
//        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void nextMusic() {
        try{
            player.stop();
            if (state==SHUNXU) {
                pos=pos+1;
                if(pos==musicList.size()){
                    player.setDataSource(musicList.get(pos-1).getFileUrl());
                    return;
                }else {
                    player.setDataSource(musicList.get(pos).getFileUrl());
                    player.prepareAsync();
                }
            }else if(state==LIEBIAOXUNHUAN){
                pos=pos+1;
                if(pos==musicList.size()){
                    pos=0;
                    player.setDataSource(musicList.get(pos).getFileUrl());
                }else {
                    player.setDataSource(musicList.get(pos).getFileUrl());

                }
                player.prepareAsync();
            }
        }catch (IOException e){

        }
    }

    /**
     * 开始播放
     */
    public void startMusic(){
        if (player.isPlaying()) {
            return;
        }
        else {
            try {
                player.setDataSource(musicList.get(pos).getFileUrl());
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    /**
     * 停止
     */
    public  void stop(){
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
