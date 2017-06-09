package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MySingerListAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.Singer;
import com.gdm.musicplayer.service.MyService;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/7 0007.
 */
public class SingerListActivity extends AppCompatActivity {
    private ArrayList<Music> musics=new ArrayList<>();
    private Singer singer=null;
    private MySingerListAdapter adapter=null;
    private ImageView imgBack;
    private TextView tvSingerName;
    private TextView tvCount;
    private ListView listView;
    private RelativeLayout rl_all2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_singer_list);
        singer = (Singer) getIntent().getSerializableExtra("singer");
        musics.addAll(singer.getMusics());
        initView();
        setListener();
        setAdapter();
    }

    private void setAdapter() {
        adapter=new MySingerListAdapter(SingerListActivity.this,musics);
        listView.setAdapter(adapter);
    }

    private void setListener() {
        imgBack.setOnClickListener(new MyListener());
        listView.setOnItemClickListener(new MyItemListener());
        rl_all2.setOnClickListener(new MyListener());
    }

    private void initView() {
        imgBack= (ImageView)findViewById(R.id.img_singername_back);
        tvSingerName= (TextView)findViewById(R.id.tv_singername);
        rl_all2= (RelativeLayout) findViewById(R.id.rl_all2);
        tvCount= (TextView)findViewById(R.id.tv_count);
        listView= (ListView)findViewById(R.id.fragment_singer_list_listview);
        tvSingerName.setText(singer.getName());
        tvCount.setText("（共"+musics.size()+"首）");
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_singername_back:
                    finish();
                    break;
                case R.id.rl_all2:
                    playAll();
                    break;
            }
        }
    }

    private void playAll() {
        Intent intent1 = new Intent(MyService.mAction);
        intent1.putExtra("cmd","chose_pos");
        intent1.putExtra("pos",0);
        intent1.putExtra("data",musics);
        intent1.putExtra("flag",0);   //0为本地音乐
        sendBroadcast(intent1);

        Intent intent = new Intent(SingerListActivity.this, PlayActivity.class);
        intent.putExtra("data",musics);
        intent.putExtra("position",0);
        intent.putExtra("state","play");
        startActivity(intent);
    }

    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent1 = new Intent(MyService.mAction);
            intent1.putExtra("cmd","chose_pos");
            intent1.putExtra("pos",position);
            intent1.putExtra("data",musics);
            intent1.putExtra("flag",0);
            sendBroadcast(intent1);

            Intent intent = new Intent(SingerListActivity.this, PlayActivity.class);
            intent.putExtra("data",musics);
            intent.putExtra("position",position);
            intent.putExtra("state","play");
            startActivity(intent);
            finish();
        }
    }
}
