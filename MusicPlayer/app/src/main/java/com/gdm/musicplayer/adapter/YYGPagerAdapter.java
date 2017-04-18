package com.gdm.musicplayer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17 0017.
 * 音乐馆推荐
 */
public class YYGPagerAdapter extends PagerAdapter {
    private ArrayList<String> imgs;
    private Context context;

    public YYGPagerAdapter(Context context, ArrayList<String> imgs) {
        this.context = context;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Glide.with(context).load(imgs.get(position%imgs.size())).into(imageView);
        container.addView(imageView);
        return container;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        if(object instanceof View){
            container.removeView((View) object);
        }
    }
}
