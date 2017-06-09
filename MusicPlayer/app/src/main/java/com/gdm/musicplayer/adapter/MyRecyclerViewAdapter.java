package com.gdm.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.ManageGedanActivity;
import com.gdm.musicplayer.bean.MusicList;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int HEAD = 0;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MusicList> beens;
    private MyApplication app;
    private User user = null;
    private ArrayList<File> files = new ArrayList<>();
    private static final String PATH = Environment.getExternalStorageDirectory() + File.separator + "a.jpg";
    public MyRecyclerViewAdapter(Context context, ArrayList<MusicList> beens, MyApplication app) {
        this.context = context;
        this.beens = beens;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.app = app;
        user = app.getUser();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        view = inflater.inflate(R.layout.fragment_my_recycler_head2, parent, false);
        holder = new HolderOne(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HolderOne) {
            //加载头部的逻辑
            final MusicList bean = beens.get(position);
            final HolderOne h = (HolderOne) holder;
            h.textViewTitle.setText(bean.getTitle());
            h.textViewCount.setText("(" + bean.getNum() + ")");
            Glide.with(context).load(bean.getImgPath()).into(h.imageView);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, bean.getmClass());
                    intent.putExtra("data", beens.get(position).getM());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beens.size();
    }
    private class HolderOne extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;
        public TextView textViewCount;
        public HolderOne(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img_head_my);
            textViewTitle = (TextView) view.findViewById(R.id.tv_head_my);
            textViewCount = (TextView) view.findViewById(R.id.tv_head_my_count);
        }
    }
}
