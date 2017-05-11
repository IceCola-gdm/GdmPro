package com.gdm.musicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.SearchOnlineActivity;
import com.gdm.musicplayer.adapter.MyPagerAdapter;
import com.gdm.musicplayer.utils.ToastUtil;
import com.gdm.musicplayer.view.MyTitleView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/16 0016.
 */
public class FragmentMain extends Fragment {
    private MyTitleView myTitleView;
    private ViewPager viewPager;
    private ArrayList<Fragment> fgs=new ArrayList<>();
    private MyPagerAdapter adapter=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        fgs.add(new FragmentMy());
        fgs.add(new FragmentYinYueGuan());
        fgs.add(new FragmentShiPing());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        myTitleView= (MyTitleView) view.findViewById(R.id.mytitleview);
        viewPager= (ViewPager) view.findViewById(R.id.viewpager);

//        return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        adapter=new MyPagerAdapter(getFragmentManager(),fgs);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }
    /**
     * 设置监听事件
     */
    private void setListener() {
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        myTitleView.setOnLeftImgClick(new MyLeftImgClickListener());
        myTitleView.setOnRightImgClick(new MyRightImgClickListener());
        myTitleView.setOnRadioGroupClick(new MyRadioGroupCheckListener());
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    /**
     * 处理ViewPPager的翻页
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioGroup group = myTitleView.getRadioGroup();
            RadioButton r= (RadioButton) group.getChildAt(position);
            r.setChecked(true);
            r.setTextSize(20);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 处理左边图片的点击事件（抽屉）
     */
    private class MyLeftImgClickListener implements MyTitleView.OnLeftImgClick {
        @Override
        public void onClick(View v) {
            onImgListener.leftImgCallback();
        }
    }

    /**
     * 处理右边图片的点击事件（搜索）
     */
    private class MyRightImgClickListener implements MyTitleView.OnRightImgClick {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), SearchOnlineActivity.class);
            startActivity(intent);
        }
    }

        /**
     * 处理RadioGroup的点击事件
     */
    private class MyRadioGroupCheckListener implements MyTitleView.OnRadioGroupClick {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int childCount = group.getChildCount();
            for(int i=0;i<childCount;i++){
                RadioButton r = (RadioButton) group.getChildAt(i);
                if(r.isChecked()){
                    r.setTextSize(20);
                    viewPager.setCurrentItem(i);
                }else{
                    r.setTextSize(18);
                }
            }
        }
    }

    public interface OnImgListener{
        void leftImgCallback();
        void rightImgCallback();
    }
    private OnImgListener onImgListener=null;
    public void setOnImgListener(OnImgListener listener){
        this.onImgListener=listener;
    }
}
