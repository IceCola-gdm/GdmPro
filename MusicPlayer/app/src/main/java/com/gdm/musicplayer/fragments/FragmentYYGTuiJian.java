package com.gdm.musicplayer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.YYGPagerAdapter;
import com.gdm.musicplayer.view.MyViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class FragmentYYGTuiJian extends Fragment {
    private ViewPager viewPager;
    private YYGPagerAdapter adapter;
    private ArrayList<String> imgs=new ArrayList<>();  //广告图片路径
    private boolean isvis=false;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==500) {
                if (viewPager!=null&&adapter!=null) {
                    int currentItem = viewPager.getCurrentItem();
                    int count = adapter.getCount();
                    if (currentItem==count-1) {
                        viewPager.setCurrentItem(0);
                    }else {
                        viewPager.setCurrentItem(currentItem+1);
                    }
                }
            }
        }
    };
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isvis) {
                        handler.sendEmptyMessage(500);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initData() {
        imgs.add("http://p0.so.qhimgs1.com/t013c279a6e3f0c11a0.jpg");
        imgs.add("http://p3.so.qhmsg.com/t0113d1b0184e0f26c8.jpg");
        imgs.add("http://p1.so.qhmsg.com/t0185cf25692ab9abd6.jpg");
        imgs.add("http://p3.so.qhmsg.com/t014e39272903d0ea18.jpg");
    }

    @Override
    public void onPause() {
        super.onPause();
        isvis=false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isvis=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuijian, container, false);
        viewPager= (ViewPager) view.findViewById(R.id.yyg_tj);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        adapter=new YYGPagerAdapter(getContext(),imgs);
        viewPager.setAdapter(adapter);
    }
}
