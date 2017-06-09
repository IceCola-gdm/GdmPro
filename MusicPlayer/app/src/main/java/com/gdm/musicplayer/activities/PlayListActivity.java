package com.gdm.musicplayer.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.MList;
import com.gdm.musicplayer.bean.MV;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.download.DataBase;
import com.gdm.musicplayer.download.DownLoadService;
import com.gdm.musicplayer.download.ShouCangDbhelper;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.ToastUtil;
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
    public static final String FLAG="add";
    private MList data;
    private ArrayList<Music> list;
    private RecyclerView show;
    private String musicpath="http://120.24.220.119:8080/music/data/music/";
    private String musicpath2="http://120.24.220.119:8080/music/music/selectListContent";
    private String addMusicPath=MyApplication.BASEPATH+"/music/addMusic2list";
    private MyApplication app;
    private User user=null;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuijian);
        data= (MList) getIntent().getSerializableExtra("data");
        app=MyApplication.getInstance();
        user=app.getUser();
        initView();
        initData();
    }

    private void initData() {
        list=new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(PlayListActivity.this);
        dialog.setMessage("玩命加载中，请稍后。。。");
        dialog.show();
        OkHttpUtils.get(musicpath2)
                .params("listid",data.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                        dialog.cancel();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dialog.cancel();
                    }
                });
    }
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
                    String lrc=obj.optString("lrcfile");
                    Music music = new Music();
                    music.setName(musicname);
                    music.setLrc(lrc);
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
                View v = getLayoutInflater().inflate(R.layout.tuijian_listview_item, null, false);
                return new ContentHolder(v);
            }
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof TitleHolder) {
                TitleHolder h= (TitleHolder) holder;
                h.desc.setText(data.getDiscription());
                Glide.with(PlayListActivity.this).load(data.getImgpath()).bitmapTransform(new BlurTransformation(PlayListActivity.this)).into(h.background);
                Glide.with(PlayListActivity.this).load(data.getImgpath()).into(h.icon);
                h.title.setText(data.getName());
                h.back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }else if(holder instanceof ContentHolder){
                ContentHolder h= (ContentHolder) holder;
                Music music = list.get(position-1);
                h.imgSetting.setTag(position-1);
                h.imgSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos= (int) v.getTag();
                        show(pos);
                    }
                });
                h.name.setText(music.getName());
                h.singer.setText(music.getSinger()+"-"+music.getAlbum());
                h.contentview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(PlayListActivity.this, PlayActivity.class);
                        intent1.putExtra("data",list);
                        intent1.putExtra("position",position-1);
                        intent1.putExtra("anim","start");
                        startActivity(intent1);

                        MyApplication ap= (MyApplication) getApplication();
                        ap.setMusics(list);
                        Intent intent = new Intent(MyService.mAction);
                        intent.putExtra("cmd","chose_pos");
                        intent.putExtra("pos",position-1);
                        intent.putExtra("data",list);
                        intent.putExtra("flag",1);
                        sendBroadcast(intent);
                        finish();
                    }
                });
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
            ImageView icon,background,back;
            public TitleHolder(View itemView) {
                super(itemView);
                title= (TextView) itemView.findViewById(R.id.activity_play_list_name);
                desc= (TextView) itemView.findViewById(R.id.activity_play_list_discription);
                icon= (ImageView) itemView.findViewById(R.id.activity_play_list_img);
                background= (ImageView) itemView.findViewById(R.id.background);
                back= (ImageView) itemView.findViewById(R.id.img_song_list_back);
            }
        }
        class ContentHolder extends RecyclerView.ViewHolder{
            private TextView name,singer;
            private ImageView imgSetting;
            private RelativeLayout contentview;
            public ContentHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.tv_song_name);
                singer= (TextView) itemView.findViewById(R.id.tv_song_singer);
                imgSetting= (ImageView) itemView.findViewById(R.id.img_song_set);
                contentview= (RelativeLayout) itemView.findViewById(R.id.contentview);
            }
        }
    }

    private void show(int pos) {
        dialog = new AlertDialog.Builder(PlayListActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.play_item_operation);
        TextView tvName = (TextView) dialog.getWindow().findViewById(R.id.tv_sn);
        TextView tvSingerName = (TextView) dialog.getWindow().findViewById(R.id.t_singer);
        TextView tvAlbumName = (TextView) dialog.getWindow().findViewById(R.id.t_album);
        RelativeLayout rlAdd= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_add);
        RelativeLayout rlMV= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_mv);
        RelativeLayout rlDown= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_down);
        rlAdd.setTag(pos);
        rlDown.setTag(pos);
        rlMV.setTag(pos);
        tvName.setText(list.get(pos).getName());
        tvAlbumName.setText(list.get(pos).getAlbum());
        tvSingerName.setText(list.get(pos).getSinger());
        rlAdd.setOnClickListener(new MyItemListener());
        rlDown.setOnClickListener(new MyItemListener());
        rlMV.setOnClickListener(new MyItemListener());
    }

    private class MyItemListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(app.isLogin()){
                int posit= (int) v.getTag();
                switch (v.getId()){
                    case R.id.rl_add:
                        add(posit);
                        break;
                    case R.id.rl_mv:
                        mv(posit);
                        break;
                    case R.id.rl_down:
                        down(posit);
                        break;
                }
                dialog.dismiss();
            }else{
                ToastUtil.toast(PlayListActivity.this,"亲，还没有登录哟");
            }
        }
    }
    private void down(int pos) {
        Music music = list.get(pos);
        boolean isxiazai = DataBase.getDb(PlayListActivity.this).isxiazai(music.getName());
        if(isxiazai){
            Intent it = new Intent(PlayListActivity.this, DownLoadService.class);
            it.putExtra("music",music);
            startService(it);
        }else{
            ToastUtil.toast(PlayListActivity.this,"已下载");
        }
    }
    private void mv(int pos) {
        ArrayList<MV> mvs = new ArrayList<>();
        Music music = list.get(pos);
        if(music.getMvPath()!=null&&!music.getMvPath().equals("暂无")){
            MV mv = new MV();
            mv.setUrl(music.getMvPath());
            mv.setSinger(music.getSinger());
            mv.setImg(music.getImgPath());
            mv.setName(music.getName());
            mv.setAlbum(music.getAlbum());
            mv.setDuration(music.getDuration()+"");
            mvs.add(mv);
            Intent intent = new Intent(PlayListActivity.this, MVPlayActivity.class);
            intent.putExtra("pos",0);
            intent.putExtra("data",mvs);
            startActivity(intent);

        }else{
            ToastUtil.toast(PlayListActivity.this,"没有MV文件");
        }
    }

    private void add(int pos) {
        Music music = list.get(pos);
        ShouCangDbhelper instance = ShouCangDbhelper.getInstance(PlayListActivity.this);
        boolean isshouchang = DataBase.getDb(PlayListActivity.this).isshouchang(music.getName());
        if(isshouchang){
            instance.shoucang(music);
            if(music!=null){
                Intent intent = new Intent(PlayListActivity.FLAG);
                intent.putExtra("add",music);
                sendBroadcast(intent);
                ToastUtil.toast(PlayListActivity.this,"收藏成功");
            }
        }else{
            ToastUtil.toast(PlayListActivity.this,"已收藏");
        }
    }
}
