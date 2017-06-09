package com.gdm.musicplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MyLocalDanquAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.download.DataBase;

import java.util.ArrayList;

/**
 * 下载管理界面
 */
public class DownLoadManageActivity extends AppCompatActivity {
    private ListView listView;
    private MyLocalDanquAdapter adt;
    private DataBase db;
    private ArrayList<Music> musicsDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_manage);
        initView();
        initDownMusic();
        adt=new MyLocalDanquAdapter(musicsDown,DownLoadManageActivity.this);
        listView.setAdapter(adt);
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.mListView);
    }

    public void downloadClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    private void initDownMusic() {
        musicsDown=new ArrayList<>();
        db = DataBase.getDb(DownLoadManageActivity.this);
        musicsDown.addAll(db.selectMusic(2));
    }
}
