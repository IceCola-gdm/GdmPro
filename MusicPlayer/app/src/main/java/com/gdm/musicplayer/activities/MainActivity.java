package com.gdm.musicplayer.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MyPagerAdapter;
import com.gdm.musicplayer.fragments.FragmentMy;
import com.gdm.musicplayer.fragments.FragmentShiPing;
import com.gdm.musicplayer.fragments.FragmentYinYueGuan;
import com.gdm.musicplayer.utils.ToastUtil;
import com.gdm.musicplayer.view.MySlidingPanelLayout;
import com.gdm.musicplayer.view.MyTitleView;

import java.util.ArrayList;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private MySlidingPanelLayout mSlidingPaneLayout;
    private MyTitleView myTitleView;
    private ViewPager mViewPager;
    private MyPagerAdapter adapter;
    private ArrayList<Fragment> fgs=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        fgs.add(new FragmentMy());
        fgs.add(new FragmentYinYueGuan());
        fgs.add(new FragmentShiPing());
        adapter=new MyPagerAdapter(getSupportFragmentManager(),fgs);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mSlidingPaneLayout= (MySlidingPanelLayout) findViewById(R.id.sliding);
        myTitleView= (MyTitleView) findViewById(R.id.mytitleview);
        mViewPager= (ViewPager) findViewById(R.id.viewpager);
    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        myTitleView.setOnLeftImgClick(new MyLeftImgClickListener());
        myTitleView.setOnRightImgClick(new MyRightImgClickListener());
        myTitleView.setOnRadioGroupClick(new MyRadioGroupCheckListener());
    }

    /**
     * 处理ViewPPager的翻页
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioGroup group = myTitleView.getRadioGroup();
            RadioButton r= (RadioButton) group.getChildAt(position);
            r.setChecked(true);
            r.setTextSize(20);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 处理左边图片的点击事件（抽屉）
     */
    private class MyLeftImgClickListener implements MyTitleView.OnLeftImgClick {
        @Override
        public void onClick(View v) {
            if(mSlidingPaneLayout.isOpen()){
                mSlidingPaneLayout.closePane();
            }else{
                mSlidingPaneLayout.openPane();
            }
        }
    }

    /**
     * 处理右边图片的点击事件（搜索）
     */
    private class MyRightImgClickListener implements MyTitleView.OnRightImgClick {
        @Override
        public void onClick(View v) {
            ToastUtil.toast(MainActivity.this,"点击了");
        }
    }

    /**
     * 处理RadioGroup的点击事件
     */
    private class MyRadioGroupCheckListener implements MyTitleView.OnRadioGroupClick {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int childCount = group.getChildCount();
            for(int i=0;i<childCount;i++){
                RadioButton r = (RadioButton) group.getChildAt(i);
                if(r.isChecked()){
                    r.setTextSize(20);
                    mViewPager.setCurrentItem(i);
                }else{
                    r.setTextSize(18);
                }
            }
        }
    }
}
