package com.gdm.musicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.SingerListActivity;
import com.gdm.musicplayer.adapter.MyLocalSingerAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.Singer;
import com.gdm.musicplayer.utils.MusicUtil;
import com.gdm.musicplayer.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentSinger extends Fragment {
    private ArrayList<Singer> singers=new ArrayList<>();
    private ListView listView;
    private MyLocalSingerAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singer, container, false);
        listView= (ListView) view.findViewById(R.id.fragment_singer_listview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        singers= (ArrayList<Singer>) MusicUtil.getAllSongs(getContext(),"singer");
        adapter=new MyLocalSingerAdapter(getContext(),singers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new MyItemListener());
    }

    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), SingerListActivity.class);
            intent.putExtra("singer",singers.get(position));
            startActivity(intent);
        }
    }
}
