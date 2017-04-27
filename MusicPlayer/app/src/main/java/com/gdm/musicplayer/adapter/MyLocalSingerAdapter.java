package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Singer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class MyLocalSingerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Singer> singers;
    private LayoutInflater inflater;

    public MyLocalSingerAdapter(Context context, ArrayList<Singer> singers) {
        this.context = context;
        this.singers = singers;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return singers.size()==0?0:singers.size();
    }

    @Override
    public Object getItem(int position) {
        return singers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.fragment_singer_listview_item,parent,false);
            holder=new ViewHolder();
            holder.imgCover= (ImageView) convertView.findViewById(R.id.img_singer_icon);
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_singer_name);
            holder.tvCount= (TextView) convertView.findViewById(R.id.tv_singer_yinyuecount);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Singer singer= (Singer) getItem(position);
        holder.tvName.setText(singer.getName());
        holder.tvCount.setText(singer.getMusics().size()+"首");
        return convertView;
    }
    class ViewHolder{
        public ImageView imgCover;
        public TextView tvName;
        public TextView tvCount;
    }
}
