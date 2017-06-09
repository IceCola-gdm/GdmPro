package com.gdm.musicplayer.fragments;

import android.content.Intent;
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
import com.gdm.musicplayer.activities.AlbumListActivity;
import com.gdm.musicplayer.adapter.MyLocalAlbumAdapter;
import com.gdm.musicplayer.bean.Album;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.download.ShouCangDbhelper;
import com.gdm.musicplayer.utils.MusicDealUtil;
import java.util.ArrayList;
/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentMyCollectionAlbum extends Fragment {
    private ListView listView;
    private ArrayList<Album> albums=new ArrayList<>();
    private MyLocalAlbumAdapter adapter=null;
    private ShouCangDbhelper shoucangHelper;
    private ArrayList<Music> allShoucang;
    private ArrayList<Album> temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoucangHelper = ShouCangDbhelper.getInstance(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycollection_album, container, false);
        listView= (ListView) view.findViewById(R.id.fragment_mycollection_album_listview);
        synchronized (this){
            allShoucang = (ArrayList<Music>) shoucangHelper.getAllShoucang();
            temp = (ArrayList<Album>) MusicDealUtil.get(allShoucang, "albums");
            albums.clear();
            albums.addAll(temp);
            adapter=new MyLocalAlbumAdapter(getContext(),albums);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnMyClickListener());
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
        listView.clearAnimation();
        albums.clear();
        albums=null;
    }

    private class OnMyClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), AlbumListActivity.class);
            intent.putExtra("data",albums.get(position));
            getContext().startActivity(intent);
        }
    }
}
