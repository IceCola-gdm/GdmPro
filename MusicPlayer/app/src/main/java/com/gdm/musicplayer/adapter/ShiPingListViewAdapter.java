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
            holder.imgCollect= (ImageView) convertView.findViewById(R.id.img_shiping_collect);
            holder.imgZan= (ImageView) convertView.findViewById(R.id.img_shiping_zan);
            holder.imgLike= (ImageView) convertView.findViewById(R.id.img_shiping_like);
            holder.imgShare= (ImageView) convertView.findViewById(R.id.img_shiping_share);
            holder.imgDown= (ImageView) convertView.findViewById(R.id.img_shiping_download);

            holder.tvZan= (TextView) convertView.findViewById(R.id.tv_shipping_zan_count);
            holder.tvCollect= (TextView) convertView.findViewById(R.id.tv_shipping_collect_count);
            holder.tvLike= (TextView) convertView.findViewById(R.id.tv_shipping_like_count);
            holder.tvShare= (TextView) convertView.findViewById(R.id.tv_shipping_share_count);
            holder.tvDown= (TextView) convertView.findViewById(R.id.tv_shipping_download_count);

            holder.tvDuration= (TextView) convertView.findViewById(R.id.tv_shiping_time);
            holder.tvInfo= (TextView) convertView.findViewById(R.id.tv_shiping_songinfo);
            holder.imgCover= (ImageView) convertView.findViewById(R.id.img_shiping_cover);
            convertView.setTag(holder);
        }else{
           holder= (ViewHolder) convertView.getTag();
        }
        MV mv= (MV) getItem(position);
        holder.tvInfo.setText(mv.getName()+"-"+mv.getSinger());
        holder.tvDuration.setText(mv.getDuration());
        Glide.with(context).load(mv.getImg()).error(R.drawable.ahb).into(holder.imgCover);
        return convertView;
    }
    class ViewHolder{
        public TextView tvInfo;
        public TextView tvDuration;
        public ImageView imgPlay;
        public ImageView imgCover;
        public ImageView imgZan;
        public ImageView imgLike;
        public ImageView imgCollect;
        public ImageView imgShare;
        public ImageView imgDown;
        public TextView tvZan;
        public TextView tvLike;
        public TextView tvCollect;
        public TextView tvShare;
        public TextView tvDown;
    }
}
