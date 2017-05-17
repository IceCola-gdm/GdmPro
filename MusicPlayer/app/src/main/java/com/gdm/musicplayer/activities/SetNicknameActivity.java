package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.bean.UserInfro;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SetNicknameActivity extends AppCompatActivity {
    private static final String TAG="SetNicknameActivity";
    private EditText edNickname;
    private String nickname="";
    private static final String PATH="http://120.24.220.119:8080/music/user/updateUser";
    private List<File> files=new ArrayList<>();   //背景图片文件
    private String img= Environment.getExternalStorageDirectory()+File.separator+"a.jpg";
    private MyApplication app;
    private User user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);
        app= (MyApplication) getApplication();
        user=app.getUser();
        edNickname= (EditText) findViewById(R.id.et_set_nickname_name);
    }
    public void setNicknameClick(View view){
        switch (view.getId()){
            case R.id.img_setnickname_back:
//                Intent intent = new Intent(SetNicknameActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.btn_start:
                String s = edNickname.getText().toString();
                if(s!=null&&s!=""){
                    nickname=s;
                    postNickName(nickname);
//                    Intent intent1 = new Intent(SetNicknameActivity.this, MainActivity.class);
//                    startActivity(intent1);
                    finish();
                }else{
                    ToastUtil.toast(SetNicknameActivity.this,"输入信息不能为空");
                    edNickname.setFocusable(true);
                }
                break;
        }
    }

    private void postNickName(String nickname) {
        files.add(new File(img));
        OkHttpUtils.post(PATH)
                .params("id", ((MyApplication)getApplication()).getUser().getId())
                .params("nickname",nickname)
                .addFileParams("background",files)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        ToastUtil.toast(SetNicknameActivity.this,"设置昵称出错");
                    }
                });
    }

    private void parse(String s) {
        Log.e("SetNickName",s);
        try {
            JSONObject job = new JSONObject(s.trim());
            String message = job.getString("message");
            if(message.equals("修改成功")){
                JSONObject data = job.getJSONObject("data");
                String name = data.getString("nickname");
                user.setNickname(name);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                ToastUtil.toast(SetNicknameActivity.this,message);
            }
        } catch (JSONException e) {
            Log.e(TAG,"设置用户昵称，数据解析出错");
        }
    }
}
