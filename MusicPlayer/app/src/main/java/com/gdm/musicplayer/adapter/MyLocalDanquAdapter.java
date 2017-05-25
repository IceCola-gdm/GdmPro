package com.gdm.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.MVPlayActivity;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.MV;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.service.MyService;
import java.util.ArrayList;


/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class MyLocalDanquAdapter extends BaseAdapter {
    private ArrayList<Music> musics;
    private Context context;
    private LayoutInflater inflater;
    private RelativeLayout rlMV;
    private RelativeLayout rlAdd;

    public MyLocalDanquAdapter(ArrayList<Music> musics, Context context) {
        this.musics = musics;
        this.context = context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return musics.size()==0?0:musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.localmusiclist_listview_item,parent,false);
            holder=new ViewHolder();
            holder.imgSetting= (ImageView) convertView.findViewById(R.id.img_song_setting);
            holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_song_name);
            holder.tvMusicInfo= (TextView) convertView.findViewById(R.id.tv_song_singer);
            holder.imgSetting= (ImageView) convertView.findViewById(R.id.img_song_setting);
            convertView.setTag(holder);
            holder.imgSetting.setTag(position);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Music music= (Music) getItem(position);
        final RelativeLayout content= (RelativeLayout) convertView.findViewById(R.id.contentview);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication ap= MyApplication.getInstance();
                ap.setMusics(musics);
                Intent intent = new Intent(MyService.mAction);
                intent.putExtra("cmd","chose_pos");
                intent.putExtra("pos",position);
                context.sendBroadcast(intent);
            }
        });
        holder.tvTitle.setText(music.getName());
        if (music.getSinger()!=null&&music.getAlbum()!=null) {
            holder.tvMusicInfo.setText(music.getSinger()+"-"+music.getAlbum());
        }
        holder.imgSetting.setOnClickListener(new MyListener());
        return convertView;
    }

    class ViewHolder{
        public TextView tvTitle;
        public TextView tvMusicInfo;
        public ImageView imgSetting;
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int pos= (int) v.getTag();
            Music m = musics.get(pos);
            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.show();
            dialog.getWindow().setContentView(R.layout.play_item_operation);
            TextView tvName = (TextView) dialog.getWindow().findViewById(R.id.tv_sn);
            TextView tvSingerName = (TextView) dialog.getWindow().findViewById(R.id.t_singer);
            TextView tvAlbumName = (TextView) dialog.getWindow().findViewById(R.id.t_album);
            rlAdd= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_add);
            rlMV= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_mv);
            if(m.getMvPath()==null||m.getMvPath().equals("暂无")) {
                rlMV.setClickable(false);
            }else{
                rlMV.setClickable(true);
            }
            RelativeLayout rlDown= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_down);
            rlDown.setClickable(false);
            rlAdd.setTag(pos);
            rlMV.setTag(pos);
            tvName.setText(musics.get(pos).getName());
            tvAlbumName.setText(musics.get(pos).getAlbum());
            tvSingerName.setText(musics.get(pos).getSinger());
            rlAdd.setOnClickListener(new MyItemListener());
//            rlDown.setOnClickListener(new MyItemListener());
            rlMV.setOnClickListener(new MyItemListener());
        }
    }

    private class MyItemListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_add:
                    int tag = (int) rlAdd.getTag();
                    break;
                case R.id.rl_mv:
                    ArrayList<MV> mvs = new ArrayList<>();
                    int tag2 = (int) rlMV.getTag();
                    Music music = musics.get(tag2);
                    if(music.getMvPath()!=null){
                        if(!music.getMvPath().equals("暂无")){
                            MV mv = new MV();
                            mv.setSinger(music.getSinger());
                            mv.setUrl(music.getMvPath());
                            mv.setImg(music.getImgPath());
                            mv.setName(music.getName());
                            mv.setDuration(music.getDuration()+"");
                            mv.setAlbum(music.getAlbum());
                            mvs.add(mv);
                            Intent intent = new Intent(context, MVPlayActivity.class);
                            intent.putExtra("pos",tag2);
                            intent.putExtra("data",mvs);
                            context.startActivity(intent);
                        }
                    }

                    break;
            }
        }
    }
}
