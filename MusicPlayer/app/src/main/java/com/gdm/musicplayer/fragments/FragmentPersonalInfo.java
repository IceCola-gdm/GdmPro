package com.gdm.musicplayer.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MyPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class FragmentPersonalInfo extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private ArrayList<Fragment> fgs=new ArrayList<>();
    private String[] titles={"音乐","关于我"};
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_info, container, false);
        tabLayout= (TabLayout) view.findViewById(R.id.personal_tablelayout);
        viewPager= (ViewPager) view.findViewById(R.id.personal_viewpager);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setAdapter();
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
