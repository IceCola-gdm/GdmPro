package com.gdm.musicplayer.bean;

/**
 * Created by Administrator on 2017/4/29 0029.
 */
public class UserInfro {
    public static User user;
    public static void setUser(User user) {
        UserInfro.user = user;
    }

    public static User getUser() {
        return user;
    }
}
