package com.gdm.musicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MyLocalAlbumAdapter;
import com.gdm.musicplayer.adapter.MyLocalSingerAdapter;
import com.gdm.musicplayer.bean.Album;
import com.gdm.musicplayer.utils.MusicUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentAlbum extends Fragment {
    private ArrayList<Album> albums=new ArrayList<>();
    private ListView listView;
    private MyLocalAlbumAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        listView= (ListView) view.findViewById(R.id.fragment_album_listview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        albums=(ArrayList<Album>) MusicUtil.getAllSongs(getContext(),"album");
        adapter=new MyLocalAlbumAdapter(getContext(),albums);
        listView.setAdapter(adapter);
    }
    public void setAlbums(ArrayList<Album> albums){
        this.albums=albums;
    }
}
