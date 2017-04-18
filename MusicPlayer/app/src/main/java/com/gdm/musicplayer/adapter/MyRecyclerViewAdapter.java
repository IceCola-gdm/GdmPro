package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.MusicList;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int HEAD=0;
    private static int CONTENT=1;
    private static int BOTTOM=2;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MusicList> beens;
    private ArrayList<MusicList> content;
    public MyRecyclerViewAdapter(Context context, ArrayList<MusicList> beens,ArrayList<MusicList> content) {
        this.context = context;
        this.beens = beens;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.content=content;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        RecyclerView.ViewHolder holder=null;
        switch (viewType){
            case 0:
                view=inflater.inflate(R.layout.fragment_my_recycler_head2,parent,false);
                holder=new HolderOne(view);
                break;
            case 1:
                view=inflater.inflate(R.layout.fragment_my_recycler_content,parent,false);
                holder=new HolderTwo(view);
                break;
            case 2:
                view=inflater.inflate(R.layout.fragment_my_recycler_bottom,parent,false);
                holder=new HolderThree(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HolderOne) {
            //加载头部的逻辑
            final MusicList bean = beens.get(position);
            final HolderOne h= (HolderOne) holder;
            h.textViewTitle.setText(bean.getTitle());
            h.textViewCount.setText("("+bean.getNum()+")");
            Glide.with(context).load(bean.getImgPath()).into(h.imageView);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context,bean.getmClass()));
                }
            });
        }else if (holder instanceof HolderTwo){
            final HolderTwo h= (HolderTwo) holder;
            MusicListContentAdapter adapter = new MusicListContentAdapter(context, beens);
            h.content.setAdapter(adapter);
            h.content.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
            h.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (h.contentparent.getVisibility()==View.VISIBLE) {
                        h.imageViewArrow.setImageResource(R.drawable.right);
                        h.contentparent.setVisibility(View.GONE);
                    }else{
                        h.imageViewArrow.setImageResource(R.drawable.down);
                        h.contentparent.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beens.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==beens.size()) {
            return CONTENT;
        }else {
            return HEAD;
        }
    }

    private class HolderOne extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;
        public TextView textViewCount;
        public HolderOne(View view) {
            super(view);
            imageView= (ImageView) view.findViewById(R.id.img_head_my);
            textViewTitle= (TextView) view.findViewById(R.id.tv_head_my);
            textViewCount= (TextView) view.findViewById(R.id.tv_head_my_count);
        }
    }

    private class HolderTwo extends RecyclerView.ViewHolder {
        public ImageView imageViewArrow;
        public ImageView imageViewEdit;
        public TextView textViewTitle;
        public TextView textViewCount;
        public RecyclerView content;
        public RelativeLayout title,contentparent;
        public HolderTwo(View view) {
            super(view);
            title= (RelativeLayout) view.findViewById(R.id.title);
            imageViewArrow= (ImageView) view.findViewById(R.id.img_content_arrow);
            textViewTitle= (TextView) view.findViewById(R.id.tv_content_title);
            textViewCount= (TextView) view.findViewById(R.id.tv_content_count);
            imageViewEdit= (ImageView) view.findViewById(R.id.img_content_setting);
            content= (RecyclerView) view.findViewById(R.id.fragment_my_recycler_content);
            contentparent= (RelativeLayout) view.findViewById(R.id.contentparent);
        }
    }

    private class HolderThree extends RecyclerView.ViewHolder {
        public ImageView imageViewIcon;
        public ImageView imageViewSetting;
        public TextView textViewTitle;
        public TextView textViewCount;
        public HolderThree(View view) {
            super(view);
            imageViewIcon= (ImageView) view.findViewById(R.id.img_content_icon);
            textViewTitle= (TextView) view.findViewById(R.id.tv_bottom_gedanname);
            textViewCount= (TextView) view.findViewById(R.id.tv_bottom_yinyuecount);
            imageViewSetting= (ImageView) view.findViewById(R.id.img_gedanbianji);
        }
    }
}
