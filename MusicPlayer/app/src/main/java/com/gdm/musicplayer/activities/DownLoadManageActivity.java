package com.gdm.musicplayer.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MyPagerAdapter;
import com.gdm.musicplayer.fragments.FragmentAlbum;
import com.gdm.musicplayer.fragments.FragmentDanQu;
import com.gdm.musicplayer.fragments.FragmentDir;
import com.gdm.musicplayer.fragments.FragmentDownloadDanQu;
import com.gdm.musicplayer.fragments.FragmentDownloadIng;
import com.gdm.musicplayer.fragments.FragmentDownloadMv;
import com.gdm.musicplayer.fragments.FragmentSinger;

import java.util.ArrayList;

/**
 * 下载管理界面
 */
public class DownLoadManageActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyPagerAdapter adapter;

    private String[] titles={"单曲","MV","下载中"};
    private ArrayList<Fragment> fgs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_manage);
        initView();
        initData();
        setAdapter();
    }

    private void setAdapter() {
        adapter=new MyPagerAdapter(getSupportFragmentManager(),fgs);
        adapter.setTitles(titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    private void initData() {
        for(int i=0;i<titles.length;i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles[i]));
        }
        fgs.add(new FragmentDownloadDanQu());
        fgs.add(new FragmentDownloadMv());
        fgs.add(new FragmentDownloadIng());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTabLayout= (TabLayout) findViewById(R.id.downloadmanage_tablelayout);
        mViewPager= (ViewPager) findViewById(R.id.downloadmanage_viewpager);
    }
    public void downloadClick(View view){
        switch (view.getId()){
            case R.id.img_downloadmanage_back:
                finish();
                break;
        }
    }
}
