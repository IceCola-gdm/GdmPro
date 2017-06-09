package com.gdm.musicplayer.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MyPagerAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.view.ChildViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentYinYueGuan extends Fragment {
    private TabLayout tabLayout;
    private ChildViewPager viewPager;
    private String[] titles={"个性推荐","全部音乐","音悦热歌"};
    private MyPagerAdapter adapter;
    private ArrayList<Fragment> fgs=new ArrayList<>();
    private ArrayList<Music> musics=new ArrayList<>();
    private Music music=null;
    private MyApplication app;
    private User user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MyApplication) getActivity().getApplication();
        user=app.getUser();
        initData();
    }

    private void initData() {
        fgs.add(new FragmentYYGTuiJian());
        fgs.add(new FragmentYYGGeDan());
        fgs.add(new FragmentYYGPaiHang());
        adapter=new MyPagerAdapter(getChildFragmentManager(),fgs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yinyueguan, container, false);
        tabLayout= (TabLayout) view.findViewById(R.id.yinyueguan_tablelayout);
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[2]));
        viewPager= (ChildViewPager) view.findViewById(R.id.yinyueguan_viewpager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager= (ChildViewPager) view.findViewById(R.id.yinyueguan_viewpager);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        adapter.setTitles(titles);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewPager.setAdapter(adapter);
    }
}
