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
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.MV;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class ShiPingListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MV> mvs;
    private LayoutInflater inflater;

    public ShiPingListViewAdapter(Context context, ArrayList<MV> mvs) {
        this.context = context;
        this.mvs = mvs;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mvs.size()==0?0:mvs.size();
    }

    @Override
    public Object getItem(int position) {
        return mvs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.fragment_shiping_listview_item,parent,false);
            holder=new ViewHolder();
            holder.tvInfo= (TextView) convertView.findViewById(R.id.tv_shiping_songinfo);
            holder.imgCover= (ImageView) convertView.findViewById(R.id.img_shiping_cover);
            holder.imgPlay= (ImageView) convertView.findViewById(R.id.img_shiping_play);
            holder.imgPlay.setTag(position);
            convertView.setTag(holder);
        }else{
           holder= (ViewHolder) convertView.getTag();
        }
        MV mv= (MV) getItem(position);
        holder.tvInfo.setText(mv.getName()+"-"+mv.getSinger());
        if(!mv.getImg().equals("暂无")){
            Glide.with(context).load(MyApplication.BASEMUSICIIMGPATH+mv.getImg()).error(R.drawable.ahb).into(holder.imgCover);
        }
        holder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                if(listener!=null){
                    listener.click(pos);
                }
            }
        });
        return convertView;
    }
    class ViewHolder{
        public TextView tvInfo;
        public ImageView imgPlay;
        public ImageView imgCover;
    }
    public interface OnClickListener{
        void click(int pos);
    }
    private OnClickListener listener=null;
    public void setListener(OnClickListener listener){
        this.listener=listener;
    }
}
