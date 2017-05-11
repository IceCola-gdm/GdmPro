package com.gdm.musicplayer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.YYGGeDanListViewAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

public class SearchOnlineActivity extends AppCompatActivity {
    private LinearLayout ll_in;
    private LinearLayout ll_out;
    private RelativeLayout rl;
    private EditText editText;
    private TextView tv_count;
    private ListView listView;
    private YYGGeDanListViewAdapter adapter=null;
    private ImageView imageView;
    private ImageView search;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;

    private ArrayList<Music> musics=new ArrayList<>();
    private Music music=null;
    private String name="";
    private String baseMusicPath="http://120.24.220.119:8080/music/data/music/";
    private String baseMusicImgPath="http://120.24.220.119:8080/music/data/music/img/";
    private String baseLrcPath="http://120.24.220.119:8080/music/data/music/lrc/";
    private String baseMvPath="http://120.24.220.119:8080/music/data/mv/";
    private static final String PATH="http://120.24.220.119:8080/music/music/search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_online);
        initView();
        setListener();
    }

    private void setListener() {
        imageView.setOnClickListener(new MyListener());
        editText.addTextChangedListener(new MyTextListener());
        search.setOnClickListener(new MyListener());
    }

    private void initView() {
        imageView= (ImageView) findViewById(R.id.img_search_back);
        search= (ImageView) findViewById(R.id.search);
        editText= (EditText) findViewById(R.id.ed_search);
        ll_in= (LinearLayout) findViewById(R.id.ll_in);
        rl= (RelativeLayout) findViewById(R.id.rl_all3);
        tv_count= (TextView) findViewById(R.id.tv_count);
        listView= (ListView) findViewById(R.id.search_listview);
        ll_out= (LinearLayout) findViewById(R.id.ll_out);
        tv1= (TextView) findViewById(R.id.tv1);
        tv2= (TextView) findViewById(R.id.tv2);
        tv3= (TextView) findViewById(R.id.tv3);
        tv4= (TextView) findViewById(R.id.tv4);
        tv5= (TextView) findViewById(R.id.tv5);
        tv6= (TextView) findViewById(R.id.tv6);
        tv7= (TextView) findViewById(R.id.tv7);
        tv8= (TextView) findViewById(R.id.tv8);
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.search:
                    ll_out.setVisibility(View.INVISIBLE);
                    ll_in.setVisibility(View.VISIBLE);
                    if(name!=null&&name!=""){
                        search();
                    }
                    name="";
                    break;
                case R.id.img_search_back:
                    finish();
                    break;
            }
        }
    }

    private void search() {
        OkHttpUtils.post(PATH)
                .params("keyword",name)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                    }
                });
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.optString("message").equals("搜索成功")){
                JSONArray data = job.optJSONArray("data");
                for(int i=0;i<data.length();i++){
                    JSONObject obj = data.optJSONObject(i);
                    music=new Music();
                    music.setName(obj.optString("name"));
                    music.setId(obj.optInt("musicid"));
                    if(obj.optString("path")!=null&&obj.optString("path")!=""){
                        music.setFileUrl(baseMusicPath+obj.optString("path"));
                    }
                    music.setSinger(obj.optString("author"));
                    music.setAlbum(obj.optString("album"));
                    music.setSize(obj.optString("size"));
                    if(obj.optString("imgpath")!=null&&obj.optString("imgpath")!=""){
                        music.setImgPath(baseMusicImgPath+obj.optString("imgpath"));
                    }
                    if(obj.optString("mvpath")!=null&&obj.optString("mvpath")!=""){
                        music.setMvPath(baseMvPath+obj.optString("mvpath"));
                    }
                    if(obj.optString("lrcfile")!=null&&obj.optString("lrcfile")!=""){
                        music.setLrc(baseLrcPath+obj.optString("lrcfile"));
                    }
                    musics.add(music);
                }
                adapter=new YYGGeDanListViewAdapter(SearchOnlineActivity.this,musics);
                listView.setAdapter(adapter);
                tv_count.setText("共("+musics.size()+")首");
                listView.setOnItemClickListener(new MyItemListener());
            }else{
                ToastUtil.toast(SearchOnlineActivity.this,"搜索失败");
            }
        } catch (JSONException e) {
            Log.e("SearchOnlineActivity","数据解析出错");
        }
    }

    private class MyTextListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().equals("")){
                ll_out.setVisibility(View.VISIBLE);
                ll_in.setVisibility(View.INVISIBLE);
                name="";
            }else{
                name=s.toString();
            }
        }
    }
    public void flowClick(View view){
        editText.setText("");
        switch (view.getId()){
            case R.id.tv1:
                name=tv1.getText().toString();
                break;
            case R.id.tv2:
                name=tv2.getText().toString();
                break;
            case R.id.tv3:
                name=tv3.getText().toString();
                break;
            case R.id.tv4:
                name=tv4.getText().toString();
                break;
            case R.id.tv5:
                name=tv5.getText().toString();
                break;
            case R.id.tv6:
                name=tv6.getText().toString();
                break;
            case R.id.tv7:
                name=tv7.getText().toString();
                break;
            case R.id.tv8:
                name=tv8.getText().toString();
                break;
        }
        editText.setText(name);
    }

    private class MyItemListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent1 = new Intent(MyService.mAction);
            intent1.putExtra("cmd","chose_pos");
            intent1.putExtra("pos",position);
            intent1.putExtra("data",musics);
            sendBroadcast(intent1);

            Intent intent = new Intent(SearchOnlineActivity.this, PlayActivity.class);
            intent.putExtra("data",musics);
            intent.putExtra("position",position);
            intent.putExtra("state","play");
            startActivity(intent);
        }
    }
}
