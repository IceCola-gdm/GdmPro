package com.gdm.musicplayer.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class PaiHangBang implements Serializable{
    private int id;
    private String name;
    private String description;
    private int img;
    private ArrayList<Music> musics;

    public PaiHangBang(int id, String name, String description, int img, ArrayList<Music> musics) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.img = img;
        this.musics = musics;
    }

    public PaiHangBang() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }

    @Override
    public String toString() {
        return "PaiHangBang{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", img='" + img + '\'' +
                ", musics=" + musics +
                '}';
    }
}
