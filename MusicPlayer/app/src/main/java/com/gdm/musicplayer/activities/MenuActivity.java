package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MenuAdapter;
import com.gdm.musicplayer.bean.Music;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Music> musics=new ArrayList<>();
    private MenuAdapter adapter;
    private int pos=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        ArrayList<Music> m= (ArrayList<Music>) intent.getSerializableExtra("data");
        musics.addAll(m);
        pos=intent.getIntExtra("pos",-1);
        initView();
        setAdapter();
    }

    private void setAdapter() {
        adapter=new MenuAdapter(musics,MenuActivity.this);
        addHeader();
        listView.setAdapter(adapter);
        View view = listView.getChildAt(pos);
        TextView tvName= (TextView) view.findViewById(R.id.tv_menu_songname);
        TextView tvSinger= (TextView) view.findViewById(R.id.tv_singer);
    }

    private void addHeader() {
        View view = LayoutInflater.from(MenuActivity.this).inflate(R.layout.menu_header, listView, false);
        ImageView imgMode = (ImageView) view.findViewById(R.id.img_mode);
        TextView tvMode = (TextView) view.findViewById(R.id.tv_mode);
        TextView tvAccount = (TextView) view.findViewById(R.id.tv_account);
        RelativeLayout rlDelete = (RelativeLayout) view.findViewById(R.id.rl_delete);
        listView.addHeaderView(view);
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.mListView_menu);
    }
}
