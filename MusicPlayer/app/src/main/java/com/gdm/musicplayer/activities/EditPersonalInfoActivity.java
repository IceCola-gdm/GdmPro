package com.gdm.musicplayer.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Response;

public class EditPersonalInfoActivity extends AppCompatActivity {
    private ImageView imgBackgroubd;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvLocation;
    private TextView tvUniversity;
    private EditText editText;
    private String backgroundPath;
    private String sex;
    private String birthday;
    private String location;
    private String university;
    private Cursor c=null;
    private String tag="";
    private int mYear;
    private int mMonth;
    private int mDay;
    private String heart="";
    private User user=null;
    private ArrayList<File> files=new ArrayList<>();
    private static final String BASEBACKGROUND="http://120.24.220.119:8080/music/image/bg/";
    private static final String BASEPORTRAIT="http://120.24.220.119:8080/music/image/port/";
    private static final String URI="http://120.24.220.119:8080/music/user/updateUser";
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        app = (MyApplication) getApplication();
        user=app.getUser();
        initView();
        initData();
    }

    private void initData() {
        Glide.with(EditPersonalInfoActivity.this).load(BASEBACKGROUND+user.getBackground()).error(R.drawable.afc).into(imgBackgroubd);
        tvSex.setText(user.getSex());
        tvBirthday.setText(user.getBirthday());
        tvLocation.setText(user.getAddress());
        tvUniversity.setText(user.getDaxue());
    }

    private void initView() {
        imgBackgroubd= (ImageView) findViewById(R.id.img_edit_personalinfo_background);
        tvSex= (TextView) findViewById(R.id.tv_edit_personalinfo_sex);
        tvBirthday= (TextView) findViewById(R.id.tv_edit_personalinfo_birthday);
        tvLocation= (TextView) findViewById(R.id.tv_edit_personalinfo_location);
        tvUniversity= (TextView) findViewById(R.id.tv_edit_personalinfo_university);
        editText= (EditText) findViewById(R.id.ed_heart);
    }

    public void MyEditClick(View view){
        switch (view.getId()){
            case R.id.img_edit_personalinfo_back:
                finish();
                break;
            case R.id.btn_edit_personalinfo_submit:
                submit();
                break;
            case R.id.rl_background:
                resetBackground();
                break;
            case R.id.rl_sex:
                tag="sex";
                Intent intent2 = new Intent(EditPersonalInfoActivity.this, EditSexActivity.class);
                startActivityForResult(intent2,200);
                break;
            case R.id.rl_birthday:
//                ToastUtil.toast(EditPersonalInfoActivity.this,"还没写");
                showDialog();
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

    private void submit() {
        heart=editText.getText().toString();
        OkHttpUtils.post(URI)
                .params("id",user.getId())
                .params("birthday",birthday)
                .params("heart",heart)
                .params("address",location)
                .params("sex",sex)
                .params("daxue",university)
                .addFileParams("background",files)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                        finish();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("---","修改个人信息请求出错");
                    }
                });
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.getString("message").equals("修改成功")){
                user.setBackground(backgroundPath);
                user.setSex(sex);
                user.setHeart(heart);
                user.setAddress(location);
                user.setBirthday(birthday);
                user.setDaxue(university);
                app.setUser(user);
            }else{
                ToastUtil.toast(EditPersonalInfoActivity.this,"修改失败");
            }
        } catch (JSONException e) {
            Log.e("EditPersonalInfo","解析数据出错");
        }
    }

    private void showDialog() {
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(EditPersonalInfoActivity.this, new MyDatePickerListener(), mYear, mMonth, mDay);
        dialog.show();
    }

    private void resetBackground() {
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
            }else if(tag.equals("background")){
                backgroundPath=c.getString(columnIndex);
                imgBackgroubd.setImageBitmap(BitmapFactory.decodeFile(backgroundPath));
                File file = new File(backgroundPath);
                if(file.exists()){
                    files.add(file);
                }
            }
            tag="";
        }else if(requestCode==200&&resultCode==RESULT_OK){
            switch (tag){
//                case "nickname":
//                    nickname=data.getStringExtra("newNickName");
//                    tvNickname.setText(nickname);
//                    break;
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

    private class MyDatePickerListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            birthday=year+"-"+(month+1)+"-"+dayOfMonth;
            tvBirthday.setText(birthday);
        }
    }
}
