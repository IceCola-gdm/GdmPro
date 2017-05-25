package com.gdm.musicplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PlayListActivity;
import com.gdm.musicplayer.adapter.MyLocalSingerAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.Singer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentMyCollectionSinger extends Fragment {
    private ListView listView;
    private ArrayList<Singer> singers=new ArrayList<>();
    private MyLocalSingerAdapter adapter=null;
    private MyAddMusicReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver=new MyAddMusicReceiver();
        IntentFilter filter=new IntentFilter(PlayListActivity.FLAG);
        getActivity().registerReceiver(receiver,filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycollection_singer, container, false);
        listView= (ListView) view.findViewById(R.id.fragment_mycollection_singer_listview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter=new MyLocalSingerAdapter(getContext(),singers);
        listView.setAdapter(adapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private class MyAddMusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(PlayListActivity.FLAG)){
                ArrayList<Music> musics=new ArrayList<>();
                Singer singer = new Singer();
                Music music = (Music) intent.getSerializableExtra("add");
                musics.add(music);
                singer.setMusics(musics);
                singer.setName(music.getSinger());
                singers.add(singer);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
