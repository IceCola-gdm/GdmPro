package com.gdm.musicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.GeDan;
import com.gdm.musicplayer.bean.User;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentPersonalYinyue extends Fragment {
    private ListView listView;
    private ArrayList<GeDan> gedans=new ArrayList<>();
    private MyApplication app;
    private User user=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MyApplication) getActivity().getApplication();
        user=app.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_yinyue, container, false);
        listView= (ListView) view.findViewById(R.id.fragment_personal_yinyue_listview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
