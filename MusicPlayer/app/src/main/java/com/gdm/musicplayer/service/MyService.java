package com.gdm.musicplayer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.MusicBean;
import com.gdm.musicplayer.bean.MusicList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MyService extends Service implements MediaPlayer.OnBufferingUpdateListener {

    public MediaPlayer player;
    //数据源
    private  static ArrayList<Music> musicList;
    //当前播放位置
    private int pos=0;
    //播放类型
    private static int type=0;
    //列表循环
    public static final int LIST_RECYCLE=1;
    //单曲循环
    public static final int ONE_MUSIC=2;
    //随机播放
    public static final int RANDOM_PLAY=3;
    //顺序播放
    public static final int LIST_PLAY=0;
    private boolean isPlay;

    public static void setType(int type) {
        MyService.type = type;
    }

    public static int getType() {
        return type;
    }

    public static void setMusicList(ArrayList<Music> musicList) {
        MyService.musicList = musicList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intPlayer();
        initBord();
        initTheard();
    }

    private void initTheard() {
        new Thread(new PlayStateTheard()).start();
    }

    class PlayStateTheard implements Runnable{
        /**
         * 发送播放状态 以及播放的音乐信息
         */
        @Override
        public void run() {
            while (true){
                if (player.isPlaying()) {
                    Music bean = musicList.get(pos);
                    Intent intent = new Intent(PLAY_ACTION);
                    intent.putExtra("state","play");
                    intent.putExtra("pos",pos);
                    intent.putExtra("total",player.getDuration());
                    intent.putExtra("now",player.getCurrentPosition());
                    intent.putExtra("title",bean.getName());
                    sendBroadcast(intent);
                }else{
                    Intent intent = new Intent(PLAY_ACTION);
                    intent.putExtra("state","stop");
                    intent.putExtra("pos",pos);
                    sendBroadcast(intent);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private MediaCommend commend;
    private void initBord() {
        IntentFilter filter=new IntentFilter(mAction);
        commend=new MediaCommend();
        registerReceiver(commend,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(commend);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void intPlayer() {
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
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }
    //action=com.gdm.player
    public static String mAction="com.gdm.player";
    public static final String PLAY_ACTION="com.gdm.playinfo";
    class MediaCommend extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mAction)) {
                String cmd = intent.getStringExtra("cmd");
                if (cmd.equals("next")) {
                    nextMusic();
                }else if(cmd.equals("play")){
                    if (player.isPlaying()) {
                        pause();
                    }else {
                        play();
                    }

                }else if(cmd.equals("pause")){
                    pause();
                }else if(cmd.equals("stop")){
                    stop();
                }else if(cmd.equals("last")){
                    last();
                }else if(cmd.equals("type")){
                    type = intent.getIntExtra("type", 0);
                }
            }
        }
    }

    private void last() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        if (pos!=0) {
            pos=pos-1;
        }else{
            pos=musicList.size()-1;
        }
        play();
    }

    private void stop() {
        if (player.isPlaying()){
            player.stop();
            isPlay=false;
        }

        player.reset();
    }
    private int nowPos=-1;
    private void pause() {
        if (player.isPlaying()) {
            player.pause();
            nowPos=player.getCurrentPosition();
        }
    }

    private void play() {
        if (player.isPlaying()) {
            return;
        }else{
            if (nowPos!=-1){
                player.start();
                player.seekTo(nowPos);
                nowPos=-1;
            }else
            {
                try {
                    Music bean = musicList.get(pos);
                    player.setDataSource(bean.getFileUrl());
                    player.prepareAsync();

                } catch (IOException e) {

                }
            }

        }
        isPlay=true;
    }
    private int tag=0;
    private void nextMusic() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        switch (type) {
            case LIST_RECYCLE:
                if (pos!=musicList.size()-1) {
                    pos=pos+1;
                }else{
                    pos=0;
                }
                break;
            case ONE_MUSIC:
                break;
            case RANDOM_PLAY:
                pos = new Random().nextInt(musicList.size());
                break;
            case LIST_PLAY:
                if (pos!=musicList.size()-1) {
                    pos=pos+1;
                }else {
                    tag=tag+1;
                    if (tag>1) {
                        pos=0;
                    }
                }
                break;
        }
        play();
    }
}
