package com.gdm.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int HEAD=0;
    private static int CONTENT=1;
    private static int BOTTOM=2;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int res=0;
        if(position==0){
            res=HEAD;
        }else if(position==1){
            res=CONTENT;
        }else if(position==2){
            res=BOTTOM;
        }
        return res;
    }
}
