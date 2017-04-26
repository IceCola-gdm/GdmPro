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

import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.MusicBean;
import com.gdm.musicplayer.bean.MusicList;

import java.io.IOException;
import java.util.ArrayList;

public class MyService extends Service implements MediaPlayer.OnBufferingUpdateListener {
    private final IBinder binder = new MyBinder();
    public MediaPlayer player;
    private  ArrayList<Music> musicList;  //数据源
    private int pos=0;   //当前播放位置
    public static boolean isPlay=false;    //当前播放状态
    public void setMusicList(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intPlayer();
        initBord();
    }
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

   public class MyBinder extends Binder {
        public MyService getService(){
            return MyService.this;
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
                onPlayChangeLlistener.playComplete(pos);
            }
        });
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }
    //action=com.gdm.player
    public static String mAction="com.gdm.player";
    class MediaCommend extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mAction)) {
                String cmd = intent.getStringExtra("cmd");
                if (cmd.equals("next")) {
                    nextMusic();
                }else if(cmd.equals("play")){
                    play();
                }else if(cmd.equals("pause")){
                    pause();
                }else if(cmd.equals("stop")){
                    stop();
                }else if(cmd.equals("last")){
                    last();
                }else if(cmd.equals("type")){
                    int type = intent.getIntExtra("type", 0);
                }
            }
        }
    }

    private void last() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        pos=pos-1;
        play();
        onPlayChangeLlistener.playComplete(pos);
    }

    private void stop() {
        if (player.isPlaying()){
            player.stop();
            isPlay=false;
        }

    }

    private void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    private void play() {
        if (player.isPlaying()) {
            return;
        }else{
            try {
                String url = musicList.get(pos).getFileUrl();
                player.setDataSource(url);
                player.prepareAsync();
            } catch (IOException e) {

            }
        }
        isPlay=true;
    }

    private void nextMusic() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        pos=pos+1;
        play();
        onPlayChangeLlistener.playComplete(pos);
    }
    public interface OnPlayChangeLlistener{
        void playComplete(int pos);
    }
    private OnPlayChangeLlistener onPlayChangeLlistener=null;
    public void setOnPlayChangeLlistener(OnPlayChangeLlistener onPlayChangeLlistener){
        this.onPlayChangeLlistener=onPlayChangeLlistener;
    }
}
