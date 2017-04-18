package com.gdm.musicplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gdm.musicplayer.R;

public class PhoneLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
    }
    public void phoneloginClick(View view){
        switch (view.getId()){
            case R.id.img_phone_login_back:
                finish();
                break;
        }
    }
}
