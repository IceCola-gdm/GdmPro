package com.gdm.musicplayer.adapter;

import android.content.Context;
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
public class MenuAdapter extends BaseAdapter {
    private ArrayList<Music> musics;
    private Context context;
    private LayoutInflater inflater;

    public MenuAdapter(ArrayList<Music> musics, Context context) {
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
            convertView=inflater.inflate(R.layout.menu_item,parent,false);
            holder=new ViewHolder();
            holder.tvSinger= (TextView) convertView.findViewById(R.id.tv_singer);
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_menu_songname);
            holder.imgDelete= (ImageView) convertView.findViewById(R.id.img_delete);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Music music= (Music) getItem(position);
        holder.tvName.setText(music.getName());
        holder.tvSinger.setText(music.getSinger());
        holder.imgDelete.setOnClickListener(new MyListener());
        return convertView;
    }
    class ViewHolder{
        public TextView tvName;
        public TextView tvSinger;
        public ImageView imgDelete;
    }
    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ToastUtil.toast(context,"还没写");
        }
    }
}
