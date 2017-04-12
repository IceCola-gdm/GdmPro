package com.gdm.musicplayer.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class ToastUtil {
    public static void toast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
