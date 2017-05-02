package com.gdm.musicplayer.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by 10789 on 2017-04-30.
 */

public class ScrollerPager extends ViewPager {
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                if (ScrollerPager.this.getChildCount()-1>ScrollerPager.this.getCurrentItem()) {
                    ScrollerPager.this.setCurrentItem(ScrollerPager.this.getCurrentItem()+1);
                }else {
                    ScrollerPager.this.setCurrentItem(0);
                }
            }catch (Exception e){

            }
        }
    };
    public ScrollerPager(Context context) {
        this(context,null);
    }

    public ScrollerPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(3000);
                        handler.sendEmptyMessage(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
