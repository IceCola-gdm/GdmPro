package com.gdm.musicplayer.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class ResetPwdActivity extends AppCompatActivity {
    private ImageView imgBack;
    private Button btnSubmit;
    private EditText etOldPwd;
    private EditText etNewPwd;
    private EditText etAgainPwd;
    private String oldPwd="";
    private String newPwd="";
    private String againPwd="";
    private static final String PATH="http://120.24.220.119:8080/music//user/updatePassword";
    private MyApplication app;
    private User user=null;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        app= (MyApplication) getApplication();
        user=app.getUser();
        sp=app.getSp();
        initView();
        setListener();
    }

    private void setListener() {
        imgBack.setOnClickListener(new MyListener());
        btnSubmit.setOnClickListener(new MyListener());
    }

    private void initView() {
        imgBack= (ImageView) findViewById(R.id.img_pwd_back);
        btnSubmit= (Button) findViewById(R.id.btn_submit);
        etOldPwd= (EditText) findViewById(R.id.ed_reset_pwd);
        etNewPwd= (EditText) findViewById(R.id.ed_reset_new);
        etAgainPwd= (EditText) findViewById(R.id.ed_reset_new_again);
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_pwd_back:
                    finish();
                    break;
                case R.id.btn_submit:
                    submitData();
                    break;
            }
        }
    }

    private void submitData() {
        oldPwd=etOldPwd.getText().toString();
        newPwd=etNewPwd.getText().toString();
        againPwd=etAgainPwd.getText().toString();
        if(!oldPwd.equals("")&&!newPwd.equals("")&&!againPwd.equals("")){
            if(!againPwd.equals(newPwd)){
                ToastUtil.toast(ResetPwdActivity.this,"两次输入密码不一致，请重新输入");
                etAgainPwd.setFocusable(true);
            }else{
                OkHttpUtils.post(PATH)
                        .params("id",user.getId())
                        .params("oldpwd",oldPwd)
                        .params("newpwd",newPwd)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                parse(s);
                            }
                        });
            }
        }else{
            if(oldPwd.equals("")){
                ToastUtil.toast(ResetPwdActivity.this,"密码不能为空，请重新输入");
                etOldPwd.setFocusable(true);
            }else if(newPwd.equals("")){
                ToastUtil.toast(ResetPwdActivity.this,"密码不能为空，请重新输入");
                etNewPwd.setFocusable(true);
            }else if(againPwd.equals("")){
                ToastUtil.toast(ResetPwdActivity.this,"密码不能为空，请重新输入");
                etAgainPwd.setFocusable(true);
            }
        }
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.optString("message").equals("修改成功")){
                user.setPassword(newPwd);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("password",newPwd).commit();
                finish();
            }else{
                ToastUtil.toast(ResetPwdActivity.this,job.optString("message"));
            }
        } catch (JSONException e) {
            Log.e("ResetPwdActivity","数据解析出错");
        }

    }
}
