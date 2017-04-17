package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdm.musicplayer.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/16 0016.
 * 自定义皮肤中GridView的Adapter
 */
public class TuiJianGridViewAdapter extends BaseAdapter {
    private ArrayList<Integer> imgs;
    private Context context;
    private LayoutInflater inflater;

    public TuiJianGridViewAdapter(ArrayList<Integer> imgs, Context context) {
        this.imgs = imgs;
        this.context = context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int position) {
        return imgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.tuijian_gridview_item,parent,false);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.img_girdview_item);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        int src = (int) getItem(position);
        holder.imageView.setImageResource(src);
        return convertView;
    }
    private class ViewHolder{
        public ImageView imageView;
    }
}
