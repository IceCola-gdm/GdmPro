package com.gdm.musicplayer.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PaihangbangListActivity;
import com.gdm.musicplayer.adapter.YYGPaiHangBangAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.PaiHangBang;
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
public class FragmentYYGPaiHang extends Fragment {
    private ListView listView;
    private ArrayList<PaiHangBang> paiHangBangs=new ArrayList<>();
    private YYGPaiHangBangAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private int[] imgs={R.drawable.ktv,R.drawable.hot,R.drawable.e,R.drawable.top,R.drawable.h,R.drawable.billboard,R.drawable.phb};
    private String[] names={"KTV麦霸","热歌榜","Beatport","中国TOP排行榜","Melon周音源榜","Billboard"};
    private ProgressDialog dialog;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=new ProgressDialog(getContext());
        dialog.setMessage("精彩内容马上揭晓");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yyg_paihang, container, false);
        listView= (ListView) view.findViewById(R.id.listview);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.yyg_paihang_refresh);
        listView.setOnItemClickListener(new MyItemListener());
        refreshLayout.setOnRefreshListener(new MyRefreshListener());
        getData();
        listView.setOnItemClickListener(new MyItemListener());
        return view;
    }

    /**
     * 从服务器获取数据
     */
    private void getData() {
        dialog.show();

        OkHttpUtils.get("http://120.24.220.119:8080/music/music/getPaiHang")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        paiHangBangs.clear();
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
            JSONObject js = new JSONObject(s.trim());
            JSONArray da = js.getJSONArray("data");
            for (int i = 0; i < da.length(); i++) {
                JSONArray d = da.getJSONArray(i);
                ArrayList<Music> m= new ArrayList<>();
                PaiHangBang bang = new PaiHangBang();
                String listname=null;
                int listid=0;
                for (int j = 0; j < d.length(); j++) {
                    JSONObject b = d.getJSONObject(j);
                    int id = b.getInt("musicid");
                    String musicname = b.getString("musicname");
                    String path="http://120.24.220.119:8080/music/data/music/"+b.getString("path");
                    String author=b.getString("author");
                    String mvpath = b.getString("mvpath");
                    String size = b.getString("size");
                    String impath = b.getString("imgpath");
                    String lrcfile = b.getString("lrcfile");
                    listname = b.getString("listname");
                    listid=b.getInt("listid");
                    Music music = new Music();
                    music.setId(id);
                    music.setFileUrl(path);
                    music.setName(musicname);
                    music.setSinger(author);
                    music.setMvPath(mvpath);
                    music.setSize(size);
                    music.setImgPath(impath);
                    music.setLrc(lrcfile);
                    m.add(music);
                }
                bang.setId(listid);
                bang.setMusics(m);
                bang.setName(names[i]);
                bang.setDescription("");
                bang.setImg(imgs[i]);
                paiHangBangs.add(bang);
            }
            adapter=new YYGPaiHangBangAdapter(paiHangBangs,getActivity());
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setAdapter() {
        adapter=new YYGPaiHangBangAdapter(paiHangBangs,getContext());
        listView.setAdapter(adapter);
    }

    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), PaihangbangListActivity.class);
            PaiHangBang paiHangBang = paiHangBangs.get(position);
            intent.putExtra("data",paiHangBang);
            startActivity(intent);
        }
    }

    private class MyRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            getData();
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.cancel();
    }
}
