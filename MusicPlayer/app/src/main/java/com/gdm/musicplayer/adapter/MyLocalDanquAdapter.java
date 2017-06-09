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
import com.gdm.musicplayer.activities.PlayActivity;
import com.gdm.musicplayer.activities.PlayListActivity;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.MV;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.download.DataBase;
import com.gdm.musicplayer.download.DownLoadService;
import com.gdm.musicplayer.download.ShouCangDbhelper;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.ToastUtil;

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
    private RelativeLayout rlDown;

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
                intent.putExtra("data",musics);
                intent.putExtra("flag",0);   //0为本地音乐
                context.sendBroadcast(intent);

                Intent intent2 = new Intent(context, PlayActivity.class);
                intent2.putExtra("data",musics);
                intent2.putExtra("position",position);
                intent2.putExtra("state","play");
                intent2.putExtra("anim","start");
                context.startActivity(intent2);
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
            rlDown= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_down);
            rlAdd.setTag(pos);
            rlMV.setTag(pos);
            rlDown.setTag(pos);
            tvName.setText(musics.get(pos).getName());
            tvAlbumName.setText(musics.get(pos).getAlbum());
            tvSingerName.setText(musics.get(pos).getSinger());
            rlAdd.setOnClickListener(new MyItemListener());
            rlDown.setOnClickListener(new MyItemListener());
            rlMV.setOnClickListener(new MyItemListener());
        }
    }

    private class MyItemListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_add:
                    addCollect();
                    break;
                case R.id.rl_mv:
                    mvPlay();
                    break;
                case R.id.rl_down:
                    ToastUtil.toast(context,"已经是本地数据了");
                    break;
            }
        }
    }

    private void mvPlay() {
        ArrayList<MV> mvs = new ArrayList<>();
        int tag2 = (int) rlMV.getTag();
        Music music = musics.get(tag2);
        if(music.getMvPath()!=null){
            if(!music.getMvPath().equals("暂无")&&!music.getMvPath().equals("")&&!music.getMvPath().endsWith("暂无")){
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
            }else{
                ToastUtil.toast(context,"没有该MV文件");
            }
        }else{
            ToastUtil.toast(context,"没有该MV文件");
        }
    }

    private void addCollect() {
        int tag = (int) rlAdd.getTag();
        Music music1 = musics.get(tag);
        ShouCangDbhelper instance = ShouCangDbhelper.getInstance(context);
        boolean isshouchang = DataBase.getDb(context).isshouchang(music1.getName());
        if(isshouchang){
            instance.shoucang(music1);
            if(music1!=null){
                Intent intent = new Intent(PlayListActivity.FLAG);
                intent.putExtra("add",music1);
                context.sendBroadcast(intent);
                ToastUtil.toast(context,"收藏成功");
            }
        }else{
            ToastUtil.toast(context,"已收藏");
        }
    }
}
