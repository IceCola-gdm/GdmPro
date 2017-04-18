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
import com.gdm.musicplayer.fragments.FragmentMyCollectionAlbum;
import com.gdm.musicplayer.fragments.FragmentMyCollectionDanQu;
import com.gdm.musicplayer.fragments.FragmentMyCollectionMv;
import com.gdm.musicplayer.fragments.FragmentMyCollectionSinger;
import com.gdm.musicplayer.fragments.FragmentSinger;

import java.util.ArrayList;

/**
 * 我的收藏
 */
public class MyCollectionActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyPagerAdapter adapter;

    private String[] titles={"单曲","专辑","歌手","MV"};
    private ArrayList<Fragment> fgs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
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
        fgs.add(new FragmentMyCollectionDanQu());
        fgs.add(new FragmentMyCollectionAlbum());
        fgs.add(new FragmentMyCollectionSinger());
        fgs.add(new FragmentMyCollectionMv());

    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTabLayout= (TabLayout) findViewById(R.id.mycollection_tablelayout);
        mViewPager= (ViewPager) findViewById(R.id.mycollection_viewpager);
    }
    public void mycollectClick(View view){
        switch (view.getId()){
            case R.id.img_mycollection_back:
                finish();
                break;
        }
    }
}
