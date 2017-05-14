package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.download.DataBase;
import com.gdm.musicplayer.download.DownLoadService;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final DataBase db = DataBase.getDb(this);
        Intent intent = new Intent(this, DownLoadService.class);
        intent.putExtra("name","演员");
        intent.putExtra("path","http://120.24.220.119:8080/music/data/music/cec439d2.mp3");
        intent.putExtra("type",1);
        startService(intent);

    }
}
