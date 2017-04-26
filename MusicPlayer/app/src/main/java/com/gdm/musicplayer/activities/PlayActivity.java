package com.gdm.musicplayer.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MenuAdapter;
import com.gdm.musicplayer.adapter.MyPagerAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.fragments.FragmentLyric;
import com.gdm.musicplayer.fragments.FragmentPlay;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.service.PlayerService;
import com.gdm.musicplayer.utils.ToastUtil;
import com.gdm.musicplayer.view.MyViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class PlayActivity extends AppCompatActivity  {
    private ImageView imgBack;
    private ImageView imgShare;
    private ImageView imgPlay;
    private ViewPager viewPager;
    private ArrayList<Fragment> fgs=new ArrayList<>();
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private TextView tvSongName;
    private TextView tvSongSinger;
    private SeekBar seekBar;
    private MyPagerAdapter adapter;
    private ArrayList<Music> musics=new ArrayList<>();
    private int currentIndex=-1;
    private Music music=null;
    private MyBrod brod;
    private MenuAdapter adapter2=null;
    private String state="stop";
    private String title;
    private int pos;
    private int total;
    private int now;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getIntentData();
        initView();
        initData();
        setAdapter();
        brod=new MyBrod();
        IntentFilter filter=new IntentFilter(MyService.PLAY_ACTION);
        registerReceiver(brod,filter);
        setListener();
    }
    private class MyBrod extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyService.PLAY_ACTION)) {
                state=intent.getStringExtra("state");
                if (state.equals("play")) {
                    total = intent.getIntExtra("total", 0);
                    now = intent.getIntExtra("now", 0);
                    pos = intent.getIntExtra("pos", 0);
                    title = intent.getStringExtra("title");
                    Log.e("PlayActivity","正在播放"+"total="+total+"  now="+now+"  title"+title);
                }else if(state.equals("stop")){
                    pos=intent.getIntExtra("pos", 0);
                }
            }
        }
    }
    private void setListener() {
        seekBar.setOnSeekBarChangeListener(new MyListener());
    }

    private void getIntentData() {
        Intent intent = getIntent();
        currentIndex=intent.getIntExtra("position",-1);
        ArrayList<Music> m = (ArrayList<Music>) intent.getSerializableExtra("data");
        musics.addAll(m);
    }

    private void setAdapter() {
        adapter=new MyPagerAdapter(getSupportFragmentManager(),fgs);
        viewPager.setAdapter(adapter);
    }

    private void initData() {
        fgs.add(new FragmentPlay());
        fgs.add(new FragmentLyric());
        music = musics.get(currentIndex);
        tvSongSinger.setText(music.getSinger());
        tvSongName.setText(music.getName());
        tvTotalTime.setText(music.getDuration()+"");
        adapter2=new MenuAdapter(musics,PlayActivity.this);
    }

    private void initView() {
        imgBack= (ImageView) findViewById(R.id.img_play_back);
        imgShare= (ImageView) findViewById(R.id.img_play_share);
        viewPager= (ViewPager) findViewById(R.id.vp_play);
        tvCurrentTime= (TextView) findViewById(R.id.tv_currenttime);
        tvTotalTime= (TextView) findViewById(R.id.tv_totaltime);
        tvSongName= (TextView) findViewById(R.id.tv_play_song_name);
        tvSongSinger= (TextView) findViewById(R.id.tv_play_song_info);
        seekBar= (SeekBar) findViewById(R.id.seekbar);
        imgPlay= (ImageView) findViewById(R.id.img_play_play);
        if(MainActivity.state.equals("stop")){
            imgPlay.setImageResource(R.drawable.a_5);
        }else if(MainActivity.state.equals("play")){
            imgPlay.setImageResource(R.drawable.a_3);
        }
    }
    public void playClick(View view){
        Intent intent = new Intent(MyService.mAction);
        switch (view.getId()){
            case R.id.img_play_type:
                intent.putExtra("cmd","type");
                break;
            case R.id.img_play_last:
                intent.putExtra("cmd","last");
                break;
            case R.id.img_play_play:
                if(MainActivity.state.equals("stop")){
                    imgPlay.setImageResource(R.drawable.a_3);
                    intent.putExtra("cmd","play");
                    state="play";
                }else if(MainActivity.state.equals("play")){
                    imgPlay.setImageResource(R.drawable.a_5);
                    intent.putExtra("cmd","play");
                    state="stop";
                }

                break;
            case R.id.img_play_next:
                intent.putExtra("cmd","next");
                break;
        }
        sendBroadcast(intent);
    }
    public void playClick2(View view){
        switch (view.getId()){
            case R.id.img_play_back:
                finish();
                break;
            case R.id.img_play_share:
                ToastUtil.toast(PlayActivity.this,"还没写");
                break;
            case R.id.img_play_menu:
                show();
                break;
        }
    }
    private void show() {
        AlertDialog dialog = new AlertDialog.Builder(PlayActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.activity_menu);
        RecyclerView recyclerView = (RecyclerView) dialog.getWindow().findViewById(R.id.mListView_menu);
        recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(PlayActivity.this));
        adapter2.setListener(new MyItemClickListener());
    }
    private class MyListener implements SeekBar.OnSeekBarChangeListener {
        Intent intent = new Intent(MyService.mAction);
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                seekBar.setProgress(progress);
                tvCurrentTime.setText(progress+"");
//                intent.putExtra("cmd","play");
//                sendBroadcast(intent);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            intent.putExtra("cmd","stop");
            sendBroadcast(intent);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            intent.putExtra("cmd","play");
            sendBroadcast(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(brod);
    }

    private class MyItemClickListener implements MenuAdapter.OnMyItemClickListener {
        @Override
        public void itemClick(int pos) {
            Intent intent = new Intent(MyService.mAction);
            intent.putExtra("pos",pos);
            sendBroadcast(intent);
        }
    }
}
