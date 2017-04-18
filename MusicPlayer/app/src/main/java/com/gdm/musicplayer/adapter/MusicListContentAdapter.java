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
 * Created by 10789 on 2017-04-18.
 */

public class MusicListContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MusicList> datas;
    private LayoutInflater inflater;
    public MusicListContentAdapter(Context context, ArrayList<MusicList> datas) {
        this.context = context;
        this.datas = datas;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.fragment_my_recycler_head2,parent,false);

        return new ContentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MusicList bean = datas.get(position);
        final ContentHolder h= (ContentHolder) holder;
        h.textViewTitle.setText(bean.getTitle());
        h.textViewCount.setText("("+bean.getNum()+")");
        Glide.with(context).load(bean.getImgPath()).into(h.imageView);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context,bean.getmClass()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    class ContentHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textViewTitle;
        public TextView textViewCount;
        public ContentHolder(View view) {
            super(view);
            imageView= (ImageView) view.findViewById(R.id.img_head_my);
            textViewTitle= (TextView) view.findViewById(R.id.tv_head_my);
            textViewCount= (TextView) view.findViewById(R.id.tv_head_my_count);
        }
    }
}
