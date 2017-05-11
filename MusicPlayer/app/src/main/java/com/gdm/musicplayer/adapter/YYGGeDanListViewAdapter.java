package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/11 0011.
 */
public class YYGGeDanListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Music> musics;

    public YYGGeDanListViewAdapter(Context context, ArrayList<Music> musics) {
        this.context = context;
        this.musics = musics;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return musics.size()==0?0:musics.size();
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
            convertView=inflater.inflate(R.layout.tuijian_listview_item,parent,false);
            holder=new ViewHolder();
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_song_name);
            holder.tvInfo= (TextView) convertView.findViewById(R.id.tv_song_singer);
            holder.imgSetting= (ImageView) convertView.findViewById(R.id.img_song_setting);
            holder.rl= (RelativeLayout) convertView.findViewById(R.id.rl);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        Music music = (Music) getItem(position);
        holder.tvName.setText(music.getName());
        holder.tvInfo.setText(music.getSinger()+"-"+music.getAlbum());
        holder.rl.setVisibility(View.INVISIBLE);
        holder.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(context,"点击了");
            }
        });
        return convertView;
    }
    class ViewHolder{
        public TextView tvName;
        public TextView tvInfo;
        public ImageView imgSetting;
        public RelativeLayout rl;
    }
}
