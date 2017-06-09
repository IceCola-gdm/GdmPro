package com.gdm.musicplayer.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.MVPlayActivity;
import com.gdm.musicplayer.adapter.ShiPingListViewAdapter;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.MV;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class FragmentShiPing extends Fragment {
    private ListView listView;
    private ArrayList<MV> mvs=new ArrayList<>();
    private ShiPingListViewAdapter adapter;
    private String path="http://120.24.220.119:8080/music/music/getMv";
    private MV mv=null;
    private ProgressDialog dialog;
    private SwipeRefreshLayout sw;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=new ProgressDialog(getContext());
        dialog.setMessage("海量MV正在加载中，请稍后");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shiping, container, false);
        listView= (ListView) view.findViewById(R.id.fragment_shiping_listview);
        sw= (SwipeRefreshLayout) view.findViewById(R.id.sw);
        sw.setOnRefreshListener(new MyRefreshListener());
        getData();
        return view;
    }

    private void getData() {
        dialog.show();
        OkHttpUtils.get(path)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dialog.dismiss();
                        parse(s);
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dialog.dismiss();
                        ToastUtil.toast(getContext(),e.getMessage());
                    }
                });
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.optString("message").equals("搜索成功")){
                JSONArray array = job.optJSONArray("data");
                for(int i=0;i<array.length();i++) {
                    JSONObject obj = array.getJSONObject(i);
                    mv = new MV();
                    mv.setName(obj.optString("name"));
                    mv.setAlbum(obj.optString("album"));
                    mv.setImg(obj.optString("imgpath"));
                    mv.setUrl(obj.optString("mvpath"));
                    mvs.add(mv);
                }
                adapter=new ShiPingListViewAdapter(getContext(),mvs);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new MyItemListener());
            }else{
                ToastUtil.toast(getContext(),job.optString("message"));
            }
        } catch (JSONException e) {
            Log.e("FragmentShiPing","数据解析出错");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MyService.mAction);
            intent.putExtra("cmd","pause");
            getActivity().sendBroadcast(intent);
            Intent intent1 = new Intent(getContext(), MVPlayActivity.class);
            intent1.putExtra("pos",position);
            intent1.putExtra("data",mvs);
            startActivity(intent1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.cancel();
    }

    private class MyRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            getData();
        }
    }
}
