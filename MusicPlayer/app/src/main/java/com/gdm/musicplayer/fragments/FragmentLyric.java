package com.gdm.musicplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.view.LrcView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/19 0019.
 */
public class FragmentLyric extends Fragment {
    private LrcView lrc;
    private MyApplication ap;
    private LrcPlayBrod brod;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lyric, container, false);
        lrc= (LrcView) view.findViewById(R.id.musiclrc);
        ap= (MyApplication) getActivity().getApplication();
        brod = new LrcPlayBrod();
        IntentFilter filter=new IntentFilter(MyService.PLAY_ACTION);
        getActivity().registerReceiver(brod,filter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(brod);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private String lrcpath;
    class LrcPlayBrod extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals(MyService.PLAY_ACTION)) {
                if (ap!=null) {
                    Music music= (Music) intent.getSerializableExtra("music");
                    if (music==null) {
                        return;
                    }
                    final String lrcs = music.getLrc();
                    if (lrcpath==null){
                        lrcpath=music.getLrc();
                        lrc.clear();
                        lrc.setVisibility(View.GONE);
                        OkHttpUtils.get("http://120.24.220.119:8080/music/data/music/lrc/"+lrcs)
                                .execute(new FileCallback() {
                                    @Override
                                    public void onSuccess(File file, Call call, Response response) {
                                        try {
                                            FragmentLyric.this.lrc.setLrcPath(file.getAbsolutePath());
                                            lrc.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            Toast.makeText(context, "暂无歌词", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        try {
                                            FragmentLyric.this.lrc.setLrcPath("error");
                                        } catch (Exception e1) {
                                            Toast.makeText(context, "暂无歌词", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{
                        if (!lrcpath.equals(lrcs)) {
                            lrc.setVisibility(View.GONE);
                            lrc.clear();
                            OkHttpUtils.get("http://120.24.220.119:8080/music/data/music/lrc/"+lrcs)
                                    .execute(new FileCallback() {
                                        @Override
                                        public void onSuccess(File file, Call call, Response response) {
                                            try {
                                                lrc.setLrcPath(file.getAbsolutePath());
                                                lrc.setVisibility(View.VISIBLE);
                                                lrcpath=lrcs;
                                            } catch (Exception e) {
                                                Toast.makeText(context, "暂无歌词", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
                                            super.onError(call, response, e);
                                            lrcpath=lrcs;
                                            try {
                                                lrc.setLrcPath("error");
                                            } catch (Exception e1) {
                                                Toast.makeText(context, "暂无歌词", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                    int now = intent.getIntExtra("now", 0);
                    FragmentLyric.this.lrc.changeCurrent(now);
                }
            }
        }
    }
}
