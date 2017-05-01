package com.gdm.musicplayer.activities;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class PhoneLoginActivity extends AppCompatActivity {
    private static final String TAG="PhoneLoginActivity";
    private ImageView imgBack;
    private Button btnLogin;
    private EditText edAccount;
    private EditText edPwd;
    private String account="";
    private String pwd="";
    private SharedPreferences sp=null;
    private String url="http://120.24.220.119:8080/music/user/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        sp=getSharedPreferences("myaccount",MODE_PRIVATE);
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
                        OkHttpUtils.post(url)
                                .params("username",account)
                                .params("password",pwd)
                                .execute(new StringCallback() {

                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {

                                    }
                                });
                    }
                    break;
            }
        }
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
