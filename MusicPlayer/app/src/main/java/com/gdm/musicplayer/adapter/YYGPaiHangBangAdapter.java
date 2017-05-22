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
import com.gdm.musicplayer.bean.PaiHangBang;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class YYGPaiHangBangAdapter extends BaseAdapter {
    private ArrayList<PaiHangBang> paiHangBangs;
    private Context context;
    private LayoutInflater inflater;

    public YYGPaiHangBangAdapter(ArrayList<PaiHangBang> paiHangBangs, Context context) {
        this.paiHangBangs = paiHangBangs;
        this.context = context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return paiHangBangs.size()==0?0:paiHangBangs.size();
    }

    @Override
    public Object getItem(int position) {
        return paiHangBangs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.yyg_paihangbang_item1,parent,false);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.img_item1_cover);
            holder.tv1= (TextView) convertView.findViewById(R.id.tv_no1);
            holder.tv2= (TextView) convertView.findViewById(R.id.tv_no2);
            holder.tv3= (TextView) convertView.findViewById(R.id.tv_no3);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        PaiHangBang paiHangBang= (PaiHangBang) getItem(position);
        ArrayList<Music> musics = paiHangBang.getMusics();
        if (musics.size()>3) {
            holder.tv1.setText(musics.get(0).getName());
            holder.tv2.setText(musics.get(1).getName());
            holder.tv3.setText(musics.get(2).getName());
        }
        Glide.with(context).load(paiHangBang.getImg()).error(R.drawable.phb).into(holder.imageView);
        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
    }
}
