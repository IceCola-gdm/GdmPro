package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.utils.ToastUtil;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG="RegisterActivity";
    private EditText edAccount;
    private EditText edpwd;
    private EditText edPwdAgain;
    private String account="";
    private String pwd="";
    private String pwdAgain="";
    private static final String PATH="http://120.24.220.119:8080/music/user/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        edAccount= (EditText) findViewById(R.id.ed_phone_register_number);
        edpwd= (EditText) findViewById(R.id.ed_phone_register_pwd);
        edPwdAgain= (EditText) findViewById(R.id.ed_phone_register_yanzheng);
    }

    public void registerClick(View view){
        switch (view.getId()){
            case R.id.img_phone_register_back:
                finish();
                break;
            case R.id.btn_register:
                account=edAccount.getText().toString();
                pwd=edpwd.getText().toString();
                pwdAgain=edPwdAgain.getText().toString();
                if(account!=null&&pwd!=null&&pwdAgain!=null){
                    if(pwd.equals(pwdAgain)){
                        OkHttpUtils.post()
                                .url(PATH)
                                .addParams("username",account)
                                .addParams("password",pwd)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        Log.e(TAG,e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        Log.e(TAG,response);
                                    }
                                });
                    }else{
                        ToastUtil.toast(RegisterActivity.this,"两次输入密码不一致，请重新输入");
                        edPwdAgain.setFocusable(true);
                    }
                }else if(account==null){
                    ToastUtil.toast(RegisterActivity.this,"输入信息不能为空");
                    edAccount.setFocusable(true);
                }else if(pwd==null){
                    ToastUtil.toast(RegisterActivity.this,"输入信息不能为空");
                    edpwd.setFocusable(true);
                }else if(pwdAgain==null){
                    ToastUtil.toast(RegisterActivity.this,"输入信息不能为空");
                    edPwdAgain.setFocusable(true);
                }
                Intent intent = new Intent(RegisterActivity.this,SetNicknameActivity.class);
                startActivity(intent);
                break;
        }
    }

}
