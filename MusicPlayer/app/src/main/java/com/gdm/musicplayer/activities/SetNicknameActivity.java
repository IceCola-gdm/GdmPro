package com.gdm.musicplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gdm.musicplayer.R;

public class SetNicknameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);
    }
    public void setNicknameClick(View view){
        switch (view.getId()){
            case R.id.img_setnickname_back:
                finish();
                break;
        }
    }
}
