package com.gdm.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class FragmentYYGGeDan extends Fragment {
    private ListView listView;
    private ArrayList<Music> musics=new ArrayList<>();
    private YYGGeDanListViewAdapter adapter;
    private Music music=null;
    private LayoutInflater inflater;
    private TextView textViewType;
    private String baseMusicPath="http://120.24.220.119:8080/music/data/music/";
    private String baseMusicImgPath="http://120.24.220.119:8080/music/data/music/img/";
    private String baseLrcPath="http://120.24.220.119:8080/music/data/music/lrc/";
    private String baseMvPath="http://120.24.220.119:8080/music/data/mv/";
    private static final String PATH="http://120.24.220.119:8080/music/music/getAllMusic";
    private Thread thread;
    private int[] imgs={R.drawable.abk,R.drawable.abl,R.drawable.abm,R.drawable.abn};
    private Thread thread1;
    private ImageView imageView;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yyg_gedan, container, false);
        listView= (ListView) view.findViewById(R.id.yyg_gedan_listview);
        getData();
        return view;
    }

    private void getData() {
        OkHttpUtils.get(PATH)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                    }
                });
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.optString("message").equals("查询成功")){
                JSONArray data = job.optJSONArray("data");
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
        addHeader();
        setAdapter();
        setListener();
    }

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(MyService.mAction);
                intent1.putExtra("cmd","chose_pos");
                intent1.putExtra("pos",position-1);
                intent1.putExtra("data",musics);
                getActivity().sendBroadcast(intent1);

                Intent intent = new Intent(getContext(), PlayActivity.class);
                intent.putExtra("data",musics);
                intent.putExtra("position",position-1);
                intent.putExtra("state","play");
                startActivity(intent);
            }
        });
    }

    private void setAdapter() {
        adapter=new YYGGeDanListViewAdapter(getContext(),musics);
        listView.setAdapter(adapter);
    }

    private void addHeader() {
        View view = inflater.inflate(R.layout.fragment_yyg_gedan_header,null);
        textViewType = (TextView) view.findViewById(R.id.tv_type);
        TextView tvHuaYu= (TextView) view.findViewById(R.id.tv_huayu);
        TextView tvYaoGun= (TextView) view.findViewById(R.id.tv_yaogun);
        TextView tvMinYao= (TextView) view.findViewById(R.id.tv_minyao);
        tvHuaYu.setOnClickListener(new MyListener());
        tvYaoGun.setOnClickListener(new MyListener());
        tvMinYao.setOnClickListener(new MyListener());
        listView.addHeaderView(view);
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String res="";
            switch (v.getId()){
                case R.id.tv_huayu:
                    res="华语";
                    break;
                case R.id.tv_yaogun:
                    res="英语";
                    break;
                case R.id.tv_minyao:
                    res="韩语";
                    break;
            }
            textViewType.setText(res);
        }
    }
}
