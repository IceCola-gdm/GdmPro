package com.gdm.musicplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdm.musicplayer.activities.PlayListActivity;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.DownLoadManageActivity;
import com.gdm.musicplayer.activities.LocalMusicListActivity;
import com.gdm.musicplayer.activities.MyCollectionActivity;
import com.gdm.musicplayer.activities.RecentlyPlayActivity;
import com.gdm.musicplayer.adapter.MyRecyclerViewAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.MusicList;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.download.DataBase;
import com.gdm.musicplayer.utils.MusicUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.Serializable;
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
    private ArrayList<Music> musicsLocal=new ArrayList<>();  //数据源1 本地音乐
    private ArrayList<Music> musicsRently=new ArrayList<>();  //数据源2 最近播放
    private ArrayList<Music> musicsDown=new ArrayList<>();  //数据源3  下载管理
    private ArrayList<Music> musicsCollect=new ArrayList<>();  //数据源4 收藏
    private ArrayList<ArrayList<Music>> data=new ArrayList<>();
    private MyApplication application;
    private User user=null;
    private SharedPreferences sp;
    private String musicsJson;
    private MyAddMusicReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application= (MyApplication) getActivity().getApplication();
        sp=getActivity().getSharedPreferences("recently", Context.MODE_PRIVATE);
        musicsJson = sp.getString("musics", "");
        user=application.getUser();
        receiver=new MyAddMusicReceiver();
        IntentFilter filter=new IntentFilter(PlayListActivity.FLAG);
        getActivity().registerReceiver(receiver,filter);
        initData();
    }

    private void getRecentlyData(String musicsJson) {
        try {
            JSONArray array = new JSONArray(musicsJson.trim());
            for(int i=0;i<array.length();i++){
                JSONObject job = array.getJSONObject(i);
                Music music=new Music();
                music.setAlbum(job.optString("album"));
                music.setDuration(job.optInt("duration"));
                music.setFileUrl(job.optString("fileUrl"));
                music.setId(job.optInt("id"));
                music.setIsnet(job.optBoolean("isnet"));
                music.setName(job.optString("name"));
                music.setSinger(job.optString("singer"));
                musicsRently.add(music);
            }
        } catch (JSONException e) {
            Log.e("FragmentMy","数据解析出错");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        musicsLocal.addAll((ArrayList<Music>)MusicUtil.getAllSongs(getContext(),"song"));
        if(!musicsJson.equals("")){
            getRecentlyData(musicsJson);
        }
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
        adapter=new MyRecyclerViewAdapter(getContext(),musicLists,content,application);
    }
    private File root=new File(Environment.getExternalStorageDirectory(),"gdm");
    private DataBase db;
    private void initDownMusic() {
        db=DataBase.getDb(getActivity());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private class MyAddMusicReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(PlayListActivity.FLAG)){
                Music music = (Music) intent.getSerializableExtra("add");
                musicsCollect.add(music);
                adapter.notifyDataSetChanged();
                Log.e("---","收到广播--"+musicsCollect.size());
            }
        }
    }
}
