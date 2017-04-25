package com.gdm.musicplayer.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gdm.musicplayer.R;

public class SettingGedan extends Activity {
    private AlertDialog myDialog;
    private EditText ed;
    private Button btn_submit;
    private Button btn_cancel;
    private String gedan="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_gedan_setting);
    }
    public void gedanClick(View view){
        switch (view.getId()){
            case R.id.rl_new:
                showDialog();
                break;
            case R.id.rl_manage:
                Intent intent = new Intent();
                intent.setClass(SettingGedan.this,ManageGedanActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void showDialog() {
        myDialog = new AlertDialog.Builder(SettingGedan.this).create();
        myDialog.show();
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        myDialog.getWindow().setAttributes(params);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        myDialog.getWindow().setContentView(R.layout.fragment_my_newgedan);
        ed = (EditText) myDialog.getWindow().findViewById(R.id.et_gedan_title);
        btn_submit = (Button) myDialog.getWindow().findViewById(R.id.btn_submit);
        btn_cancel = (Button) myDialog.getWindow().findViewById(R.id.btn_cancel);
        btn_submit.setOnClickListener(new MyListener());
        btn_cancel.setOnClickListener(new MyListener());
        ed.addTextChangedListener(new ChangeListener());
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_cancel:  //取消
                    myDialog.dismiss();
                    break;
                case R.id.btn_submit:  //提交
                    gedan=ed.getText().toString();
                    myDialog.dismiss();
                    break;
            }
        }
    }

    private class ChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString()!=null&&s.toString()!="") {
                btn_submit.setClickable(true);
                btn_submit.setTextColor(Color.RED);
            }else{
                btn_submit.setTextColor(Color.GRAY);
                btn_submit.setClickable(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString()!=null&&s.toString()!="") {
                btn_submit.setClickable(true);
                btn_submit.setTextColor(Color.RED);
            }else{
                btn_submit.setTextColor(Color.GRAY);
                btn_submit.setClickable(false);
            }
        }
    }
}
