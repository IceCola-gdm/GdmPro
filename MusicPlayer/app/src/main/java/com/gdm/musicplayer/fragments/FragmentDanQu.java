package com.gdm.musicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PlayActivity;
import com.gdm.musicplayer.adapter.MyLocalDanquAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.MusicUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentDanQu extends Fragment {
    private RelativeLayout rl_all;
    private TextView tvMusicCount;
    private ListView listView;
    private ArrayList<Music> musics=new ArrayList<>();
    private MyLocalDanquAdapter adapter=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danqu, container, false);
        rl_all= (RelativeLayout) view.findViewById(R.id.rl_all);
        tvMusicCount= (TextView) view.findViewById(R.id.tv_danqu_count);
        listView= (ListView) view.findViewById(R.id.fragment_danqu_listview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        musics.addAll((ArrayList<Music>)MusicUtil.getAllSongs(getContext(),"song"));
        setAdapter();
    }

    private void setAdapter() {
        adapter=new MyLocalDanquAdapter(musics,getContext());
        listView.setAdapter(adapter);
        tvMusicCount.setText("("+musics.size()+")");
        listView.setOnItemClickListener(new MyListener());
    }

    private class MyListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent1 = new Intent(MyService.mAction);
            intent1.putExtra("cmd","play");
            getActivity().sendBroadcast(intent1);
            Intent intent = new Intent(getContext(), PlayActivity.class);
            intent.putExtra("data",musics);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    }
}
