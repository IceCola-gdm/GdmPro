package com.gdm.musicplayer.bean;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class MV{
    private String name;
    private String img;
    private String url;
    private String description;
    private String singer;
    private String album;
    private String duration;

    public MV(String name, String img, String url, String description, String singer, String album, String duration) {
        this.name = name;
        this.img = img;
        this.url = url;
        this.description = description;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
    }

    public MV() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "MV{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", singer='" + singer + '\'' +
                ", album='" + album + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
