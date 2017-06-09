package com.gdm.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.MVPlayActivity;
import com.gdm.musicplayer.activities.PlayListActivity;
import com.gdm.musicplayer.bean.MV;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.download.DataBase;
import com.gdm.musicplayer.download.DownLoadService;
import com.gdm.musicplayer.download.ShouCangDbhelper;
import com.gdm.musicplayer.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class YYGGeDanRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Music> musics;
    private Context context;
    private LayoutInflater inflater;
    private RelativeLayout rlAdd;
    private RelativeLayout rlMV;
    private RelativeLayout rlDown;

    public YYGGeDanRecycleViewAdapter(ArrayList<Music> musics, Context context) {
        this.musics = musics;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.tuijian_listview_item,null,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder m= (MyHolder) holder;
        Music music = musics.get(position);
        m.tvName.setText(music.getName());
        m.tvInfo.setText(music.getSinger()+"-"+music.getAlbum());
        m.imgSetting.setTag(position);
        m.imgSetting.setOnClickListener(new MyListener());
        m.contentview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvInfo;
        ImageView imgSetting;
        RelativeLayout contentview;
        public MyHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.tv_song_name);
            tvInfo= (TextView) itemView.findViewById(R.id.tv_song_singer);
            imgSetting= (ImageView) itemView.findViewById(R.id.img_song_set);
            contentview= (RelativeLayout) itemView.findViewById(R.id.contentview);
        }
    }
    public interface onItemListener{
        void onItemClick(int pos);
    }
    private onItemListener listener=null;
    public void setListener(onItemListener listener){
        this.listener=listener;
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            Music m = musics.get(pos);
            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.show();
            dialog.getWindow().setContentView(R.layout.play_item_operation);
            TextView tvName = (TextView) dialog.getWindow().findViewById(R.id.tv_sn);
            TextView tvSingerName = (TextView) dialog.getWindow().findViewById(R.id.t_singer);
            TextView tvAlbumName = (TextView) dialog.getWindow().findViewById(R.id.t_album);
            rlAdd= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_add);
            rlMV= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_mv);

            if(m.getMvPath()==null||m.getMvPath().equals("暂无")) {
                rlMV.setClickable(false);
            }else{
                rlMV.setClickable(true);
            }
            rlDown= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_down);
            rlAdd.setTag(pos);
            rlMV.setTag(pos);
            rlDown.setTag(pos);
            tvName.setText(musics.get(pos).getName());
            tvAlbumName.setText(musics.get(pos).getAlbum());
            tvSingerName.setText(musics.get(pos).getSinger());
            rlAdd.setOnClickListener(new MyClickListener());
            rlDown.setOnClickListener(new MyClickListener());
            rlMV.setOnClickListener(new MyClickListener());

        }
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_add:
                    addCollect();
                    break;
                case R.id.rl_mv:
                    mvPlay();
                    break;
                case R.id.rl_down:
                    down();
                    break;
            }
        }
    }

    private void down() {
        int pos = (int) rlDown.getTag();
        Music music = musics.get(pos);
        boolean isxiazai = DataBase.getDb(context).isxiazai(music.getName());
        if(isxiazai){
            Intent it = new Intent(context, DownLoadService.class);
            it.putExtra("music",music);
            context.startService(it);
        }else{
            ToastUtil.toast(context,"已下载");
        }
    }

    private void mvPlay() {
        int pos = (int) rlAdd.getTag();
        ArrayList<MV> mvs = new ArrayList<>();
        Music music = musics.get(pos);

        if(music.getMvPath()!=null&&!music.getMvPath().equals("暂无")&&!music.getMvPath().endsWith("暂无")){
            MV mv = new MV();
            mv.setUrl(music.getMvPath());
            mv.setSinger(music.getSinger());
            mv.setImg(music.getImgPath());
            mv.setName(music.getName());
            mv.setAlbum(music.getAlbum());
            mv.setDuration(music.getDuration()+"");
            mvs.add(mv);
            Intent intent = new Intent(context, MVPlayActivity.class);
            intent.putExtra("pos",0);
            intent.putExtra("data",mvs);
            context.startActivity(intent);

        }else{
            ToastUtil.toast(context,"没有MV文件");
        }
    }

    private void addCollect() {
        int tag = (int) rlAdd.getTag();
        Music music1 = musics.get(tag);
        ShouCangDbhelper instance = ShouCangDbhelper.getInstance(context);
        boolean isshouchang = DataBase.getDb(context).isshouchang(music1.getName());
        if(isshouchang){
            instance.shoucang(music1);
            if(music1!=null){
                Intent intent = new Intent(PlayListActivity.FLAG);
                intent.putExtra("add",music1);
                context.sendBroadcast(intent);
                ToastUtil.toast(context,"收藏成功");
            }
        }else{
            ToastUtil.toast(context,"已收藏");
        }
    }
}
