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
import com.gdm.musicplayer.bean.YYGGeDan;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class YYGGeDanGridViewAdapter extends BaseAdapter {
    private ArrayList<YYGGeDan> gedans;
    private Context context;
    private LayoutInflater inflater;

    public YYGGeDanGridViewAdapter(ArrayList<YYGGeDan> gedans, Context context) {
        this.gedans = gedans;
        this.context = context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gedans.size();
    }

    @Override
    public Object getItem(int position) {
        return gedans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.fragment_yyg_gedan_item,parent,false);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.img_yyg_gridview_cover);
            holder.textViewAuthorName= (TextView) convertView.findViewById(R.id.tv_name);
            holder.textViewCount= (TextView) convertView.findViewById(R.id.tv_count);
            holder.textViewTitle= (TextView) convertView.findViewById(R.id.tv_yyg_gedan_title);
            convertView.setTag(holder);

        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        YYGGeDan y = (YYGGeDan) getItem(position);
        holder.textViewCount.setText(y.getCount()+"万");
        holder.textViewAuthorName.setText(y.getTitle());
        holder.textViewAuthorName.setText(y.getAuthorName());
        Glide.with(context).load(y.getImgPath()).into(holder.imageView);
        return convertView;
    }
    private class ViewHolder{
        public TextView textViewCount;
        public TextView textViewAuthorName;
        public TextView textViewTitle;
        public ImageView imageView;
    }
}
