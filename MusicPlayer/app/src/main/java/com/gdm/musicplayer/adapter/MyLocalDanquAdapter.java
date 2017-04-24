package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class MyLocalDanquAdapter extends BaseAdapter {
    private ArrayList<Music> musics;
    private Context context;
    private LayoutInflater inflater;

    public MyLocalDanquAdapter(ArrayList<Music> musics, Context context) {
        this.musics = musics;
        this.context = context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.localmusiclist_listview_item,parent,false);
            holder=new ViewHolder();
            holder.imgSetting= (ImageView) convertView.findViewById(R.id.img_song_setting);
            holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_song_name);
            holder.tvMusicInfo= (TextView) convertView.findViewById(R.id.tv_song_singer);
            holder.imgSetting= (ImageView) convertView.findViewById(R.id.img_song_setting);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Music music= (Music) getItem(position);
        holder.tvTitle.setText(music.getName());
        holder.tvMusicInfo.setText(music.getSinger()+"-"+music.getAlbum());
        holder.imgSetting.setOnClickListener(new MyListener());
        return convertView;
    }
    class ViewHolder{
        public TextView tvTitle;
        public TextView tvMusicInfo;
        public ImageView imgSetting;
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ToastUtil.toast(context,"还没写");
        }
    }
}
