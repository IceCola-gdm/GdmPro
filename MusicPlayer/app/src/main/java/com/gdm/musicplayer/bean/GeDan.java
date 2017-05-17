package com.gdm.musicplayer.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/16 0016.
 */
public class GeDan {
    private String name;
    private int id;
    private ArrayList<Music> musics;

    public GeDan(String name, int id, ArrayList<Music> musics) {
        this.name = name;
        this.id = id;
        this.musics = musics;
    }

    public GeDan() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }

    @Override
    public String toString() {
        return "GeDan{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", musics=" + musics +
                '}';
    }
}
