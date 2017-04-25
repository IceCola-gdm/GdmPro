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

import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.MusicBean;
import com.gdm.musicplayer.bean.MusicList;

import java.io.IOException;
import java.util.ArrayList;

public class MyService extends Service implements MediaPlayer.OnBufferingUpdateListener {
    private final IBinder binder = new MyBinder();
    public MediaPlayer player;
    //数据源
    private  ArrayList<Music> musicList;
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

    public static void setType(int type) {
        MyService.type = type;
    }

    public static int getType() {
        return type;
    }

    public static void setMusicList(ArrayList<MusicBean> musicList) {
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

    }

    class PlayStateTheard implements Runnable{

        @Override
        public void run() {
            while (true){
                if (player.isPlaying()) {
                    MusicBean bean = musicList.get(pos);
                    Intent intent = new Intent(PLAY_ACTION);
                    intent.putExtra("total",player.getDuration());
                    intent.putExtra("now",player.getCurrentPosition());
                    intent.putExtra("title",bean.getTitle());
                    intent.putExtra("icon",bean.getIcon());
                    intent.putExtra("alter",bean.getAlter());
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
    public static final String PLAY_ACTION="com.gdm.playinfo";
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
        player.stop();
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
                MusicBean bean = musicList.get(pos);
                player.setDataSource(bean.getPath());
                player.prepareAsync();

            } catch (IOException e) {

            }
        }
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
