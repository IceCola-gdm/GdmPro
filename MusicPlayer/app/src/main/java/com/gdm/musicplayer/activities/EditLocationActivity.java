package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.gdm.musicplayer.R;

public class EditLocationActivity extends AppCompatActivity {
    private EditText editText;
    private String location="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        editText= (EditText) findViewById(R.id.et_edit_nickname_name);
    }
    public void editLocationClick(View view){
        switch (view.getId()){
            case R.id.img_edit_location_back:
                finish();
                break;
            case R.id.tv_edit_location_save:
                returnLocation();
                break;
            case R.id.img_edit_location_cancel:
                editText.setText("");
                break;
        }
    }

    private void returnLocation() {
        location=editText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("newLocation",location);
        setResult(RESULT_OK,intent);
        finish();
    }
}
