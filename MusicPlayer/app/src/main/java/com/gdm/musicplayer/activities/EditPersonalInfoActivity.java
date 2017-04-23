package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.utils.ToastUtil;

public class EditPersonalInfoActivity extends AppCompatActivity {
    private ImageView imgPortrait;
    private ImageView imgBackgroubd;
    private TextView tvSex;
    private TextView tvNickname;
    private TextView tvBirthday;
    private TextView tvLocation;
    private TextView tvUniversity;

    private String imgPath;
    private String backgroundPath;
    private String sex;
    private String nickname;
    private String birthday;
    private String location;
    private String university;
    private Cursor c=null;
    private String tag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        initView();
    }

    private void initView() {
        imgPortrait= (ImageView) findViewById(R.id.img_edit_personalinfo_touxiang);
        imgBackgroubd= (ImageView) findViewById(R.id.img_edit_personalinfo_background);
        tvNickname= (TextView) findViewById(R.id.tv_edit_personalinfo_nickname);
        tvSex= (TextView) findViewById(R.id.tv_edit_personalinfo_sex);
        tvBirthday= (TextView) findViewById(R.id.tv_edit_personalinfo_birthday);
        tvLocation= (TextView) findViewById(R.id.tv_edit_personalinfo_location);
        tvUniversity= (TextView) findViewById(R.id.tv_edit_personalinfo_university);
    }

    public void MyEditClick(View view){
        switch (view.getId()){
            case R.id.img_edit_personalinfo_back:
                finish();
                break;
            case R.id.btn_edit_personalinfo_submit:
                ToastUtil.toast(EditPersonalInfoActivity.this,"还没写");
                break;
            case R.id.rl_portrait:
                resetPortrait();
                break;
            case R.id.rl_background:
                resetBackground();
                break;
            case R.id.rl_nickname:
                tag="nickname";
                Intent intent = new Intent(EditPersonalInfoActivity.this, EditNicknameActivity.class);
                startActivityForResult(intent,200);
                break;
            case R.id.rl_sex:
                tag="sex";
                Intent intent2 = new Intent(EditPersonalInfoActivity.this, EditSexActivity.class);
                startActivityForResult(intent2,200);
                break;
            case R.id.rl_birthday:
                ToastUtil.toast(EditPersonalInfoActivity.this,"还没写");
                break;
            case R.id.rl_location:
                tag="location";
                Intent intent3 = new Intent(EditPersonalInfoActivity.this, EditLocationActivity.class);
                startActivityForResult(intent3,200);
                break;
            case R.id.rl_university:
                tag="university";
                Intent intent4 = new Intent(EditPersonalInfoActivity.this, EditUniversityActivity.class);
                startActivityForResult(intent4,200);
                break;
        }
    }
    private void resetBackground() {
        tag="portrait";
        Intent intent= new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,100);
    }

    private void resetPortrait() {
        tag="background";
        Intent intent= new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK&&data!=null){
            Uri uri = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            c = getContentResolver().query(uri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            if(tag.equals("portrait")){
                imgPath = c.getString(columnIndex);
                imgPortrait.setImageBitmap(BitmapFactory.decodeFile(imgPath));
            }else if(tag.equals("background")){
                backgroundPath=c.getString(columnIndex);
                imgBackgroubd.setImageBitmap(BitmapFactory.decodeFile(backgroundPath));
            }
            tag="";
        }else if(requestCode==200&&resultCode==RESULT_OK){
            switch (tag){
                case "nickname":
                    nickname=data.getStringExtra("newNickName");
                    tvNickname.setText(nickname);
                    break;
                case "sex":
                    sex=data.getStringExtra("newSex");
                    tvSex.setText(sex);
                    break;
                case "location":
                    location=data.getStringExtra("newLocation");
                    tvLocation.setText(location);
                    break;
                case "university":
                    university=data.getStringExtra("newUniversity");
                    tvUniversity.setText(university);
                    break;
            }
            tag="";
        }
    }
}
