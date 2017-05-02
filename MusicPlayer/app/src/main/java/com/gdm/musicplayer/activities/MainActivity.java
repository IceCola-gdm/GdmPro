package com.gdm.musicplayer.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MenuAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.bean.UserInfro;
import com.gdm.musicplayer.fragments.FragmentMain;
import com.gdm.musicplayer.fragments.FragmentPersonalInfo;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.MusicUtil;
import com.gdm.musicplayer.utils.ToastUtil;
import com.gdm.musicplayer.view.MySlidingPanelLayout;

import java.util.ArrayList;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private MySlidingPanelLayout mSlidingPaneLayout;
    private ImageView imgPortrait;   //头像
    private ImageView imgSex;
    private TextView tvHeart;
    private TextView tvNickname;
    private TextView tvPiFu;    //当前皮肤
    private TextView tvLogin;    //登录注册
    private RelativeLayout rl_info;

    private FragmentMain fragmentMain;
    private ArrayList<Fragment> menus;
    private ArrayList<Music> musics=new ArrayList<>();
    private ImageView imgPlay;
    private MenuAdapter adapter=null;
    private ImageView imgPor;
    private TextView tvSong;
    private TextView tvSinger;

    public static String state="stop";  //播放状态
    private String title;
    private int pos=0;
    private MyPlayStateReceiver receiver;
    private ImageView imgType;
    private TextView tvType;
    private TextView tvCount;
    private int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new MyPlayStateReceiver();
        IntentFilter filter = new IntentFilter(MyService.PLAY_ACTION);
        registerReceiver(receiver,filter);
        initView();
        initData();
        Intent intent = new Intent(this, MyService.class);
        MyService.setMusicList(musics);
        startService(intent);
    }

    private void initData() {
        fragmentMain = new FragmentMain();
        showFragment1(fragmentMain);
        fragmentMain.setOnImgListener(new MyListener());
        menus=new ArrayList<>();
        menus.add(new FragmentPersonalInfo());
        musics.addAll((ArrayList<Music>)MusicUtil.getAllSongs(MainActivity.this,"song"));
        adapter=new MenuAdapter(musics,MainActivity.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lastFragment==null||lastFragment==fragmentMain) {
            if (fragmentMain.isAdded()) {
                getSupportFragmentManager().beginTransaction().show(fragmentMain).commitAllowingStateLoss();
            }
        }else{
            if (lastFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().show(lastFragment).commitAllowingStateLoss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private Fragment lastFragment=null;
    private void showFragment1(FragmentMain mainFragment) {
        if (mainFragment.isVisible()) {
            return;
        }
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        if (lastFragment==null) {

        }else{
            t.hide(lastFragment).commit();
        }
        lastFragment=mainFragment;
        if(mainFragment.isAdded()){
            t.show(mainFragment);
        }else{
            t.add(R.id.main_container,mainFragment);
        }
        t.commit();
    }
    private void showFragment(Fragment mainFragment) {
        if (mainFragment.isVisible()) {
            return;
        }
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (lastFragment==null) {
            
        }else{
            ft.hide(lastFragment).commit();
        }
        lastFragment=mainFragment;
        if(mainFragment.isAdded()){
            ft.show(mainFragment);
        }else{
            ft.add(R.id.main_container,mainFragment);
        }
        lastFragment=mainFragment;
//        ft.commit();
    }

    private void closeFragment(Fragment mainFragment) {
        android.support.v4.app.FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        if(mainFragment.isAdded()) {
            ft.hide(mainFragment);
            ft.commit();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mSlidingPaneLayout= (MySlidingPanelLayout) findViewById(R.id.sliding);
        imgPortrait= (ImageView) findViewById(R.id.img_main_portrait);
        imgSex= (ImageView) findViewById(R.id.img_sex);
        tvNickname= (TextView) findViewById(R.id.tv_nickname);
        tvHeart= (TextView) findViewById(R.id.tv_heart);

        tvLogin= (TextView) findViewById(R.id.tv_main_login);
        tvPiFu= (TextView) findViewById(R.id.tv_pifu);
        imgPlay= (ImageView) findViewById(R.id.rb_song_playicon);
        imgPor= (ImageView) findViewById(R.id.img_song_cover);
        tvSong= (TextView) findViewById(R.id.tv_songname);
        tvSinger= (TextView) findViewById(R.id.tv_singer);
        rl_info= (RelativeLayout) findViewById(R.id.rl_info);

        User user = UserInfro.getUser();
        if(MyApplication.isLogin) {
            if (user != null) {
                tvLogin.setVisibility(View.INVISIBLE);
                rl_info.setVisibility(View.VISIBLE);
                if (user.getNickname() != null && user.getNickname() != "") {
                    tvNickname.setText(user.getNickname());
                }
                if (user.getHeart() != null && user.getHeart() != "") {
                    tvHeart.setText(user.getHeart());
                }
            }
        }
    }

    private class MyListener implements FragmentMain.OnImgListener {

        @Override
        public void leftImgCallback() {
            if(mSlidingPaneLayout.isOpen()){
                mSlidingPaneLayout.closePane();
            }else{
                mSlidingPaneLayout.openPane();
            }
        }

        @Override
        public void rightImgCallback() {
            ToastUtil.toast(MainActivity.this,"还没写");
        }
    }

    /**
     * 处理SlidingPanelLayout里面的点击事件
     * @param view
     */
    public void clickInMain(View view){
        if(lastFragment!=null){
            closeFragment(lastFragment);
        }
        switch (view.getId()){
            case R.id.img_main_portrait:  //头像
                showFragment(menus.get(0));
                break;
            case R.id.tv_main_login:  //登录、注册
                Intent intent = new Intent(MainActivity.this, ChooseToLoginOrRegister.class);
                startActivity(intent);
                break;
            case R.id.main_rl_huanfu:  //个性换肤
                Intent intent1 = new Intent(MainActivity.this, HuanfuActivity.class);
                startActivity(intent1);
                break;
            case R.id.main_rl_timer:  //定时停止
                ToastUtil.toast(MainActivity.this,"还没写");
                break;
            case R.id.main_rl_naozhong:  //音乐闹钟
                ToastUtil.toast(MainActivity.this,"还没写");
                break;
            case R.id.rl_change:  //切换账号
                Intent intent2 = new Intent(MainActivity.this, ChooseToLoginOrRegister.class);
                startActivity(intent2);
                break;
            case R.id.main_rl_exit:  //退出
                finish();
                break;
            case R.id.main_rl_pwd:
                Intent intent3 = new Intent(MainActivity.this, ResetPwdActivity.class);
                startActivity(intent3);
                break;
        }
        mSlidingPaneLayout.closePane();
    }

    @Override
    public void onBackPressed() {
        if (fragmentMain.isAdded()&&fragmentMain.isVisible()) {
            finish();
        }else{
            showFragment1(fragmentMain);
        }
    }
    public void myClick(View view){
        switch (view.getId()){
            case R.id.rl_play_icon:
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("data",musics);
                intent.putExtra("position",pos);
                intent.putExtra("state",state);
                startActivity(intent);
                break;
            case R.id.rb_song_playicon:
                Intent intent1 = new Intent(MyService.mAction);
                if(state.equals("play")){
                    imgPlay.setImageResource(R.drawable.play);
                    intent1.putExtra("cmd","play");
                    state="stop";
                }else{
                    imgPlay.setImageResource(R.drawable.stop);
                    intent1.putExtra("cmd","play");
                    state="play";
                }
                sendBroadcast(intent1);
                break;
            case R.id.img_song_list:
                show();
                break;
        }
    }

    private void show() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.activity_menu);
        RecyclerView recyclerView = (RecyclerView) dialog.getWindow().findViewById(R.id.mListView_menu);
        imgType = (ImageView) dialog.getWindow().findViewById(R.id.img_mode);
        tvType= (TextView) dialog.getWindow().findViewById(R.id.tv_mode);
        tvCount= (TextView) dialog.getWindow().findViewById(R.id.tv_account);
        RelativeLayout rlDelete= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_delete);
        if(type==MyService.LIST_PLAY){  //顺序播放
            imgType.setImageResource(R.drawable.sx2);
            tvType.setText("顺序播放");
        }else if(type==MyService.LIST_RECYCLE){  //列表循环
            imgType.setImageResource(R.drawable.xh2);
            tvType.setText("列表循环");
        }else if(type==MyService.ONE_MUSIC){    //单曲
            imgType.setImageResource(R.drawable.dq2);
            tvType.setText("单曲循环");
        }else if(type==MyService.RANDOM_PLAY){  //随机
            imgType.setImageResource(R.drawable.sj2);
            tvType.setText("随机播放");
        }
        tvCount.setText("("+musics.size()+"首)");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter.setListener(new MyItemClickListener());
        imgType.setOnClickListener(new MyClickListener());
        rlDelete.setOnClickListener(new MyClickListener());
    }
    private class MyPlayStateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case MyService.PLAY_ACTION:
                    state=intent.getStringExtra("state");
                    pos = intent.getIntExtra("pos", 0);
                    title = intent.getStringExtra("title");
                    tvSong.setText(title);
                    if(state.equals("play")){
                        imgPlay.setImageResource(R.drawable.stop);
                    }else if(state.equals("stop")){
                        imgPlay.setImageResource(R.drawable.play);
                    }
                    break;
            }
        }
    }

    private class MyItemClickListener implements MenuAdapter.OnMyItemClickListener {
        @Override
        public void itemClick(int pos) {
            Intent intent = new Intent(MyService.mAction);
            intent.putExtra("cmd","chose_pos");
            intent.putExtra("pos",pos);
            sendBroadcast(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        MyApplication.isLogin=false;
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_mode:
                    sendBroadcastWithType();
                    break;
                case R.id.rl_delete:

                    break;
            }
        }
    }

    private void sendBroadcastWithType() {
        Intent intent = new Intent(MyService.mAction);
        if(type==MyService.LIST_PLAY){  //顺序播放
            type=MyService.LIST_RECYCLE;
            imgType.setImageResource(R.drawable.xh2);
            tvType.setText("列表循环");
        }else if(type==MyService.LIST_RECYCLE){  //列表循环
            type=MyService.ONE_MUSIC;
            imgType.setImageResource(R.drawable.dq2);
            tvType.setText("单曲循环");
        }else if(type==MyService.ONE_MUSIC){    //单曲
            type=MyService.RANDOM_PLAY;
            imgType.setImageResource(R.drawable.sj2);
            tvType.setText("随机播放");
        }else if(type==MyService.RANDOM_PLAY){  //随机
            type=MyService.LIST_PLAY;
            imgType.setImageResource(R.drawable.sx2);
            tvType.setText("顺序播放");
        }
        intent.putExtra("cmd","type");
        intent.putExtra("type",type);
        sendBroadcast(intent);
    }
}
