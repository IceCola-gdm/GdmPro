package com.gdm.musicplayer.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PlayActivity;
import com.gdm.musicplayer.adapter.YYGGeDanListViewAdapter;
import com.gdm.musicplayer.adapter.YYGGeDanRecycleViewAdapter;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class FragmentYYGGeDan extends Fragment implements YYGGeDanRecycleViewAdapter.onItemListener {
    private RecyclerView listView;
    private ArrayList<Music> musics=new ArrayList<>();
    private YYGGeDanRecycleViewAdapter adapter;
    private Music music=null;
    private String baseMusicPath="http://120.24.220.119:8080/music/data/music/";
    private String baseMusicImgPath="http://120.24.220.119:8080/music/data/music/img/";
    private String baseLrcPath="http://120.24.220.119:8080/music/data/music/lrc/";
    private String baseMvPath="http://120.24.220.119:8080/music/data/mv/";
    private static final String PATH="http://120.24.220.119:8080/music/music/getAllMusic";
    private SwipeRefreshLayout refreshLayout;
    private ProgressDialog dialog;
    private int page=1;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=new ProgressDialog(getContext());
        dialog.setMessage("精彩稍后继续");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yyg_gedan, container, false);
        listView= (RecyclerView) view.findViewById(R.id.yyg_gedan_listview);
        setAdapter();
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.yyg_gedan_fresh);
        refreshLayout.setOnRefreshListener(new MyRefreshListener());
        getData();
        return view;
    }

    private void getData() {
        dialog.show();
        OkHttpUtils.get(PATH)
                .params("pageNum",page)
                .params("pageSize",20)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dialog.dismiss();
                        parse(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dialog.dismiss();
                        ToastUtil.toast(getContext(),e.getMessage());
                    }
                });
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.optString("message").equals("查询成功")){
                ToastUtil.toast(getContext(),job.optString("message"));
                JSONArray data = job.optJSONArray("data");
                musics=new ArrayList<>();
                for(int i=0;i<data.length();i++){
                    JSONObject obj = data.optJSONObject(i);
                    music=new Music();
                    music.setId(obj.optInt("musicid"));
                    music.setName(obj.optString("name"));
                    if(obj.optString("path")!=null&&obj.optString("path")!=""){
                        music.setFileUrl(baseMusicPath+obj.optString("path"));
                    }
                    music.setSinger(obj.optString("author"));
                    music.setAlbum(obj.optString("album"));
                    music.setSize(obj.optString("size"));
                    if(obj.optString("imgpath")!=null&&obj.optString("imgpath")!=""){
                        music.setImgPath(baseMusicImgPath+obj.optString("imgpath"));
                    }
                    if(obj.optString("mvpath")!=null&&obj.optString("mvpath")!=""){
                        music.setMvPath(baseMvPath+obj.optString("mvpath"));
                    }
                    if(obj.optString("lrcfile")!=null&&obj.optString("lrcfile")!=""){
                        music.setLrc(baseLrcPath+obj.optString("lrcfile"));
                    }
                    musics.add(music);
                }
                setAdapter();
            }else{
                ToastUtil.toast(getContext(),job.optString("message"));
            }
        } catch (JSONException e) {
            Log.e("FragmnetYYGGeDan","数据解析出错");
        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setAdapter() {
        adapter=new YYGGeDanRecycleViewAdapter(musics,getActivity());
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter.setListener(this);
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent1 = new Intent(getContext(), PlayActivity.class);
        intent1.putExtra("data",musics);
        intent1.putExtra("position",pos);
        intent1.putExtra("anim","start");
        startActivity(intent1);

        MyApplication ap= (MyApplication) getActivity().getApplication();
        ap.setMusics(musics);
        Intent intent = new Intent(MyService.mAction);
        intent.putExtra("cmd","chose_pos");
        intent.putExtra("pos",pos);
        intent.putExtra("data",musics);
        getActivity().sendBroadcast(intent);
    }

    private class MyRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            page++;
            if(page>10){
                page=1;
            }
            getData();
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.cancel();
    }
}
