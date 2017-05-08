package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.gdm.musicplayer.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.MList;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.service.MyService;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Response;

public class PlayListActivity extends AppCompatActivity {
    private MList data;
    private ArrayList<Music> list;
    private RecyclerView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuijian);
        data= (MList) getIntent().getSerializableExtra("data");
        initView();
        initData();
    }

    private void initData() {
        list=new ArrayList<>();
        OkHttpUtils.get("http://120.24.220.119:8080/music/music/selectListContent")
                .params("listid",data.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                    }
                });
    }
    private String musicpath="http://120.24.220.119:8080/music/data/music/";
    private void parse(String s) {
        try {
            JSONObject js = new JSONObject(s);
            String message = js.getString("message");
            if (message.equals("查询成功")) {
                JSONArray data = js.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject obj = data.getJSONObject(i);
                    String musicname = obj.getString("musicname");
                    String path = obj.getString("path");
                    Music music = new Music();
                    music.setName(musicname);
                    music.setFileUrl(musicpath+path);
                    music.setSinger(obj.getString("author"));
                    list.add(music);
                }
                show.setAdapter(new ListContentAdt());
                show.setLayoutManager(new GridLayoutManager(this,1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        show= (RecyclerView) findViewById(R.id.list);
    }
    class ListContentAdt extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==1) {
                View v = getLayoutInflater().inflate(R.layout.item_play_list_title, null, false);
                return new TitleHolder(v);
            }else {
                View v = getLayoutInflater().inflate(R.layout.localmusiclist_listview_item, null, false);
                return new ContentHolder(v);
            }
//            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof TitleHolder) {
                TitleHolder h= (TitleHolder) holder;
                h.desc.setText(data.getDiscription());
                Glide.with(PlayListActivity.this).load(data.getImgpath()).bitmapTransform(new BlurTransformation(PlayListActivity.this)).into(h.background);
                Glide.with(PlayListActivity.this).load(data.getImgpath()).into(h.icon);
                h.title.setText(data.getName());
            }else if(holder instanceof ContentHolder){
                ContentHolder h= (ContentHolder) holder;
                Music music = list.get(position-1);
                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication ap= (MyApplication) getApplication();
                        ap.setMusics(list);
                        Intent intent = new Intent(MyService.mAction);
                        intent.putExtra("cmd","chose_pos");
                        intent.putExtra("pos",position-1);
                        sendBroadcast(intent);

                    }
                });
                h.name.setText(music.getName());
                h.singer.setText(music.getSinger());
            }
        }

        @Override
        public int getItemCount() {
            return list.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0) {
                return 1;
            }else {
                return 2;
            }
        }

        class TitleHolder extends RecyclerView.ViewHolder{
            TextView title,desc;
            ImageView icon,background;
            public TitleHolder(View itemView) {
                super(itemView);
                title= (TextView) itemView.findViewById(R.id.activity_play_list_name);
                desc= (TextView) itemView.findViewById(R.id.activity_play_list_discription);
                icon= (ImageView) itemView.findViewById(R.id.activity_play_list_img);
                background= (ImageView) itemView.findViewById(R.id.background);
            }
        }
        class ContentHolder extends RecyclerView.ViewHolder{
            private TextView name,singer;
            public ContentHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.tv_song_name);
                singer= (TextView) itemView.findViewById(R.id.tv_song_singer);
            }
        }
    }
}
