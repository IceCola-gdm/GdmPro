package com.gdm.musicplayer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.FujingViewPagerAdapter;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class FragmentFujing extends Fragment {
    private ImageView imgBack;
    private ViewPager viewPager;
    private ListView listView;
    private Handler handler=null;
    private int currentIndex=0;
    private int[] imgs={R.drawable.spic_c1,R.drawable.spic_c2,R.drawable.spic_c3,R.drawable.spic_c4,R.drawable.spic_c5,R.drawable.spic_c6};
    private FujingViewPagerAdapter adapter=null;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fujing, container, false);
        imgBack= (ImageView) view.findViewById(R.id.img_fujing_back);
        viewPager= (ViewPager) view.findViewById(R.id.fujing_viewpager);
        listView= (ListView) view.findViewById(R.id.fujing_listview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
        createHandler();
        createThread();
    }
    private void createThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    handler.sendEmptyMessage(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void createHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
                switch (msg.what){
                    case 100:
                        if(currentIndex==imgs.length){
                            currentIndex=currentIndex%imgs.length;
                        }
                        viewPager.setCurrentItem(currentIndex);
                        currentIndex++;
                        break;
                }
            }
        };
    }

    private void setAdapter() {
        adapter=new FujingViewPagerAdapter(imgs,getContext());
        viewPager.setAdapter(adapter);
    }
}
