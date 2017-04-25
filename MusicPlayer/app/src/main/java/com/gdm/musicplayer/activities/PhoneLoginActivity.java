package com.gdm.musicplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdm.musicplayer.R;

public class PhoneLoginActivity extends AppCompatActivity {
    private ImageView imgBack;
    private Button btnLogin;
    private EditText edAccount;
    private EditText edPwd;
    private String account="";
    private String pwd="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        initView();
        setListener();
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
                    break;
            }
        }
    }
}
