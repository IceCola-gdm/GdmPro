package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gdm.musicplayer.R;

public class EditNicknameActivity extends AppCompatActivity {
    private String nickname="";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
        editText= (EditText) findViewById(R.id.et_edit_nickname_name);
    }
    public void editNicknameClick(View view){
        switch (view.getId()){
            case R.id.img_edit_nickname_back:
                finish();
                break;
            case R.id.tv_edit_nickname_save:
                nickname=editText.getText().toString();
                returnNickname();
                break;
            case R.id.img_edit_nickname_cancel:
                editText.setText("");
                break;
        }
    }

    private void returnNickname() {
        Intent intent = new Intent();
        intent.putExtra("newNickName",nickname);
        setResult(RESULT_OK,intent);
        finish();
    }

}
