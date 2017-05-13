package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Music;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class PHBListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Music> musics;
    private int[] imgs;

    public PHBListAdapter(Context context, ArrayList<Music> musics,int[] imgs) {
        this.context = context;
        this.musics = musics;
        this.imgs=imgs;
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
            convertView=inflater.inflate(R.layout.phb_list_listview_item,parent,false);
            holder=new ViewHolder();
            holder.imgNo= (ImageView) convertView.findViewById(R.id.img_number);
            holder.tvNo= (TextView) convertView.findViewById(R.id.tv_number);
            holder.tvName= (TextView) convertView.findViewById(R.id.name);
            holder.tvInfo= (TextView) convertView.findViewById(R.id.info);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Music music= (Music) getItem(position);
        if(position<3){
            holder.imgNo.setVisibility(View.VISIBLE);
            holder.tvNo.setVisibility(View.INVISIBLE);
            holder.imgNo.setImageResource(imgs[position]);
        }else{
            holder.imgNo.setVisibility(View.INVISIBLE);
            holder.tvNo.setVisibility(View.VISIBLE);
            holder.tvNo.setText(position+"");
        }
        holder.tvName.setText(music.getName());
        holder.tvInfo.setText(music.getSinger()+"-"+music.getAlbum());
        return convertView;
    }
    class ViewHolder{
        public TextView tvNo;
        public ImageView imgNo;
        public TextView tvName;
        public TextView tvInfo;
    }
}
