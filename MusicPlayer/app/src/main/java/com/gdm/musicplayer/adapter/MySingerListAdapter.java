package com.gdm.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.MVPlayActivity;
import com.gdm.musicplayer.bean.MV;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/2 0002.
 */
public class MySingerListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Music> musics;
    private LayoutInflater inflater;
    private RelativeLayout rlAdd;
    private RelativeLayout rlMV;

    public MySingerListAdapter(Context context, ArrayList<Music> musics) {
        this.context = context;
        this.musics = musics;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return musics.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.localmusiclist_listview_item,parent,false);
            holder=new ViewHolder();
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_song_name);
            holder.textView= (TextView) convertView.findViewById(R.id.tv_song_singer);
            holder.imageView= (ImageView) convertView.findViewById(R.id.img_song_setting);
            holder.imageView.setTag(position);
            convertView.setTag(holder);

        }else{
           holder= (ViewHolder) convertView.getTag();
        }
        Music music = (Music) getItem(position);
        holder.tvName.setText(music.getName());
        holder.textView.setText(music.getSinger()+"-"+music.getAlbum());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int pos= (int) v.getTag();
                Music music1 = musics.get(pos);
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.show();
                dialog.getWindow().setContentView(R.layout.play_item_operation);
                TextView tvName = (TextView) dialog.getWindow().findViewById(R.id.tv_sn);
                TextView tvSingerName = (TextView) dialog.getWindow().findViewById(R.id.t_singer);
                TextView tvAlbumName = (TextView) dialog.getWindow().findViewById(R.id.t_album);
                rlAdd= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_add);
                rlMV= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_mv);
                rlAdd.setTag(pos);
                rlMV.setTag(pos);
                if(music1.getMvPath()==null||music1.getMvPath().equals("暂无")){
                    rlMV.setClickable(false);
                }else{
                    rlMV.setClickable(true);
                }
                RelativeLayout rlDown= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_down);
                rlDown.setClickable(false);
                tvName.setText(musics.get(pos).getName());
                tvAlbumName.setText(musics.get(pos).getAlbum());
                tvSingerName.setText(musics.get(pos).getSinger());
                rlAdd.setOnClickListener(new MyItemListener());
//                rlDown.setOnClickListener(new MyItemListener());
                rlMV.setOnClickListener(new MyItemListener());
            }
        });
        return convertView;
    }
    class ViewHolder{
        public TextView tvName;
        public TextView textView;
        public ImageView imageView;
    }

    private class MyItemListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_add:
                    int pos= (int) rlAdd.getTag();
                    break;
                case R.id.rl_mv:
                    int pos2= (int) rlMV.getTag();
                    Music music = musics.get(pos2);
                    if(music.getMvPath()==null||music.getMvPath().equals("暂无")){
                        ToastUtil.toast(context,"没有mv文件");
                    }else{
                        ArrayList<MV> mvs = new ArrayList<>();
                        MV mv = new MV();
                        mv.setAlbum(music.getAlbum());
                        mv.setDuration(music.getDuration()+"");
                        mv.setName(music.getName());
                        mv.setImg(music.getImgPath());
                        mv.setSinger(music.getSinger());
                        mv.setUrl(music.getMvPath());
                        mvs.add(mv);
                        Intent intent = new Intent(context, MVPlayActivity.class);
                        intent.putExtra("pos",pos2);
                        intent.putExtra("data",mvs);
                        context.startActivity(intent);
                    }
                    break;
            }
        }
    }
}
