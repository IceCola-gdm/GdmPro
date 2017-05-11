package com.gdm.musicplayer.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.ContactsContract;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gdm.musicplayer.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MenuAdapter;
import com.gdm.musicplayer.adapter.MyPagerAdapter;
import com.gdm.musicplayer.bean.MList;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.fragments.FragmentLyric;
import com.gdm.musicplayer.fragments.FragmentPlay;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.TimeUtil;
import com.gdm.musicplayer.utils.ToastUtil;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity  {
    private ImageView imgBack;
    private ImageView imgShare;
    private ImageView imgPlay;
    private ViewPager viewPager;
    private ImageView imgPlayType;
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
    private int type=0;  //默认为全部播放
    private int pos;
    private int total;
    private int now;
    private ImageView imgType2;
    private TextView tvType;
    private TextView tvCount;
    private String anim="";
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
        filter.addAction(MyApplication.CHANGELIST);
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
                    tvCurrentTime.setText(TimeUtil.parse(now));
                    seekBar.setMax(total);
                    seekBar.setProgress(now);
                    tvTotalTime.setText(TimeUtil.parse(total));
                    pos = intent.getIntExtra("pos", 0);
                    title = intent.getStringExtra("title");
                    tvSongName.setText(title);
                    musics=MyService.getMusicList();
                    if (adapter2!=null) {
                        adapter2.notifyDataSetChanged();
                    }
                }else if(state.equals("stop")){
                    pos=intent.getIntExtra("pos", 0);
                }else if(state.equals("next")){
                    int p=intent.getIntExtra("pos",0);
                    tvTotalTime.setText(TimeUtil.parse(intent.getIntExtra("total",0)));
                    tvSongName.setText(intent.getStringExtra("title"));
                    tvSongSinger.setText(musics.get(p).getSinger());
                    seekBar.setMax(intent.getIntExtra("total",0));
                    seekBar.setProgress(0);
                }else if (state.equals("changeList")){
                    musics=MyService.getMusicList();
                    if (adapter2!=null) {
                        adapter2.notifyDataSetChanged();
                    }
                }
            }else if(intent.getAction().equals(MyApplication.CHANGELIST)){
                MyApplication ap= (MyApplication) getApplication();
                musics=ap.getMusics();
                adapter2.notifyDataSetChanged();
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
        anim = intent.getStringExtra("anim");
        String state1 = intent.getStringExtra("state");
        if(state1!=null){
            state=state1;
        }
        if(musics!=null){
            musics.clear();
        }
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
        pos=currentIndex;
        tvSongSinger.setText(music.getSinger());
        tvSongName.setText(music.getName());
        tvTotalTime.setText(TimeUtil.parse(music.getDuration()));
        seekBar.setMax(music.getDuration());
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
        if(musics.size()!=0){
            seekBar.setMax(musics.get(pos).getDuration());
        }
        imgPlay= (ImageView) findViewById(R.id.img_play_play);
        imgPlayType= (ImageView) findViewById(R.id.img_play_type);
        if(anim!=null&&anim!=""){
            if(anim.equals("start")){
                imgPlay.setImageResource(R.drawable.a_3);
                state="play";
            }else{
                imgPlay.setImageResource(R.drawable.a_5);
                state="stop";
            }
        }else{
            if(state.equals("stop")){
                state="stop";
                imgPlay.setImageResource(R.drawable.a_5);
            }else if(state.equals("play")){
                imgPlay.setImageResource(R.drawable.a_3);
                state="play";
            }
        }
        Intent intent = new Intent("palyactivity");
        intent.putExtra("state",state);
        sendBroadcast(intent);

    }
    public void playClick(View view){
        Intent intent = new Intent(MyService.mAction);
        switch (view.getId()){
            case R.id.img_play_type:
                if(type==MyService.LIST_PLAY){  //顺序播放
                    type=MyService.LIST_RECYCLE;
                    imgPlayType.setImageResource(R.drawable.a_h);
                }else if(type==MyService.LIST_RECYCLE){  //列表循环
                    type=MyService.ONE_MUSIC;
                    imgPlayType.setImageResource(R.drawable.a_p);
                }else if(type==MyService.ONE_MUSIC){    //单曲
                    type=MyService.RANDOM_PLAY;
                    imgPlayType.setImageResource(R.drawable.a_z);
                }else if(type==MyService.RANDOM_PLAY){  //随机
                    type=MyService.LIST_PLAY;
                    imgPlayType.setImageResource(R.drawable.a_r);
                }
                intent.putExtra("cmd","type");
                intent.putExtra("type",type);
                break;
            case R.id.img_play_last:
                seekBar.setProgress(0);
                pos--;
                if(pos==0){
                    return;
                }
                intent.putExtra("cmd","last");
                state="play";
                imgPlay.setImageResource(R.drawable.a_3);
                tvSongName.setText(musics.get(pos).getName());
                tvSongSinger.setText(musics.get(pos).getSinger());
                tvTotalTime.setText(TimeUtil.parse(musics.get(pos).getDuration()));
                seekBar.setMax(musics.get(pos).getDuration());
                break;
            case R.id.img_play_play:
                if(MainActivity.state.equals("stop")){
                    imgPlay.setImageResource(R.drawable.a_3);
                    state="play";
                }else if(MainActivity.state.equals("play")){
                    imgPlay.setImageResource(R.drawable.a_5);
                    state="stop";
                }
                intent.putExtra("cmd","play");
                break;
            case R.id.img_play_next:
                seekBar.setProgress(0);
                pos++;
                if(pos==musics.size()){
                    return;
                }
                intent.putExtra("cmd","next");
                state="play";
                imgPlay.setImageResource(R.drawable.a_3);
                tvSongSinger.setText(musics.get(pos).getSinger());
                tvSongName.setText(musics.get(pos).getName());
                tvTotalTime.setText(TimeUtil.parse(musics.get(pos).getDuration()));
                seekBar.setMax(musics.get(pos).getDuration());
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
        dialog.getWindow().setContentView(R.layout.activity_menu2);
        RecyclerView recyclerView = (RecyclerView) dialog.getWindow().findViewById(R.id.mListView_menu2);
        imgType2 = (ImageView) dialog.getWindow().findViewById(R.id.img_mode2);
        tvType= (TextView) dialog.getWindow().findViewById(R.id.tv_mode2);
        tvCount= (TextView) dialog.getWindow().findViewById(R.id.tv_account2);
        RelativeLayout rlDelete= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_delete2);
        if(type==MyService.LIST_PLAY){  //顺序播放
            imgType2.setImageResource(R.drawable.sx2);
            tvType.setText("顺序播放");
        }else if(type==MyService.LIST_RECYCLE){  //列表循环
            imgType2.setImageResource(R.drawable.xh2);
            tvType.setText("列表循环");
        }else if(type==MyService.ONE_MUSIC){    //单曲
            imgType2.setImageResource(R.drawable.dq2);
            tvType.setText("单曲循环");
        }else if(type==MyService.RANDOM_PLAY){  //随机
            imgType2.setImageResource(R.drawable.sj2);
            tvType.setText("随机播放");
        }
        tvCount.setText("("+musics.size()+"首)");
        recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(PlayActivity.this));
        adapter2.setListener(new MyItemClickListener());
        imgType2.setOnClickListener(new MyClickListener());
        rlDelete.setOnClickListener(new MyClickListener());

    }
    private class MyListener implements SeekBar.OnSeekBarChangeListener {
        Intent intent = new Intent(MyService.mAction);
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                seekBar.setProgress(progress);
                tvCurrentTime.setText(TimeUtil.parse(progress));
                intent.putExtra("cmd","seek_pos");
                intent.putExtra("pos",progress);
                sendBroadcast(intent);
            }else{
                tvCurrentTime.setText(TimeUtil.parse(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            intent.putExtra("cmd","seek_stop");
            sendBroadcast(intent);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            imgPlay.setImageResource(R.drawable.a_3);
            state="play";
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
            intent.putExtra("cmd","chose_pos");
            intent.putExtra("pos",pos);
            sendBroadcast(intent);
            Music music=musics.get(pos);
            tvSongName.setText(music.getName());
            tvSongSinger.setText(music.getSinger());
            tvTotalTime.setText(TimeUtil.parse(music.getDuration()));
            seekBar.setMax(music.getDuration());
            imgPlay.setImageResource(R.drawable.a_3);
            state="play";
        }
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_mode2:
                    sendBroadcastwithType();
                    break;
                case R.id.rl_delete2:

                    break;
            }
        }
    }

    private void sendBroadcastwithType() {
        Intent intent = new Intent(MyService.mAction);
        if(type==MyService.LIST_PLAY){  //顺序播放
            type=MyService.LIST_RECYCLE;
            imgType2.setImageResource(R.drawable.xh2);
            tvType.setText("列表循环");
        }else if(type==MyService.LIST_RECYCLE){  //列表循环
            type=MyService.ONE_MUSIC;
            imgType2.setImageResource(R.drawable.dq2);
            tvType.setText("单曲循环");
        }else if(type==MyService.ONE_MUSIC){    //单曲
            type=MyService.RANDOM_PLAY;
            imgType2.setImageResource(R.drawable.sj2);
            tvType.setText("随机播放");
        }else if(type==MyService.RANDOM_PLAY){  //随机
            type=MyService.LIST_PLAY;
            imgType2.setImageResource(R.drawable.sx2);
            tvType.setText("顺序播放");
        }
        intent.putExtra("cmd","type");
        intent.putExtra("type",type);
        sendBroadcast(intent);
    }
}
