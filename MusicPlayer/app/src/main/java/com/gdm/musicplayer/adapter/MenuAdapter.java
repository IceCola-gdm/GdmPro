package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private ArrayList<Music> musics;
    private Context context;
    private LayoutInflater inflater;

    public MenuAdapter(ArrayList<Music> musics, Context context) {
        this.musics = musics;
        this.context = context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    private RecyclerView.ViewHolder buffer;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Music music=musics.get(position);
        holder.tvName.setText(music.getName());
        holder.tvSinger.setText(music.getSinger());
        holder.tvName.setTextColor(Color.BLACK);
        holder.tvSinger.setTextColor(Color.BLACK);
        holder.imgDelete.setOnClickListener(new MyListener());
//        if (itemClickListener!=null){
//            itemClickListener.itemClick(position);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener!=null){
                    if (buffer!=null) {
                        ViewHolder b= (ViewHolder) buffer;
                        b.tvName.setTextColor(Color.BLACK);
                        b.tvSinger.setTextColor(Color.BLACK);
                    }
                    itemClickListener.itemClick(position);
                    holder.tvName.setTextColor(Color.RED);
                    holder.tvSinger.setTextColor(Color.RED);
                    buffer=holder;
        }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvSinger;
        public ImageView imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSinger= (TextView) itemView.findViewById(R.id.tv_singer);
            tvName= (TextView) itemView.findViewById(R.id.tv_menu_songname);
            imgDelete= (ImageView) itemView.findViewById(R.id.img_delete);
        }
    }
    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ToastUtil.toast(context,"还没写");
        }
    }
    public interface OnMyItemClickListener{
        void itemClick(int pos);
    }
    private OnMyItemClickListener itemClickListener=null;
    public void setListener(OnMyItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
}
