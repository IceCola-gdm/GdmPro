package com.gdm.musicplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.gdm.musicplayer.bean.Album;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.Singer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class MusicUtil {
    public static ArrayList<?> getAllSongs(Context context,String type) {
        ArrayList<Music> songs = null;
        ArrayList<Singer> singers=null;
        ArrayList<Album> albums=null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DATA,},
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[]{"audio/mpeg", "audio/x-ms-wma"}, null);
        songs = new ArrayList<>();
        singers=new ArrayList<>();
        albums=new ArrayList<>();
        if (cursor.moveToFirst()) {
            Music song = null;
            Singer singer=null;
            Album album=null;
            do {
                song = new Music();
                singer=new Singer();
                album=new Album();

                song.setName(cursor.getString(0));  // 文件名
//              song.setTitle(cursor.getString(2));  // 歌曲名
                song.setDuration(cursor.getInt(1));  // 时长
                song.setSinger(cursor.getString(2)); // 歌手名
                song.setAlbum(cursor.getString(3));  // 专辑名
                singer.setName(cursor.getString(2));
                singers.add(singer);
                album.setName(cursor.getString(3));
                albums.add(album);
                if (cursor.getString(4) != null) {
                    song.setFileUrl(cursor.getString(4));  // 文件路径
                }
                songs.add(song);
            } while (cursor.moveToNext());
            cursor.close();
        }
        for(int i=0;i<singers.size();i++){
            ArrayList<Music> musics=new ArrayList<>();
            for(int j=0;j<songs.size();j++){
                Music music = songs.get(j);
                if(music.getSinger().equals(singers.get(j).getName())){
                    musics.add(music);
                }
                singers.get(j).setMusics(musics);
            }
        }
        for(int i=0;i<albums.size();i++){
            ArrayList<Music> musics=new ArrayList<>();
            for(int j=0;j<songs.size();j++){
                Music music = songs.get(j);
                if(music.getAlbum().equals(albums.get(j).getName())){
                    musics.add(music);
                }
                albums.get(j).setMusics(musics);
            }
        }

        if(type.equals("song")){
            return songs;
        }else if(type.equals("singer")){
            return singers;
        }else if(type.equals("album")){
            return albums;
        }
       return null;
    }
}
