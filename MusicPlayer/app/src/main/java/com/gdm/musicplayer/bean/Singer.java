package com.gdm.musicplayer.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class Singer implements Serializable{
    private String name;
    private String imgPortrait;
    private ArrayList<Music> musics;

    public Singer(String name, String imgPortrait, ArrayList<Music> musics) {
        this.name = name;
        this.imgPortrait = imgPortrait;
        this.musics = musics;
    }

    public Singer() {
    }

    public String getImgPortrait() {
        return imgPortrait;
    }

    public void setImgPortrait(String imgPortrait) {
        this.imgPortrait = imgPortrait;
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
        return "Singer{" +
                "name='" + name + '\'' +
                ", imgPortrait='" + imgPortrait + '\'' +
                ", musics=" + musics +
                '}';
    }
}
