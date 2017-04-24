package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.gdm.musicplayer.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText edAccount;
    private EditText edpwd;
    private EditText edPwdAgain;
    private String account="";
    private String pwd="";
    private String pwdAgain="";

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
                Intent intent = new Intent(RegisterActivity.this,SetNicknameActivity.class);
                startActivity(intent);
                break;
        }
    }

}
