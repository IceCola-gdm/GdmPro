package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.utils.ToastUtil;

public class ChooseToLoginOrRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_to_login_or_register);
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
                ToastUtil.toast(ChooseToLoginOrRegister.this,"还没写");
                break;
            case R.id.img_login_qq_icon:
                ToastUtil.toast(ChooseToLoginOrRegister.this,"还没写");
                break;
            case R.id.img_login_weibo_icon:
                ToastUtil.toast(ChooseToLoginOrRegister.this,"还没写");
                break;
            case R.id.img_login_wangyi_icon:
                ToastUtil.toast(ChooseToLoginOrRegister.this,"还没写");
                break;

        }
    }

}
