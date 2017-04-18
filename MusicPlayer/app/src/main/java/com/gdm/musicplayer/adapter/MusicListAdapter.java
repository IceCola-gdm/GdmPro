package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.MusicList;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MusicList> datas;
    private Context context;
    private LayoutInflater inflater;
    public MusicListAdapter(ArrayList<MusicList> datas, Context context) {
        this.datas = datas;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.fragment_my_recycler_bottom,null,false);
        MusicListHolder holder = new MusicListHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MusicListHolder h= (MusicListHolder) holder;
        final MusicList list = datas.get(position);
        h.title.setText(list.getTitle());
        h.num.setText(list.getNum());
        Glide.with(context).load(list.getImgPath()).into(h.img);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,list.getmClass()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    class MusicListHolder extends RecyclerView.ViewHolder{
        private TextView title,num;
        private ImageView img,menu;
        public MusicListHolder(View v) {
            super(v);
            title= (TextView) v.findViewById(R.id.tv_bottom_gedanname);
            num= (TextView) v.findViewById(R.id.tv_bottom_yinyuecount);
            img= (ImageView) v.findViewById(R.id.img_content_icon);
            menu= (ImageView) v.findViewById(R.id.img_gedanbianji);
        }
    }
}
