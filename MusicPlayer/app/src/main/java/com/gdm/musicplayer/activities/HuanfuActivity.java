package com.gdm.musicplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.TuiJianGridViewAdapter;

import java.util.ArrayList;

public class HuanfuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huanfu);
    }
    public void huanfuClick(View view){
        switch (view.getId()){
            case R.id.img_huanfu_back:
                finish();
                break;
        }
    }
}
