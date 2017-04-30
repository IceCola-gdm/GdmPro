package com.gdm.musicplayer.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class Album {
    private String imgpath;
    private String name;
    private ArrayList<Music> musics;

    public Album(String imgpath, String name, ArrayList<Music> musics) {
        this.imgpath = imgpath;
        this.name = name;
        this.musics = musics;
    }

    public Album() {
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }

    @Override
    public String toString() {
        return "Album{" +
                "imgpath='" + imgpath + '\'' +
                ", name='" + name + '\'' +
                ", musics=" + musics +
                '}';
    }
}
