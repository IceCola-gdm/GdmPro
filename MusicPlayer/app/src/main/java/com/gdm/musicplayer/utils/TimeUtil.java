package com.gdm.musicplayer.utils;

/**
 * Created by Administrator on 2017/4/29 0029.
 */
public class TimeUtil {
    public static String parse(int time){
        time=time/1000;
        String res="";
        int min = time / 60;
        int sec = time % 60;
        res=min+":"+sec;
        return res;
    }
}
