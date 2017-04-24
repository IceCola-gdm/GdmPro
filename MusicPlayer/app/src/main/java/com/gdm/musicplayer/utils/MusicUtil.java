package com.gdm.musicplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.gdm.musicplayer.bean.Music;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class MusicUtil {
    public static ArrayList<Music> getAllSongs(Context context) {

        ArrayList<Music> songs = null;
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

        songs = new ArrayList<Music>();
        if (cursor.moveToFirst()) {
            Music song = null;
            do {
                song = new Music();
                // 文件名
                song.setName(cursor.getString(0));
//                // 歌曲名
//                song.setTitle(cursor.getString(2));
                // 时长
                song.setDuration(cursor.getInt(1));
                // 歌手名
                song.setSinger(cursor.getString(2));
                // 专辑名
                song.setAlbum(cursor.getString(3));
                // 文件路径
                if (cursor.getString(4) != null) {
                    song.setFileUrl(cursor.getString(4));
                }
                songs.add(song);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return songs;
    }
}
