package com.gdm.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.EditPersonalInfoActivity;
import com.gdm.musicplayer.adapter.MyPagerAdapter;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.ToastUtil;
import com.gdm.musicplayer.view.CircleImageView;
import com.gdm.musicplayer.view.RoundImageView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/17 0017.
 * 个人中心
 */
public class FragmentPersonalInfo extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private ArrayList<Fragment> fgs=new ArrayList<>();
    private String[] titles={"音乐","关于我"};
    private ImageView imgPortrait;
    private ImageView imgBack;
    private ImageView imgBackground;
    private ImageView imgSex;
    private TextView tvNikName;
    private TextView tvEditUserInfo;
    private User user=null;
    private Cursor c;
    private String imgPath;
    private ArrayList<File> files=new ArrayList<>();
    private static final String BASEPORTRAIT="http://120.24.220.119:8080/music/image/port/";
    private static final String BASEBACKGROUND="http://120.24.220.119:8080/music/image/bg/";
    private static final String PATH="http://120.24.220.119:8080/music/user/uploadPort";
    private MyApplication app;
    private SharedPreferences sp;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MyApplication) getActivity().getApplication();
        sp=app.getSp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_info, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tabLayout= (TabLayout) view.findViewById(R.id.personal_tablelayout);
        viewPager= (ViewPager) view.findViewById(R.id.personal_viewpager);
        tabLayout.setupWithViewPager(viewPager);
        imgPortrait= (ImageView) view.findViewById(R.id.img_personalinfo_portrait);
        imgBack= (ImageView) view.findViewById(R.id.img_personalinfo_back);
        imgBackground= (ImageView) view.findViewById(R.id.img_personalinfo_background);
        imgSex= (ImageView) view.findViewById(R.id.img_personalinfo_sex);
        tvNikName= (TextView) view.findViewById(R.id.tv_personalinfo_username);
        tvEditUserInfo= (TextView) view.findViewById(R.id.tv_personalinfo_edit);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setAdapter();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        user=app.getUser();
        tvNikName.setText(user.getNickname());
        Glide.with(getContext()).load(BASEPORTRAIT+user.getImgpath()).error(R.drawable.pp).into(imgPortrait);
        Glide.with(getContext()).load(BASEBACKGROUND+user.getBackground()).error(R.drawable.afc).into(imgBackground);
        String sex = user.getSex();
        if(sex!=null&&sex!=""){
            if(sex.equals("男")){
                Glide.with(getContext()).load(R.drawable.ab1).into(imgSex);
            }else if(sex.equals("女")){
                Glide.with(getContext()).load(R.drawable.ab2).into(imgSex);
            }
        }
    }

    private void setListener() {
        imgBack.setOnClickListener(new MyListener());
        tvEditUserInfo.setOnClickListener(new MyListener());
        imgPortrait.setOnClickListener(new MyListener());
    }

    private void setAdapter() {
        adapter=new MyPagerAdapter(getChildFragmentManager(),fgs);
        adapter.setTitles(titles);
        viewPager.setAdapter(adapter);
    }

    private void initData() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i=0;i<titles.length;i++){
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }
        fgs.add(new FragmentPersonalYinyue());
        fgs.add(new FragmentPersonalAbout());
        app = (MyApplication) getActivity().getApplication();
        user=app.getUser();
        tvNikName.setText(user.getNickname());
        Glide.with(getContext()).load(BASEPORTRAIT+user.getImgpath()).error(R.drawable.pp).into(imgPortrait);
        Glide.with(getContext()).load(BASEBACKGROUND+user.getBackground()).error(R.drawable.afc).into(imgBackground);
        String sex = user.getSex();
        if(sex!=null&&sex!=""){
            if(sex.equals("男")){
                Glide.with(getContext()).load(R.drawable.ab1).into(imgSex);
            }else if(sex.equals("女")){
                Glide.with(getContext()).load(R.drawable.ab2).into(imgSex);
            }
        }
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_personalinfo_back:  //返回
                    ToastUtil.toast(getContext(),"点击了");
//                    removeFragment();
                    break;
                case R.id.tv_personalinfo_edit:  //修改个人信息
                    Intent intent = new Intent(getContext(), EditPersonalInfoActivity.class);
                    startActivityForResult(intent,101);
                    break;
                case R.id.img_personalinfo_portrait:  //修改头像
                    resetPortrait();
                    break;
            }
        }
    }
    private void resetPortrait() {
        Intent intent= new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10&&resultCode==getActivity().RESULT_OK&&data!=null){
            Uri uri = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            c = getActivity().getContentResolver().query(uri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imgPath = c.getString(columnIndex);
            File file = new File(imgPath);
            if(file.exists()){
                files.add(file);
            }
            OkHttpUtils.post(PATH)
                    .params("userid",user.getId())
                    .addFileParams("portimg",files)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            parse(s);
                        }
                    });
        }
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.optString("message").equals("文件上传成功")){
                user.setImgpath(imgPath);
                Glide.with(getContext()).load(imgPath).error(R.drawable.me).into(imgPortrait);
                SharedPreferences.Editor edit = sp.edit();
                user.setImgpath(imgPath);
                edit.putString("portrait",imgPath).commit();
            }else{
                ToastUtil.toast(getContext(),job.optString("message"));
            }
        } catch (JSONException e) {
            Log.e("FragmentPersonalInfo","数据解析出错");
        }
    }
}
