package com.gdm.musicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.User;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentPersonalAbout extends Fragment {
    private TextView textView;
    private String mes="";
    private User user=null;
    private MyApplication app;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MyApplication) getActivity().getApplication();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_aboutme, container, false);
        textView= (TextView) view.findViewById(R.id.fragment_personal_aboutme_tv);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user=app.getUser();
        mes="昵称："+user.getNickname()+"\n\n"+"性别："+user.getSex()+"\n\n"+"生日："+user.getBirthday()+"\n\n"+"地址："+user.getAddress()+"\n\n"+"大学："+user.getDaxue()+"\n\n"+"心情："+user.getHeart();
        textView.setText(mes);
    }
}
