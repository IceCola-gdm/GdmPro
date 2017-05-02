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
        ArrayList<Music> songs = new ArrayList<>();
        ArrayList<String> singerNames=new ArrayList<>();
        ArrayList<String> albumNames=new ArrayList<>();
        ArrayList<Singer> singers=new ArrayList<>();
        ArrayList<Album> albums=new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DATA,},
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[]{"audio/mpeg", "audio/x-ms-wma"}, null);
        if (cursor.moveToFirst()) {
            Music song = null;
            do {
                song = new Music();
                String[] split = cursor.getString(0).split(".mp3");
                song.setName(split[0]);  // 文件名
//              song.setTitle(cursor.getString(2));  // 歌曲名
                song.setDuration(cursor.getInt(1));  // 时长
                song.setSinger(cursor.getString(2)); // 歌手名
                song.setAlbum(cursor.getString(3));  // 专辑名
                if(!cursor.getString(2).equals("<unknown>")&&!singerNames.contains(cursor.getString(2))){
                    singerNames.add(cursor.getString(2));
                }
                if(!cursor.getString(3).equals("<unknown>")&&!albumNames.contains(cursor.getString(3))){
                    albumNames.add(cursor.getString(3));
                }
                if (cursor.getString(4) != null) {
                    song.setFileUrl(cursor.getString(4));  // 文件路径
                }
                songs.add(song);
            } while (cursor.moveToNext());
            cursor.close();
        }

        for(int i=0;i<singerNames.size();i++){
            Singer singer = new Singer();
            singer.setName(singerNames.get(i));
            ArrayList<Music> musics = new ArrayList<>();
            for(int j=0;j<songs.size();j++){
                if (songs.get(j).getSinger().equals(singerNames.get(i))) {
                    musics.add(songs.get(j));
                }
            }
            singer.setMusics(musics);
            singers.add(singer);
        }

        for(int i=0;i<albumNames.size();i++){
            Album album = new Album();
            album.setName(albumNames.get(i));
            ArrayList<Music> musics = new ArrayList<>();
            for(int j=0;j<songs.size();j++){
                if (songs.get(j).getAlbum().equals(albumNames.get(i))) {
                    musics.add(songs.get(j));
                }
            }
            album.setMusics(musics);
            albums.add(album);
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
