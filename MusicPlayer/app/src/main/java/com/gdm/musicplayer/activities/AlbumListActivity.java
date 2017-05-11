package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MySingerListAdapter;
import com.gdm.musicplayer.bean.Album;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.service.MyService;

import java.util.ArrayList;

public class AlbumListActivity extends AppCompatActivity {
    private ListView listView;
    private TextView albumName;
    private TextView count;
    private ArrayList<Music> musics=new ArrayList<>();
    private Album album=null;
    private MySingerListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        Intent intent = getIntent();
        album= (Album) intent.getSerializableExtra("data");
        musics.addAll(album.getMusics());
        initView();
        initData();
        setAdapter();
        setListener();
    }

    private void initData() {
        albumName.setText(album.getName());
        count.setText("共（"+musics.size()+"）首");
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

                Intent intent = new Intent(AlbumListActivity.this, PlayActivity.class);
                intent.putExtra("data",musics);
                intent.putExtra("position",position);
                intent.putExtra("state","play");
                startActivity(intent);
                finish();
            }
        });
    }

    private void setAdapter() {
        adapter=new MySingerListAdapter(AlbumListActivity.this,musics);
        listView.setAdapter(adapter);
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.album_list_listview);
        albumName= (TextView) findViewById(R.id.tv_albumname);
        count= (TextView) findViewById(R.id.tv_count);
    }
    public void albumClick(View view){
        switch (view.getId()){
            case R.id.img_albumname_back:
                finish();
                break;
            case R.id.rl_all2:

                break;
        }
    }
}
