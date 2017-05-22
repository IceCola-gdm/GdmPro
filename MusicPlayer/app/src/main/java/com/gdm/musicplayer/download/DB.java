package com.gdm.musicplayer.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gdm.musicplayer.R;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
