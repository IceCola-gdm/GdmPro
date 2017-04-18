package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.fragments.FragmentFriend;
import com.gdm.musicplayer.fragments.FragmentFujing;
import com.gdm.musicplayer.fragments.FragmentMain;
import com.gdm.musicplayer.fragments.FragmentPersonalInfo;
import com.gdm.musicplayer.utils.ToastUtil;
import com.gdm.musicplayer.view.MySlidingPanelLayout;

import java.util.ArrayList;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private MySlidingPanelLayout mSlidingPaneLayout;
    private ArrayList<Fragment> fgs=new ArrayList<>();

    private ImageView imgPortrait;   //头像
    private TextView tvPiFu;    //当前皮肤
    private TextView tvLogin;    //登录注册
    private Fragment lastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        fgs.add(new FragmentPersonalInfo());
        fgs.add(new FragmentFriend());
        fgs.add(new FragmentFujing());
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentMain fragmentMain = new FragmentMain();
        showFragment(fragmentMain);
        fragmentMain.setOnImgListener(new MyListener());
    }

    private void showFragment(FragmentMain mainFragment) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(mainFragment.isAdded()){
            ft.show(mainFragment);
        }else{
            ft.add(R.id.main_container,mainFragment);
        }
        ft.commit();
    }
    private void showFragment(Fragment mainFragment) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(mainFragment.isAdded()){
            ft.show(mainFragment);
        }else{
            ft.add(R.id.main_container,mainFragment);
        }
        ft.commit();
        lastFragment=mainFragment;
    }

    private void closeFragment(Fragment mainFragment) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(mainFragment.isAdded()) {
            ft.hide(mainFragment);
        }
        ft.commit();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mSlidingPaneLayout= (MySlidingPanelLayout) findViewById(R.id.sliding);
        imgPortrait= (ImageView) findViewById(R.id.img_main_portrait);
        tvLogin= (TextView) findViewById(R.id.tv_main_login);
        tvPiFu= (TextView) findViewById(R.id.tv_pifu);
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
    public void clickInMain(View view){
        if(lastFragment!=null){
            closeFragment(lastFragment);
        }
        switch (view.getId()){
            case R.id.img_main_portrait:  //头像
                FragmentPersonalInfo fragmentPersonalInfo = new FragmentPersonalInfo();
                showFragment(fragmentPersonalInfo);
                break;
            case R.id.tv_main_login:  //登录、注册
                Intent intent = new Intent(MainActivity.this, ChooseToLoginOrRegister.class);
                startActivity(intent);
                break;
            case R.id.main_rl_personal:  //个人中心
                showFragment(new FragmentPersonalInfo());
                break;
            case R.id.main_rl_friend:  //我的好友
                showFragment(new FragmentFriend());
                break;
            case R.id.main_rl_fujin:  //附近的人
                showFragment(new FragmentFujing());
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
            case R.id.main_rl_mode:  //模式切换
                ToastUtil.toast(MainActivity.this,"还没写");
                break;
            case R.id.main_rl_setting:  //切换账号
                Intent intent2 = new Intent(MainActivity.this, ChooseToLoginOrRegister.class);
                startActivity(intent2);
                break;
            case R.id.main_rl_exit:  //退出
                finish();
                break;
        }
        mSlidingPaneLayout.closePane();
    }
}
