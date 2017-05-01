package com.gdm.musicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PlayListActivity;
import com.gdm.musicplayer.adapter.YYGPagerAdapter;
import com.gdm.musicplayer.bean.MList;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.view.MyViewPager;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class FragmentYYGTuiJian extends Fragment {

    private ArrayList<MList> data;
    private ArrayList<MList> title;
    private RecyclerView listview;
    private ListAdt listAdt;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tuijian_fragment, container, false);
        listview= (RecyclerView) view.findViewById(R.id.list);
        initMusicData();
        return view;
    }

    private void initMusicData() {
        data=new ArrayList<>();
        OkHttpUtils.post("http://120.24.220.119:8080/music/music/getTypeList")
                .params("type",2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                    }
                });
    }
    private String listimgpath="http://120.24.220.119:8080/music/image/";
    private void parse(String s) {
        try {
            JSONObject js = new JSONObject(s);
            String message = js.getString("message");
            if (message.equals("搜索成功")) {
                JSONArray data = js.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject obj = data.getJSONObject(i);
                    String name = obj.getString("name");
                    int id=obj.getInt("id");
                    String discription = obj.getString("discription");
                    String imgpath = obj.optString("imgpath", "a48391a7.jpg");
                    MList mList = new MList();
                    mList.setName(name);
                    mList.setId(id);
                    mList.setDiscription(discription);
                    mList.setImgpath(listimgpath+imgpath);
                    this.data.add(mList);
                }
                //TODO
                listAdt = new ListAdt();
                listview.setAdapter(listAdt);
                listview.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class ListAdt extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            if (viewType==PAGE) {
                View view = inflater.inflate(R.layout.item_viewpager, parent, false);
                return new PagerHolder(view);
            }else{
                View view = inflater.inflate(R.layout.tuijian_fragment, parent, false);
                return new GridListHolder(view);
            }
//            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof PagerHolder) {
                PagerHolder h= (PagerHolder) holder;
                MyPagerAdt adt = new MyPagerAdt();
                h.pager.setAdapter(adt);

            }else if(holder instanceof GridListHolder){
                GridListHolder h= (GridListHolder) holder;
                h.datalist.setAdapter(new GridListAdapter());
                h.datalist.setLayoutManager(new GridLayoutManager(getActivity(),2));
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
        private int PAGE=1;
        private int CONTENT=2;
        @Override
        public int getItemViewType(int position) {
            if (1>position) {
                return PAGE;
            }else {
                return CONTENT;
            }
        }

        class PagerHolder extends RecyclerView.ViewHolder{
            ViewPager pager;
            public PagerHolder(View itemView) {
                super(itemView);
                pager= (ViewPager) itemView.findViewById(R.id.mypager);
            }
        }
        class MyPagerAdt extends PagerAdapter{
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = new ImageView(getActivity());
                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                final MList mList = data.get(position);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getActivity()).load(mList.getImgpath()).error(R.mipmap.ic_launcher).into(iv);
                container.addView(iv);
                iv.setClickable(true);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PlayListActivity.class);
                        intent.putExtra("data",mList);
                        getActivity().startActivity(intent);
                    }
                });
                return iv;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        }
        class GridListHolder extends RecyclerView.ViewHolder{
            RecyclerView datalist;
            public GridListHolder(View itemView) {
                super(itemView);
                datalist= (RecyclerView) itemView.findViewById(R.id.list);
            }
        }
        class GridListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_list, null, false);
                return new ContentHolder(v);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ContentHolder h= (ContentHolder) holder;
                MList mList = data.get(position);
                h.title.setText(mList.getName());
                Glide.with(getActivity()).load(mList.getImgpath()).into(h.iv);
            }


            @Override
            public int getItemCount() {
                return data.size();
            }
            class ContentHolder extends RecyclerView.ViewHolder{
                ImageView iv;
                TextView title;
                public ContentHolder(View itemView) {
                    super(itemView);
                    iv= (ImageView) itemView.findViewById(R.id.list_image);
                    title= (TextView) itemView.findViewById(R.id.list_name);
                }
            }
        }
    }
}
