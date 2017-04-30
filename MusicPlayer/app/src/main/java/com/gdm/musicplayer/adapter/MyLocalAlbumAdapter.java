package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Album;
import com.gdm.musicplayer.bean.Singer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class MyLocalAlbumAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Album> albums;
    private LayoutInflater inflater;

    public MyLocalAlbumAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.fragment_album_listview_item,parent,false);
            holder=new ViewHolder();
            holder.imgCover= (ImageView) convertView.findViewById(R.id.img_album_icon);
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_album_name);
            holder.tvCount= (TextView) convertView.findViewById(R.id.tv_album_yinyuecount);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Album album= (Album) getItem(position);
        holder.tvName.setText(album.getName());
        holder.tvCount.setText(album.getMusics().size()+"首");
        return convertView;
    }
    class ViewHolder{
        public ImageView imgCover;
        public TextView tvName;
        public TextView tvCount;
    }
}
