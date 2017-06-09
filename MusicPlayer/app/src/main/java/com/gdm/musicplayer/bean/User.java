package com.gdm.musicplayer.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/29 0029.
 * 用户实体类
 */
public class User implements Serializable{
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String imgpath;
    private String birthday;
    private String heart;
    private String address;
    private String sex;
    private String daxue;
    private String background;

    public User(int id, String username, String password, String nickname, String imgpath, String birthday, String heart, String address, String sex, String daxue, String background) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.imgpath = imgpath;
        this.birthday = birthday;
        this.heart = heart;
        this.address = address;
        this.sex = sex;
        this.daxue = daxue;
        this.background = background;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDaxue() {
        return daxue;
    }

    public void setDaxue(String daxue) {
        this.daxue = daxue;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", imgpath='" + imgpath + '\'' +
                ", birthday='" + birthday + '\'' +
                ", heart='" + heart + '\'' +
                ", address='" + address + '\'' +
                ", sex='" + sex + '\'' +
                ", daxue='" + daxue + '\'' +
                ", background='" + background + '\'' +
                '}';
    }
}
