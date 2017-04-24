package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.gdm.musicplayer.R;

public class EditSexActivity extends AppCompatActivity {
    private ImageView imgMan;
    private ImageView imgWoman;
    private String sex="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sex);
        imgMan= (ImageView) findViewById(R.id.iv_man);
        imgWoman= (ImageView) findViewById(R.id.iv_woman);
    }
    public void editSexClick(View view){
        switch (view.getId()){
            case R.id.img_edit_nickname_back:
                finish();
                break;
            case R.id.tv_edit_sex_save:
                returnSex();
                break;
            case R.id.rl_sex_man:
                imgWoman.setVisibility(View.INVISIBLE);
                imgMan.setVisibility(View.VISIBLE);
                sex="男";
                break;
            case R.id.rl_sex_woman:
                imgMan.setVisibility(View.INVISIBLE);
                imgWoman.setVisibility(View.VISIBLE);
                sex="女";
                break;
        }
    }

    private void returnSex() {
        Intent intent = new Intent();
        intent.putExtra("newSex",sex);
        setResult(RESULT_OK,intent);
        finish();
    }
}
