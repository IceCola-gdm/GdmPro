package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.download.DataBase;
import com.gdm.musicplayer.download.DownLoadService;
import com.gdm.musicplayer.view.LrcView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {
    LrcView lrc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        lrc= (LrcView) findViewById(R.id.lrc_text);
        OkHttpUtils.get("http://120.24.220.119:8080/music/data/music/lrc/1b2b65b1.lrc")
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        try {
                            lrc.setLrcPath(file.getAbsolutePath());
                            new Thread(new Runnable() {
                                int time=0;
                                @Override
                                public void run() {
                                    while (true){

                                        lrc.changeCurrent(time);
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        time=time+1000;
                                    }
                                }
                            }).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
