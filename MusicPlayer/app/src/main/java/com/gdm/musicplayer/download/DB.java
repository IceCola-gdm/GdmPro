package com.gdm.musicplayer.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gdm.musicplayer.R;

import io.vov.vitamio.utils.Log;

/**
 * Created by 10789 on 2017-05-14.
 */

public class DB extends SQLiteOpenHelper {
    private Context mContext;
    public DB(Context context) {
        super(context, "musicDown.db", null, 1);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(mContext.getString(R.string.creat_table));
        Log.e("创建列表",R.string.creat_table);
        db.execSQL(mContext.getString(R.string.create_shoucang));
        Log.e("创建列表",R.string.create_shoucang);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
