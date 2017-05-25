package com.gdm.musicplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PlayListActivity;
import com.gdm.musicplayer.adapter.MyLocalDanquAdapter;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.download.DataBase;
import com.gdm.musicplayer.service.MyService;

import java.io.File;
import java.util.AbstractSequentialList;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentDownloadDanQu extends Fragment {
    private MyLocalDanquAdapter adt;
    private DataBase db;
    private ArrayList<Music> musicsDown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private File root=new File(Environment.getExternalStorageDirectory(),"gdm");
    private ListView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloadmanage_danqu, container, false);
        list= (ListView) view.findViewById(R.id.fragment_downloadmanage_danqu_listview);
        initDownMusic();
        adt=new MyLocalDanquAdapter(musicsDown,getActivity());
        list.setAdapter(adt);

        return view;
    }
    private void initDownMusic() {
        musicsDown=new ArrayList<>();
        db = DataBase.getDb(getActivity());
        ArrayList<DataBase.Down> downs = db.getAllByType(1);
        for (int i = 0; i < downs.size(); i++) {
            Music music = new Music();
            DataBase.Down down = downs.get(i);
            music.setName(down.getName());
            File f = new File(root, down.getName() + ".mp3");
            music.setFileUrl(f.getAbsolutePath());
            musicsDown.add(music);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
