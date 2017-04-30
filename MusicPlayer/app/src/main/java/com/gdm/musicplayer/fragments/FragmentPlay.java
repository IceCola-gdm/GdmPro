package com.gdm.musicplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.view.RoundImageView;

/**
 * Created by Administrator on 2017/4/19 0019.
 */
public class FragmentPlay extends Fragment {
    private RoundImageView imgCD;
    private ImageView imgPortrait;
    private ImageView imgBar;
    RotateAnimation rotate;
    RotateAnimation rotate2;
    RotateAnimation rotate3;
    RotateAnimation rotate4;
    LinearInterpolator lin;
    private String state;
    private int pos;
    private MyBroadcastReceiver receiver;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter("palyactivity");
        getActivity().registerReceiver(receiver,filter);
    }

    private void initData() {
        lin= new LinearInterpolator();
        createAnim();
    }

    private void createAnim() {
        rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(lin);
        rotate.setDuration(9000);//设置动画持续时间
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setStartOffset(1000);

        rotate2  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate2.setInterpolator(lin);
        rotate2.setDuration(9000);//设置动画持续时间
        rotate2.setRepeatCount(-1);//设置重复次数
        rotate.setStartOffset(1000);

        rotate3  = new RotateAnimation(0f,20f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        rotate3.setInterpolator(lin);
        rotate3.setDuration(1000);//设置动画持续时间
        rotate3.setFillAfter(true);//动画执行完后是否停留在执行完的状态

        rotate4  = new RotateAnimation(20f, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        rotate4.setInterpolator(lin);
        rotate4.setDuration(1000);//设置动画持续时间
        rotate4.setFillAfter(true);//动画执行完后是否停留在执行完的状态

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        imgCD= (RoundImageView) view.findViewById(R.id.rl_play);
        imgPortrait= (ImageView) view.findViewById(R.id.img_play_portrait);
        imgBar= (ImageView) view.findViewById(R.id.img_bar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("palyactivity")){
                Log.e("---","收到广播");
                state=intent.getStringExtra("state");
                if (state.equals("play")) {
                    imgBar.startAnimation(rotate3);
                    imgCD.startAnimation(rotate);
                    imgPortrait.startAnimation(rotate2);
                }else if(state.equals("stop")){
                    imgCD.clearAnimation();
                    imgPortrait.clearAnimation();
                    imgBar.setAnimation(rotate4);
                }
            }
        }
    }
}
