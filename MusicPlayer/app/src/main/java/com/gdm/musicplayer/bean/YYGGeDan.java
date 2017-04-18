package com.gdm.musicplayer.bean;

/**
 * Created by Administrator on 2017/4/17 0017.
 * 音乐馆GridView  item实体类
 */
public class YYGGeDan {
    private String imgPath;
    private int count;
    private String authorName;
    private String title;

    public YYGGeDan(String imgPath, int count, String title, String authorName) {
        this.imgPath = imgPath;
        this.count = count;
        this.title = title;
        this.authorName = authorName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "YYGGeDan{" +
                "imgPath='" + imgPath + '\'' +
                ", count=" + count +
                ", authorName='" + authorName + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
