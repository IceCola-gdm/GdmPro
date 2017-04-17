package com.gdm.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.YYGGeDanGridViewAdapter;
import com.gdm.musicplayer.adapter.YYGPagerAdapter;
import com.gdm.musicplayer.bean.YYGGeDan;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class FragmentYYGGeDan extends Fragment {
    private GridView gridView;
    private ArrayList<YYGGeDan> gedan=new ArrayList<>();
    private YYGGeDanGridViewAdapter adapter=null;
    private LayoutInflater inflater;
    private TextView textViewType;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yyg_gedan, container, false);
        gridView= (GridView) view.findViewById(R.id.yyg_gedan_gridview);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        addHeader();
        adapter=new YYGGeDanGridViewAdapter(gedan,getContext());
        gridView.setAdapter(adapter);
    }

    private void addHeader() {
        View view = inflater.inflate(R.layout.fragment_yyg_gedan_header,null);
        textViewType = (TextView) view.findViewById(R.id.tv_type);
        TextView tvHuaYu= (TextView) view.findViewById(R.id.tv_huayu);
        TextView tvYaoGun= (TextView) view.findViewById(R.id.tv_yaogun);
        TextView tvMinYao= (TextView) view.findViewById(R.id.tv_minyao);
        tvHuaYu.setOnClickListener(new MyListener());
        tvYaoGun.setOnClickListener(new MyListener());
        tvMinYao.setOnClickListener(new MyListener());

    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String res="";
            switch (v.getId()){
                case R.id.tv_huayu:
                    res="华语";
                    break;
                case R.id.tv_yaogun:
                    res="摇滚";
                    break;
                case R.id.tv_minyao:
                    res="民谣";
                    break;
            }
            textViewType.setText(res);
        }
    }
}
