package com.gdm.musicplayer.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gdm.musicplayer.bean.Music;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by 10789 on 2017-05-14.
 */

public class DataBase  {
    private SQLiteOpenHelper helper;
    private Context mContext;
    private SQLiteDatabase wdatabase;
    private SQLiteDatabase rdatabase;
    private String TAG="DataBase";
    private DataBase(Context context){
        helper=new DB(context);
        mContext=context;
        wdatabase=helper.getWritableDatabase();
        rdatabase=helper.getReadableDatabase();
    }

    /**
     * 插入数据
     */
    public int insertMusic(Music m){
        ContentValues values = new ContentValues();
        values.put("name", m.getName());
        values.put("type",0);
        values.put("state",0);
        if (m.getSinger()!=null) {
            values.put("singer",m.getSinger());
        }

        if (m.getAlbum()!=null) {
            values.put("album",m.getAlbum());
        }

        values.put("fileUrl",m.getFileUrl());
        if (m.getSize()!=null) {
            values.put("size",m.getSize());
        }

        if (m.getImgPath()!=null) {
            values.put("imgPath",m.getImgPath());
        }

        if (m.getMvPath()!=null) {
            values.put("mvPath",m.getMvPath());
        }
        if (m.getLrc()!=null){
            values.put("lrc",m.getLrc());
        }

        long l = wdatabase.insert("down", null, values);
        Log.e(TAG,"插入数据成功"+l);
        return m.getId();
    }

    /**
     * 更新
     * @param id id
     * @param state 状态 0.等待 1.下载中 2.下载完成
     */
    public void update(Integer id,Integer state,String path){
        if (state!=null) {
            ContentValues values = new ContentValues();
            values.put("state",state);
            wdatabase.execSQL("update down set state="+'"'+state+'"'+"where id="+id);
        }
        Log.e(TAG,"UPDATE");
    }
    public void delete(Integer id){

    }
    public void update(String name,Integer state,String path){
        if (state!=null) {
            ContentValues values = new ContentValues();
            values.put("state",state);
            wdatabase.execSQL("update down set state="+'"'+state+'"'+"where name="+'"'+name+'"');
        }
        Log.e(TAG,"UPDATE");
    }

    public List<Music> selectMusic(int mtype){
        Cursor cursor = rdatabase.rawQuery("select * from down where state=?", new String[]{mtype+""});
        List<Music> list=new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String msuciname = cursor.getString(cursor.getColumnIndex("name"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String singer = cursor.getString(cursor.getColumnIndex("singer"));
                String album = cursor.getString(cursor.getColumnIndex("album"));
                String fileUrl = cursor.getString(cursor.getColumnIndex("fileUrl"));
                String size = cursor.getString(cursor.getColumnIndex("size"));
                String imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));
                String mvPath = cursor.getString(cursor.getColumnIndex("mvPath"));
                String lrc = cursor.getString(cursor.getColumnIndex("lrc"));
                String state = cursor.getString(cursor.getColumnIndex("state"));
                Music m = new Music(msuciname, id, 0, singer, album, fileUrl, size, imgPath, mvPath, lrc, false);
                list.add(m);
            } while (cursor.moveToNext());

        }
        Log.e("down","音悦数+"+list.size());
        return list;
    }
    private static DataBase db;
    public boolean isxiazai(String name){
        Cursor cursor = rdatabase.rawQuery("select * from down where name=?", new String[]{name});
        if (cursor.moveToFirst()) {
            do {
                return false;
            } while (cursor.moveToNext());
        }
        return true;
    }
    public boolean isshouchang(String name){
        Cursor cursor = rdatabase.rawQuery("select * from shoucang where name=?", new String[]{name});
        if (cursor.moveToFirst()) {
            do {
                return false;
            } while (cursor.moveToNext());
        }
        return true;
    }
    public static DataBase getDb(Context context) {
        if (db==null) {
            db=new DataBase(context);
        }
        return db;
    }
}
