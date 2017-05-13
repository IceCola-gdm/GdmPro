package com.gdm.musicplayer.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.MVPlayActivity;
import com.gdm.musicplayer.adapter.ShiPingListViewAdapter;
import com.gdm.musicplayer.bean.MV;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentShiPing extends Fragment {
    private ListView listView;
    private ArrayList<MV> mvs=new ArrayList<>();
    private ShiPingListViewAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shiping, container, false);
        listView= (ListView) view.findViewById(R.id.fragment_shiping_listview);
        getData();
        return view;
    }

    private void getData() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter=new ShiPingListViewAdapter(getContext(),mvs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new MyItemListener());
    }

    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
}
