package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gdm.musicplayer.R;

public class EditUniversityActivity extends AppCompatActivity {
    private EditText editText;
    private String university="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_university);
        editText= (EditText) findViewById(R.id.et_edit_university_name);
    }
    public void editUniversityClick(View view){
        switch (view.getId()){
            case R.id.img_edit_university_icon:
                finish();
                break;
            case R.id.tv_edit_university_save:
                returnUniversity();
                break;
            case R.id.img_edit_university_cancel:
                editText.setText("");
                break;
        }

    }

    private void returnUniversity() {
        university=editText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("newUniversity",university);
        setResult(RESULT_OK,intent);
        finish();
    }
}
