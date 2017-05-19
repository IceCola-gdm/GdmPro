package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Music;

import java.util.ArrayList;

import io.vov.vitamio.utils.Log;

/**
 * Created by 10789 on 2017-05-17.
 */

public class DownLoadAdt extends BaseAdapter {
    private ArrayList<Music> musics;
    private Context context;
    private LayoutInflater inflater;

    public DownLoadAdt(ArrayList<Music> musics, Context context) {
        this.musics = musics;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.localmusiclist_listview_item, null, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()== MotionEvent.ACTION_UP) {
                    Log.e("DownLoadAdt","触发了点击事件");
                }
                return false;
            }
        });
        TextView name= (TextView) view.findViewById(R.id.tv_song_name);
        name.setText(musics.get(position).getName());
        return view;
    }
}
