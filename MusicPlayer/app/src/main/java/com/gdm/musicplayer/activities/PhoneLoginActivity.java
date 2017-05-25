package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class PhoneLoginActivity extends AppCompatActivity {
    private static final String TAG="PhoneLoginActivity";
    private ImageView imgBack;
    private ImageView portrait;
    private Button btnLogin;
    private EditText edAccount;
    private EditText edPwd;
    private String account="";
    private String pwd="";
    private SharedPreferences sp=null;
    private MyApplication app;
    private static final String BASEPORTRAIT="http://120.24.220.119:8080/music/image/port/";
    private String url="http://120.24.220.119:8080/music/user/login";
    private static final String PATH3="http://120.24.220.119:8080/music/music/getTypeList";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        app= (MyApplication) getApplication();
        sp=app.getSp();
        initView();
        setListener();
    }

    private void getInitData() {
        account=sp.getString("account","");
        pwd=sp.getString("pwd","");
        if(account!=""&&pwd!=""){
            edAccount.setText(account);
            edPwd.setText(pwd);
        }
        Glide.with(PhoneLoginActivity.this).load(BASEPORTRAIT+sp.getString("portrait","")).error(R.drawable.pp).into(portrait);
    }

    private void setListener() {
        imgBack.setOnClickListener(new MyListener());
        btnLogin.setOnClickListener(new MyListener());
    }

    private void initView() {
        imgBack= (ImageView) findViewById(R.id.img_phone_login_back);
        btnLogin= (Button) findViewById(R.id.btn_phone_login);
        edAccount= (EditText) findViewById(R.id.ed_phone_login_number);
        edPwd= (EditText) findViewById(R.id.ed_phone_login_pwd);
        portrait= (ImageView) findViewById(R.id.portrait);
        getInitData();
    }

    public void phoneloginClick(View view){
        switch (view.getId()){
            case R.id.img_phone_login_back:
                finish();
                break;
        }
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_phone_login_back:
                    finish();
                    break;
                case R.id.btn_phone_login:
                    account=edAccount.getText().toString();
                    pwd=edPwd.getText().toString();
                    if(account==null||pwd==null){
                        ToastUtil.toast(PhoneLoginActivity.this,"输入信息不能为空");
                        if(account==null){
                            edAccount.setFocusable(true);
                        }else if(pwd==null){
                            edPwd.setFocusable(true);
                        }
                    }else{
                        login();
                    }
                    break;
            }
        }
    }

    private void login() {
        OkHttpUtils.post(url)
                .params("username",account)
                .params("password",pwd)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                        OkHttpUtils.post(PATH3)
                                .params("userid",user.getId())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        parse2(s);
                                    }
                                });
                    }
                });
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            String message = job.getString("message");
            if(message.equals("登录成功")){
                user = new User();
                JSONObject data = job.getJSONObject("data");
                user.setId(data.optInt("id"));
                user.setNickname(data.optString("nickname"));
                user.setPassword(data.optString("password"));
                user.setAddress(data.optString("address"));
                user.setBirthday(data.optString("birthday"));
                user.setDaxue(data.optString("daxue"));
                user.setHeart(data.optString("heart"));
                user.setSex(data.optString("sex"));
                user.setUsername(data.optString("username"));
                user.setBackground(data.optString("background"));
                user.setImgpath(data.optString("imgpath"));
                app.setUser(user);
                app.setLogin(true);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("account",account).commit();
                editor.putString("pwd",pwd).commit();
                editor.putString("portrait",user.getImgpath()).commit();
                Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                ToastUtil.toast(PhoneLoginActivity.this,message);
            }
        } catch (JSONException e) {
            Log.e("PhoneLoginActivity","数据解析出错");
        }
    }
    private void parse2(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            JSONArray array = job.optJSONArray("data");
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                if(obj.optString("name").equals("我的收藏")){
                    MyApplication.cid=obj.optInt("id");
                }
                if(obj.optString("name").equals("我喜欢的")){
                    MyApplication.lid=obj.optInt("id");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
