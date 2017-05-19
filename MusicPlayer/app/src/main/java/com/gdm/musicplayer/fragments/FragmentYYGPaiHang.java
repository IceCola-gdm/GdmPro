package com.gdm.musicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PaihangbangListActivity;
import com.gdm.musicplayer.adapter.YYGPaiHangBangAdapter;
import com.gdm.musicplayer.bean.PaiHangBang;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class FragmentYYGPaiHang extends Fragment {
    private ListView listView;
    private ArrayList<PaiHangBang> paiHangBangs=new ArrayList<>();
    private YYGPaiHangBangAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yyg_paihang, container, false);
        listView= (ListView) view.findViewById(R.id.listview);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.yyg_paihang_refresh);
        listView.setOnItemClickListener(new MyItemListener());
        refreshLayout.setOnRefreshListener(new MyRefreshListener());
        getData();
        return view;
    }

    /**
     * 从服务器获取数据
     */
    private void getData() {

    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        adapter=new YYGPaiHangBangAdapter(paiHangBangs,getContext());
        listView.setAdapter(adapter);
    }

    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), PaihangbangListActivity.class);
            PaiHangBang paiHangBang = paiHangBangs.get(position);
            intent.putExtra("data",paiHangBang);
            startActivity(intent);
        }
    }

    private class MyRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            getData();
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }
}
