package com.gdm.musicplayer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.Music;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class MyService extends Service implements MediaPlayer.OnBufferingUpdateListener {
    public static ArrayList<Music> getMusicList() {
        return musicList;
    }
    public static void setMusicList(ArrayList<Music> musicList) {
        MyService.musicList = musicList;
    }
    public MediaPlayer player;
    //数据源
    private  static ArrayList<Music> musicList=new ArrayList<>();
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
    public static boolean isPlay=false;
    private SharedPreferences sp;
    private ArrayList<Music> temp=new ArrayList<>();
    public static void setType(int type) {
        MyService.type = type;
    }

    public static int getType() {
        return type;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intPlayer();
        initBord();
        initTheard();
        sp=getSharedPreferences("recently",MODE_PRIVATE);
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
            while (true) {
                if (musicList != null && musicList.size() != 0) {
                    if (player.isPlaying()) {
                        Music bean = musicList.get(pos);
                        Intent intent = new Intent(PLAY_ACTION);
                        intent.putExtra("music",bean);
                        intent.putExtra("state", "play");
                        intent.putExtra("pos", pos);
                        if (bean.getSinger()!=null) {
                            intent.putExtra("author",bean.getSinger());
                        }else {
                            intent.putExtra("author","");
                        }

                        intent.putExtra("total", player.getDuration());
                        intent.putExtra("now", player.getCurrentPosition());
                        intent.putExtra("title", bean.getName());
                        sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(PLAY_ACTION);
                        intent.putExtra("state", "stop");
                        intent.putExtra("pos", pos);
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
    }
    private MediaCommend commend;
    private void initBord() {
        IntentFilter filter=new IntentFilter(mAction);
        filter.addAction(MyApplication.CHANGELIST);
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
                Music bean = musicList.get(pos);
                Intent intent = new Intent(PLAY_ACTION);
                intent.putExtra("state", "next");
                intent.putExtra("pos", pos);
                intent.putExtra("total", player.getDuration());
                intent.putExtra("now", 0);
                intent.putExtra("title", bean.getName());
                sendBroadcast(intent);
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
                if(cmd!=null&&cmd!=""){
                    if (cmd.equals("next")) {//下一首命令
                        nextMusic();
                    }else if(cmd.equals("play")){//播放命令
                        if (player.isPlaying()) {
                            pause();
                        }else {
                            play();
                        }
                    }else if(cmd.equals("pause")){//暂停命令
//                    ToastUtil.toast(MyService.this,"pause");
                        pause();
                    }else if(cmd.equals("stop")){//停止命令
                        stop();
                    }else if(cmd.equals("last")){//上一首命令
                        last();
                    }else if(cmd.equals("type")){//设置播放类型
                        type = intent.getIntExtra("type", 0);
                    }else if(cmd.equals("seek_stop")){//拖动时要先暂停播放
                        player.pause();
                    }else if(cmd.equals("seek_pos")){//拖动完成之后
                        int pos = intent.getIntExtra("pos", 0);
                        player.seekTo(pos);
                        if (!player.isPlaying()) {
                            player.start();
                        }
                    }else if(cmd.equals("chose_pos")){//从列表位置开始播放
                        stop();
                        int pos = intent.getIntExtra("pos", 0);
                        ArrayList musics = (ArrayList) intent.getSerializableExtra("data");
                        if(musicList!=null){
                            musicList.clear();
                        }
                        if(musics!=null){
                            musicList.addAll(musics);
                        }
                        MyService.this.pos=pos;
                        play();
                    }
                }

            }else if(intent.getAction().equals(MyApplication.CHANGELIST)){
                MyApplication ap= (MyApplication) getApplication();
                musicList=ap.getMusics();
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
        isPlay=false;
    }

    private void play() {
        if (player.isPlaying()) {
            return;
        }else{
            if (nowPos!=-1){
                player.seekTo(nowPos);
                player.start();
                nowPos=-1;
            }else
            {
                try {
                    Music bean = musicList.get(pos);
                    if (bean.isnet()) {
                        if (!checkNet()) {
                            Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    player.setDataSource(bean.getFileUrl());
                    player.prepare();
                    temp.add(bean);

                    SharedPreferences.Editor editor = sp.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(temp);
                    editor.putString("musics","");
                    editor.putString("musics",json);
                    editor.commit();
                } catch (Exception e) {
                }
            }
        }
        isPlay=true;
    }

    private boolean checkNet() {
        return isNetworkConnected(this);
    }
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
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
