package com.gdm.musicplayer.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MyPagerAdapter;
import com.gdm.musicplayer.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17 0017.
 * 个人中心
 */
public class FragmentPersonalInfo extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private ArrayList<Fragment> fgs=new ArrayList<>();
    private String[] titles={"音乐","关于我"};

    private CircleImageView imgPortrait;
    private ImageView imgBack;
    private ImageView imgBackground;
    private ImageView imgSex;
    private TextView tvNikName;
    private TextView tvEditUserInfo;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_info, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tabLayout= (TabLayout) view.findViewById(R.id.personal_tablelayout);
        viewPager= (ViewPager) view.findViewById(R.id.personal_viewpager);
        imgPortrait= (CircleImageView) view.findViewById(R.id.img_personalinfo_portrait);
        imgBack= (ImageView) view.findViewById(R.id.img_personalinfo_back);
        imgBackground= (ImageView) view.findViewById(R.id.img_personalinfo_background);
        imgSex= (ImageView) view.findViewById(R.id.img_personalinfo_sex);
        tvNikName= (TextView) view.findViewById(R.id.tv_personalinfo_username);
        tvEditUserInfo= (TextView) view.findViewById(R.id.tv_personalinfo_edit);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setAdapter();
        setListener();
    }

    private void setListener() {
        imgBack.
    }

    private void setAdapter() {
        adapter=new MyPagerAdapter(getChildFragmentManager(),fgs);
        adapter.setTitles(titles);
        viewPager.setAdapter(adapter);
    }

    private void initData() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i=0;i<titles.length;i++){
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }
        fgs.add(new FragmentPersonalYinyue());
        fgs.add(new FragmentPersonalAbout());
    }
}
