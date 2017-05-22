package com.gdm.musicplayer.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import java.io.File;
import java.util.ArrayList;
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
     * @param name 名称
     */
    public int insertData(String name,Integer type,Integer state){
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("type",type);
        values.put("state",state);
        long l = wdatabase.insert("down", 0+"", values);
        Log.e(TAG,"插入数据成功"+l);
        return (int) l;
    }

    /**
     * 更新
     * @param id id
     * @param state 状态 0.等待 1.下载中 2.下载完成
     */
    public void update(Integer id,Integer state){

        if (state!=null) {
            ContentValues values = new ContentValues();
            values.put("state",state);
            wdatabase.execSQL("update down set state="+'"'+state+'"'+"where id="+id);
        }
        Log.e(TAG,"UPDATE");
    }
    public void delete(Integer id){

    }
    /**
     * 查询
     * @param id
     * @return
     */
    public ArrayList<Down> selectDown(@Nullable Integer id){
        ArrayList<Down> downs = new ArrayList<>();
        if (id!=null) {
            Cursor cursor = rdatabase.rawQuery("select * from down where id=?", new String[]{id + ""});
            if (cursor.moveToFirst()) {
                int mid = cursor.getInt(0);
                String name = cursor.getString(1);
                int type = cursor.getInt(2);
                int state = cursor.getInt(3);
                Down down = new Down();
                down.setId(mid);
                down.setName(name);
                down.setState(state);
                down.setType(type);
                downs.add(down);

            }
        }
        Log.e(TAG,"查询成功"+downs.size());
        return downs;
    }
    public ArrayList<Down> getAllByType(Integer type){
        ArrayList<Down> downs = new ArrayList<>();
        Cursor cursor = rdatabase.rawQuery("select * from down where type="+type,null);
        if (cursor.moveToFirst()) {
           do {
               int mid = cursor.getInt(0);
               String name = cursor.getString(1);
               int state = cursor.getInt(3);
               Down down = new Down();
               down.setId(mid);
               down.setName(name);
               down.setState(state);
               down.setType(type);
               downs.add(down);
           }while (cursor.moveToNext());
        }
        return downs;
    }
    public Down selectDown(String name){
        Cursor cursor = rdatabase.rawQuery("select * from down where name=?", new String[]{name});
        if (cursor.moveToFirst()) {
            int mid = cursor.getInt(0);
            int type = cursor.getInt(2);
            int state = cursor.getInt(3);
            Down down = new Down();
            down.setId(mid);
            down.setName(name);
            down.setState(state);
            down.setType(type);
            return down;
        }
        return null;
    }
    public class Down{
        private int id;
        private int state;
        private int type;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Down{" +
                    "id=" + id +
                    ", state=" + state +
                    ", type=" + type +
                    ", name='" + name + '\'' +
                    '}';
        }

        public Down() {
        }
    }
    private static DataBase db;

    public static DataBase getDb(Context context) {
        if (db==null) {
            db=new DataBase(context);
        }
        return db;
    }

}
