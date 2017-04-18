package com.gdm.musicplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gdm.musicplayer.R;

public class EditPersonalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
    }
    public void editPersonalInfoClick(View view){
        switch (view.getId()){
            case R.id.img_edit_personalinfo_back:
                finish();
                break;
        }
    }
}
