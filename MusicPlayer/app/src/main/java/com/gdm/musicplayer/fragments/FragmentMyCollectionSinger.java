package com.gdm.musicplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PlayListActivity;
import com.gdm.musicplayer.activities.SingerListActivity;
import com.gdm.musicplayer.adapter.MyLocalSingerAdapter;
import com.gdm.musicplayer.bean.Album;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.Singer;
import com.gdm.musicplayer.download.ShouCangDbhelper;
import com.gdm.musicplayer.utils.MusicDealUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentMyCollectionSinger extends Fragment {
    private ListView listView;
    private ArrayList<Singer> singers=new ArrayList<>();
    private MyLocalSingerAdapter adapter=null;
    private ShouCangDbhelper shoucangHelper;
    private boolean isLoad=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoucangHelper = ShouCangDbhelper.getInstance(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycollection_singer, container, false);
        listView= (ListView) view.findViewById(R.id.fragment_mycollection_singer_listview);
        synchronized (this) {
            isLoad=false;
            final ArrayList<Music> allShoucang = (ArrayList<Music>) shoucangHelper.getAllShoucang();
            ArrayList<Singer> s = (ArrayList<Singer>) MusicDealUtil.get(allShoucang, "singers");
            singers.clear();
            singers.addAll(s) ;
            adapter=new MyLocalSingerAdapter(getContext(),singers);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new MyListener());
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), SingerListActivity.class);
            intent.putExtra("singer",singers.get(position));
            startActivity(intent);
        }
    }
}
