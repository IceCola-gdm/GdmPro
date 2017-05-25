package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Music;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class YYGGeDanRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Music> musics;
    private Context context;
    private LayoutInflater inflater;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder m= (MyHolder) holder;
        Music music = musics.get(position);
        m.tvName.setText(music.getName());
        m.tvInfo.setText(music.getSinger()+"-"+music.getAlbum());
        if(listener!=null){
            listener.onItemClick(position);
        }
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvInfo;
        ImageView imgSetting;
        public MyHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.tv_song_name);
            tvInfo= (TextView) itemView.findViewById(R.id.tv_song_singer);
            imgSetting= (ImageView) itemView.findViewById(R.id.img_song_setting);
        }
    }
    public interface onItemListener{
        void onItemClick(int pos);
    }
    private onItemListener listener=null;
    public void setListener(onItemListener listener){
        this.listener=listener;
    }

}
