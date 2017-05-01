package com.gdm.musicplayer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

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
            case R.id.btn_register:
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
//                Intent intent = new Intent(RegisterActivity.this,SetNicknameActivity.class);
//                startActivity(intent);
                break;
        }
    }

}
