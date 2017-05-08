package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
    private ImageView imgAll;
    private TextView tvCount;
    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_singer_list);
        singer = (Singer) getIntent().getSerializableExtra("singer");
        musics.addAll(singer.getMusics());
        initView();
        setListener();


    }

    private void setListener() {
        imgBack.setOnClickListener(new MyListener());
        listView.setOnItemClickListener(new MyItemListener());
        imgAll.setOnClickListener(new MyListener());
    }

    private void initView() {
        imgBack= (ImageView)findViewById(R.id.img_singername_back);
        tvSingerName= (TextView)findViewById(R.id.tv_singername);
        imgAll= (ImageView)findViewById(R.id.img_singername_icon);
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
                case R.id.img_singername_icon:

                    break;
            }
        }
    }

    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MyService.mAction);
            intent.putExtra("cmd","chose_pos");
            intent.putExtra("pos",position);
            sendBroadcast(intent);
        }
    }
}
