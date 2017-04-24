package com.gdm.musicplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdm.musicplayer.R;

public class SetNicknameActivity extends AppCompatActivity {
    private EditText edNickname;
    private String nickname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);
        edNickname= (EditText) findViewById(R.id.et_set_nickname_name);
    }
    public void setNicknameClick(View view){
        switch (view.getId()){
            case R.id.img_setnickname_back:
                finish();
                break;
            case R.id.btn_start:
                String s = edNickname.getText().toString();
                if(s!=null&&s!=""){
                    nickname=s;
                }else{
                    finish();
                }
                break;
        }
    }
}
