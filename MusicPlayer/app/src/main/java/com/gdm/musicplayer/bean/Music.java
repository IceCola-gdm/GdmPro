package com.gdm.musicplayer.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class Music implements Serializable{
    private String name;
    private int id;
    private int duration;
    private String singer;
    private String album;
    private String fileUrl;
    private String size;
    private String imgPath;
    private String mvPath;
    private String lrc;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    private boolean isnet=true;
    public boolean isnet() {
        return isnet;
    }
    public void setIsnet(boolean isnet) {
        this.isnet = isnet;
    }

    public Music() {
    }

    public Music(String name, int id, int duration, String singer, String album, String fileUrl, String size, String imgPath, String mvPath, String lrc, boolean isnet) {
        this.name = name;
        this.id = id;
        this.duration = duration;
        this.singer = singer;
        this.album = album;
        this.fileUrl = fileUrl;
        this.size = size;
        this.imgPath = imgPath;
        this.mvPath = mvPath;
        this.lrc = lrc;
        this.isnet = isnet;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getMvPath() {
        return mvPath;
    }

    public void setMvPath(String mvPath) {
        this.mvPath = mvPath;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    @Override
    public String toString() {
        return "Music{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", duration=" + duration +
                ", singer='" + singer + '\'' +
                ", album='" + album + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", size='" + size + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", mvPath='" + mvPath + '\'' +
                ", lrc='" + lrc + '\'' +
                ", isnet=" + isnet +
                '}';
    }
}
