package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MyLocalDanquAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.service.MyService;

import java.util.ArrayList;

/**
 * 最近播放界面
 */
public class RecentlyPlayActivity extends AppCompatActivity {
    private ArrayList<Music> musics=new ArrayList<>();  //数据源
    private TextView tvCount;
    private ListView listView;
    private MyLocalDanquAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_play);
        ArrayList<Music> temp= (ArrayList<Music>) getIntent().getSerializableExtra("data");
        musics.addAll(temp);
        initView();
        setAdapter();
        setListener();
    }

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(MyService.mAction);
                intent1.putExtra("cmd","chose_pos");
                intent1.putExtra("pos",position);
                intent1.putExtra("data",musics);
                sendBroadcast(intent1);

                Intent intent = new Intent(RecentlyPlayActivity.this, PlayActivity.class);
                intent.putExtra("data",musics);
                intent.putExtra("position",position);
                intent.putExtra("state","play");
                startActivity(intent);
            }
        });
    }

    private void setAdapter() {
        adapter=new MyLocalDanquAdapter(musics,RecentlyPlayActivity.this);
        listView.setAdapter(adapter);
    }

    private void initView() {
        tvCount= (TextView) findViewById(R.id.tv_recently_count);
        listView= (ListView) findViewById(R.id.recentlyplay_listview);
        tvCount.setText("（共"+musics.size()+"首）");
    }

    public void recentlyplayClick(View view){
        switch (view.getId()){
            case R.id.img_recentlyplay_back:   //返回
                finish();
                break;
            case R.id.tv_recentlyplay_clear:   //清空

                break;
            case R.id.img_recentlyplay_icon:   //全部播放

                break;
        }
    }
}
