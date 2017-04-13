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
 * Created by Administrator on 2017/4/11 0011.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int HEAD=0;
    private static int CONTENT=1;
    private static int BOTTOM=2;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MusicList> beens;

    public MyRecyclerViewAdapter(Context context, ArrayList<MusicList> beens) {
        this.context = context;
        this.beens = beens;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
//        switch (getItemViewType(position)){
//            case 0:
//                HolderOne holderOne=(HolderOne)holder;
//                break;
//            case 1:
//                HolderTwo holderTwo=(HolderTwo)holder;
//                break;
//            case 2:
//                HolderThree holderThree=(HolderThree)holder;
//                break;
//        }
        final MusicList bean = beens.get(position);
        if (holder instanceof HolderOne) {
            //加载头部的逻辑
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

        }
    }

    @Override
    public int getItemCount() {
        return beens.size();
    }

    @Override
    public int getItemViewType(int position) {
//        int res=0;
//        if(position==0){
//            res=HEAD;
//        }else if(position==1){
//            res=CONTENT;
//        }else if(position==2){
//            res=BOTTOM;
//        }
//        return res;
        MusicList bean = beens.get(position);
        switch (bean.getType()){
            case 0:
                return HEAD;
            case 1:return CONTENT;
            case 2:return BOTTOM;
        }
        return 0;
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
        public HolderTwo(View view) {
            super(view);
            imageViewArrow= (ImageView) view.findViewById(R.id.img_content_arrow);
            textViewTitle= (TextView) view.findViewById(R.id.tv_content_title);
            textViewCount= (TextView) view.findViewById(R.id.tv_content_count);
            imageViewEdit= (ImageView) view.findViewById(R.id.img_content_setting);
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
