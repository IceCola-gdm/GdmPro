package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gdm.musicplayer.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.bean.UserInfro;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

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
            case R.id.btn_reg:
                account=edAccount.getText().toString();
                pwd=edpwd.getText().toString();
                pwdAgain=edPwdAgain.getText().toString();
                if(account!=null&&pwd!=null&&pwdAgain!=null){
                    if(pwd.equals(pwdAgain)){
                        OkHttpUtils.post(PATH)
                                .params("username",account)
                                .params("password",pwd)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        parse(s);
                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        Log.e("Register",e.getMessage());
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
                break;
        }
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            String message = job.getString("message");
            if(message.equals("注册成功")){
                JSONObject data = job.getJSONObject("data");
                String username = data.getString("username");
                String password = data.getString("password");
                int id = data.getInt("id");
                User u = new User();
                u.setUsername(username);
                u.setPassword(password);
                u.setId(id);
                UserInfro.setUser(u);
                MyApplication.isLogin=true;
                Intent intent = new Intent(RegisterActivity.this,SetNicknameActivity.class);
                startActivity(intent);
                finish();
            }else{
                ToastUtil.toast(RegisterActivity.this,message);
            }
        } catch (JSONException e) {
            Log.e(TAG,"注册数据解析出错");
        }
    }

}
