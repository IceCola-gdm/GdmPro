package com.gdm.musicplayer.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class MyViewPager extends ViewPager {
    private MyViewPager instence;
    private int currentIndex=0;
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.instence=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(100);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    int childCount = instence.getChildCount();
                    instence.setCurrentItem(currentIndex);
                    currentIndex++;
                    if(currentIndex==childCount){
                        currentIndex=currentIndex%childCount;
                    }
                    break;
            }
        }
    };
}
