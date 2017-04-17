package com.gdm.musicplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gdm.musicplayer.R;

/**
 * 最近播放界面
 */
public class RecentlyPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_play);
    }
    public void recentlyplayClick(View view){
        switch (view.getId()){
            case R.id.img_recentlyplay_back:
                finish();
                break;
        }
    }
}
