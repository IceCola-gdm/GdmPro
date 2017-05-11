package com.gdm.musicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdm.musicplayer.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.DownLoadManageActivity;
import com.gdm.musicplayer.activities.LocalMusicListActivity;
import com.gdm.musicplayer.activities.MyCollectionActivity;
import com.gdm.musicplayer.activities.RecentlyPlayActivity;
import com.gdm.musicplayer.adapter.MyRecyclerViewAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.MusicList;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.MusicUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentMy extends Fragment {
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ArrayList<MusicList> musicLists=new ArrayList<>();
    private MusicList musicList=null;
    private String[] titles={"本地音乐","最近播放","下载管理","我的收藏"};
    private ArrayList<MusicList> content;
    private Class[] classes={LocalMusicListActivity.class, RecentlyPlayActivity.class, DownLoadManageActivity.class, MyCollectionActivity.class};
    private int[] icons={R.drawable.local,R.drawable.w1,R.drawable.downmanage,R.drawable.collectmanage};
    private ArrayList<Music> musicsLocal=new ArrayList<>();  //数据源1
    private ArrayList<Music> musicsRently=new ArrayList<>();  //数据源2
    private ArrayList<Music> musicsDown=new ArrayList<>();  //数据源3
    private ArrayList<Music> musicsCollect=new ArrayList<>();  //数据源4
    private ArrayList<ArrayList<Music>> data=new ArrayList<>();
    private MyApplication application;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application= (MyApplication) getActivity().getApplication();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        musicsLocal.addAll((ArrayList<Music>)MusicUtil.getAllSongs(getContext(),"song"));
        data.add(musicsLocal);
        data.add(musicsRently);
        data.add(musicsDown);
        data.add(musicsCollect);
        for(int i=0;i<titles.length;i++){
            String title = titles[i];
            musicList=new MusicList();
            musicList.setImgPath(icons[i]);
            musicList.setTitle(title);
            musicList.setNum(data.get(i).size()+"");
            musicList.setType(0);
            musicList.setmClass(classes[i]);
            musicList.setM(data.get(i));
            musicLists.add(musicList);
        }
        content=new ArrayList<>();
        //TODO 从服务器上获取数据
        adapter=new MyRecyclerViewAdapter(getContext(),musicLists,content,application);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.fragment_my_recyclerview);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
    }
}
