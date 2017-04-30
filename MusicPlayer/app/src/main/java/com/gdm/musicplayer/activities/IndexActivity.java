package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gdm.musicplayer.R;

public class IndexActivity extends AppCompatActivity {
    private ImageView imgAds;
    private ImageView imgJump;
    private int[] imgs={R.drawable.timg,R.drawable.timg1,R.drawable.timg2,R.drawable.timg3};
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        imgAds= (ImageView) findViewById(R.id.imgAds);
        imgJump= (ImageView) findViewById(R.id.img_jump);
        imgJump.setOnClickListener(new MyListener());
        imgAds.setOnClickListener(new MyListener());
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
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    if(index!=imgs.length){
                        imgAds.setImageResource(imgs[index++]);
                    }
                    break;
            }
        }
    };
    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_jump:
                    Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.imgAds:
                    if(index==imgs.length-1){
                        Intent intent2 = new Intent(IndexActivity.this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                    break;
            }
        }
    }
}
