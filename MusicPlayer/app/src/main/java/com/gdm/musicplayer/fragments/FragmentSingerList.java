package com.gdm.musicplayer.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.PlayActivity;
import com.gdm.musicplayer.adapter.MySingerListAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.Singer;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/2 0002.
 */
public class FragmentSingerList extends Fragment {
    private ArrayList<Music> musics=new ArrayList<>();
    private Singer singer=null;
    private MySingerListAdapter adapter=null;
    private ImageView imgBack;
    private TextView tvSingerName;
    private ImageView imgAll;
    private TextView tvCount;
    private ListView listView;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        singer = (Singer) bundle.getSerializable("singer");
        musics.addAll(singer.getMusics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singer_list, container, false);
        imgBack= (ImageView) view.findViewById(R.id.img_singername_back);
        tvSingerName= (TextView) view.findViewById(R.id.tv_singername);
        imgAll= (ImageView) view.findViewById(R.id.img_singername_icon);
        tvCount= (TextView) view.findViewById(R.id.tv_count);
        listView= (ListView) view.findViewById(R.id.fragment_singer_list_listview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setListener();
    }

    private void setListener() {
        imgBack.setOnClickListener(new MyListener());
        listView.setOnItemClickListener(new MyItemListener());
    }

    private void initView() {
        tvSingerName.setText(singer.getName());
        adapter=new MySingerListAdapter(getContext(),musics);
        listView.setAdapter(adapter);
        tvCount.setText(musics.size()+"首");
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ToastUtil.toast(getContext(),"点击了");
        }
    }

    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MyService.mAction);
            intent.putExtra("cmd","chose_pos");
            intent.putExtra("pos",position);
            getActivity().sendBroadcast(intent);
        }
    }
}
