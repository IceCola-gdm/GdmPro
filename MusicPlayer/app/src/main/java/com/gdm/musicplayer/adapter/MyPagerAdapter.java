package com.gdm.musicplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fgs;

    public MyPagerAdapter(FragmentManager fm,ArrayList<Fragment> fgs) {
        super(fm);
        this.fgs=fgs;
    }


    @Override
    public Fragment getItem(int position) {
        return fgs.get(position);
    }

    @Override
    public int getCount() {
        return fgs.size();
    }
}
