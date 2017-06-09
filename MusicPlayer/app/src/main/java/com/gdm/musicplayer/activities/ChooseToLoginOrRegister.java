package com.gdm.musicplayer.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

public class ChooseToLoginOrRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_to_login_or_register);
        ShareSDK.initSDK(ChooseToLoginOrRegister.this,"1e2b04e15eab0");
    }
    public void chooseClick(View view){
        switch (view.getId()){
            case R.id.btn_phone_login:
                Intent intent = new Intent(ChooseToLoginOrRegister.this, PhoneLoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_register:
                Intent intent2 = new Intent(ChooseToLoginOrRegister.this, RegisterActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_try:
                finish();
                break;
            case R.id.img_login_weixin_icon:
                Platform zone = ShareSDK.getPlatform(QZone.NAME);
                plogin(zone);
                break;
            case R.id.img_login_qq_icon:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                plogin(qq);
                break;
            case R.id.img_login_weibo_icon:
                Platform twb = ShareSDK.getPlatform(TencentWeibo.NAME);
                plogin(twb);
                break;
//            case R.id.img_login_wangyi_icon:
//                Platform swb = ShareSDK.getPlatform(SinaWeibo.NAME);
//                plogin(swb);
//                break;

        }
    }

    /**
     * 新浪微博
     */
    private void sWlogin() {
        Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
        sina.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //输出所有授权信息
                String s = arg0.getDb().exportData();
                Log.e("---",s);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {

            }
        });
        sina.showUser(null);//授权并获取用户信息
        //移除授权
        sina.removeAccount(true);
    }
    private void plogin(Platform p){
        p.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                arg2.printStackTrace();
            }
            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //输出所有授权信息
                String s = arg0.getDb().exportData();
                try {
                    JSONObject js=new JSONObject(s);
                    String nickname = js.getString("nickname");
                    String icon = js.getString("icon");
                    User u = new User();
                    u.setUsername(nickname);
                    u.setNickname(nickname);
                    u.setId(-1);
                    u.setImgpath(icon);
                    MyApplication ap = (MyApplication) getApplication();
                    ap.setUser(u);
                    ap.setLogin(true);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancel(Platform arg0, int arg1) {

            }
        });
        p.showUser(null);//授权并获取用户信息
        //移除授权
        p.removeAccount(true);
    }
}
