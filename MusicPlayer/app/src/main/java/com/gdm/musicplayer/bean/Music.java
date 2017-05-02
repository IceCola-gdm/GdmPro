package com.gdm.musicplayer.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class Music implements Serializable{
    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private boolean isnet=true;
    private int duration;
    private String singer;
    private String album;
    private String fileUrl;

    public boolean isnet() {
        return isnet;
    }

    public void setIsnet(boolean isnet) {
        this.isnet = isnet;
    }

    public Music(String name, int duration, String singer, String album, String fileUrl) {
        this.name = name;
        this.duration = duration;
        this.singer = singer;
        this.album = album;
        this.fileUrl = fileUrl;
}

    public Music() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "Music{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", singer='" + singer + '\'' +
                ", album='" + album + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
