package com.gdm.musicplayer.fragments;

import android.app.AlertDialog;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.ToastUtil;
import com.gdm.musicplayer.view.RoundImageView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Response;

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
    private MyApplication app;
    private User user;
    private ImageView imgLike;
    private ImageView imgDown;
    private ImageView imgInfo;
    private ImageView imgMes;
    private ImageView imgZan;
    private AlertDialog myDialog;
    private Music lastMusic=new Music();
    private String path="http://120.24.220.119:8080/music/comment/good";

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(MyService.PLAY_ACTION);
        getActivity().registerReceiver(receiver,filter);
        app= (MyApplication) getActivity().getApplication();
        user=app.getUser();
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
        imgLike= (ImageView) view.findViewById(R.id.img_play_like);
        imgDown= (ImageView) view.findViewById(R.id.img_play_down);
        imgInfo= (ImageView) view.findViewById(R.id.img_play_info);
        imgMes= (ImageView) view.findViewById(R.id.img_play_mes);
        imgZan= (ImageView) view.findViewById(R.id.img_play_zan);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
    }

    private void setListener() {
        imgLike.setOnClickListener(new MyListener());
        imgDown.setOnClickListener(new MyListener());
        imgInfo.setOnClickListener(new MyListener());
        imgZan.setOnClickListener(new MyListener());
        imgMes.setOnClickListener(new MyListener());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private boolean animstart=false;
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyService.PLAY_ACTION)){
                state=intent.getStringExtra("state");
                Music music = (Music) intent.getSerializableExtra("music");
                if(music!=null){
                    if(!music.getName().equals(lastMusic.getName())){
                        lastMusic=music;
                    }
                }
                if(state!=null){
                    if (state.equals("play")) {
                        if (animstart) {
                          return;
                        }
                        imgBar.startAnimation(rotate3);
                        imgCD.startAnimation(rotate);
                        imgPortrait.startAnimation(rotate2);
                        animstart=true;
                    }else if(state.equals("stop")){
                        if (!animstart) {
                            return;
                        }
                        imgCD.clearAnimation();
                        imgPortrait.clearAnimation();
                        imgBar.setAnimation(rotate4);
                        animstart=false;
                    }
                }
            }
        }
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(app.isLogin()){
                switch (v.getId()){
                    case R.id.img_play_like:
                        like();
                        break;
                    case R.id.img_play_down:
                        download();
                        break;
                    case R.id.img_play_info:
                        musicoperate();
                        break;
                    case R.id.img_play_mes:
                        mess();
                        break;
                    case R.id.img_play_zan:
                        zan();
                        break;
                }
            }else{
                ToastUtil.toast(getContext(),"还没有登录哟");
            }

        }
    }

    private void mess() {

    }

    private void zan() {
        OkHttpUtils.post(path)
                .params("id",user.getId())
                .params("type",1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("------",s);
                    }
                });
    }

    private void musicoperate() {
        myDialog = new AlertDialog.Builder(getContext()).create();
        myDialog.show();
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        myDialog.getWindow().setAttributes(params);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        myDialog.getWindow().setContentView(R.layout.play_item_operation);
        RelativeLayout rlAdd= (RelativeLayout) myDialog.getWindow().findViewById(R.id.rl_add);
        RelativeLayout rlMv= (RelativeLayout) myDialog.getWindow().findViewById(R.id.rl_mv);
        TextView tvSinger= (TextView) myDialog.getWindow().findViewById(R.id.t_singer);
        TextView tvAlbum= (TextView) myDialog.getWindow().findViewById(R.id.t_album);

    }

    /**
     * 添加下载
     */
    private void download() {

    }

    /**
     * 添加喜欢
     */
    private void like() {

    }
}
