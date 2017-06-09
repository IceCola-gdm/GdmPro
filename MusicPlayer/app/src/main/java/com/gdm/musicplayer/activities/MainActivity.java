package com.gdm.musicplayer.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.adapter.MenuAdapter;
import com.gdm.musicplayer.bean.Music;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.fragments.FragmentMain;
import com.gdm.musicplayer.service.MyService;
import com.gdm.musicplayer.utils.ToastUtil;
import com.gdm.musicplayer.view.MySlidingPanelLayout;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity{
    private MySlidingPanelLayout mSlidingPaneLayout;
    private ImageView imgPortrait;   //头像
    private ImageView imgSex;
    private ImageView imgbackground;
    private TextView tvHeart;
    private TextView tvNickname;
    private TextView tvBirthday;
    private TextView tvLocation;
    private TextView tvUniversity;
    private TextView tvLogin;    //登录注册
    private RelativeLayout rl_info;
    private FrameLayout rl_first;
    private User user=null;
    private FragmentMain fragmentMain;
    private Fragment lastFragment=null;
    private ArrayList<Music> musics=new ArrayList<>();
    private ImageView imgPlay;
    private MenuAdapter adapter=null;
    private ImageView imgPor;
    private TextView tvSong;
    private TextView tvSinger;
    public static String state="stop";  //播放状态
    private String title;
    private int pos=0;
    private MyPlayStateReceiver receiver;
    private ImageView imgType;
    private TextView tvType;
    private TextView tvCount;
    private int type=0;
    private MyApplication ap;
    private SharedPreferences sp;
    private int selectH=0;
    private int selectM=0;
    private int ms;   //定时时间
    private static final String BASEPORTRAIT="http://120.24.220.119:8080/music/image/port/";
    private static final String PATH="http://120.24.220.119:8080/music/music/getAllMusic";
    private static final String BASEBACKGROUND="http://120.24.220.119:8080/music/image/bg/";
    private static final String PATH2="http://120.24.220.119:8080/music/user/uploadPort";
    private Music music=null;
    private Cursor c;
    private String imgPath;
    private ArrayList<File> files=new ArrayList<>();
    private AlertDialog dialog;
    private RecyclerView recyclerView;
    private ProgressDialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new MyPlayStateReceiver();
        IntentFilter filter = new IntentFilter(MyService.PLAY_ACTION);
        filter.addAction(MyApplication.CHANGELIST);
        registerReceiver(receiver,filter);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        getData();
        ap= (MyApplication) getApplication();
        sp=ap.getSp();
        user=ap.getUser();
        ap.setMusics(musics);
        initView();
        initData();
    }

    private void getData() {
        dialog2=new ProgressDialog(MainActivity.this);
        dialog2.setMessage("玩命儿加载中，请稍后...");
        dialog2.show();
        OkHttpUtils.get(PATH)
                .params("pageNum",1)
                .params("pageSize",20)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parse(s);
                        dialog2.dismiss();
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dialog.dismiss();
                        ToastUtil.toast(MainActivity.this,e.getMessage());
                    }
                });
    }

    private void parse(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.optString("message").equals("查询成功")){
                JSONArray data = job.optJSONArray("data");
                for(int i=0;i<data.length();i++){
                    JSONObject obj = data.optJSONObject(i);
                    music=new Music();
                    music.setId(obj.optInt("musicid"));
                    music.setName(obj.optString("name"));
                    if(obj.optString("path")!=null&&obj.optString("path")!=""){
                        music.setFileUrl(MyApplication.BASEMUSICPATH+obj.optString("path"));
                    }
                    music.setSinger(obj.optString("author"));
                    music.setAlbum(obj.optString("album"));
                    music.setSize(obj.optString("size"));
                    if(obj.optString("imgpath")!=null&&obj.optString("imgpath")!=""){
                        music.setImgPath(MyApplication.BASEMUSICIIMGPATH+obj.optString("imgpath"));
                    }
                    if(obj.optString("mvpath")!=null&&obj.optString("mvpath")!=""){
                        music.setMvPath(MyApplication.BASEMVPATH+obj.optString("mvpath"));
                    }
                    if(obj.optString("lrcfile")!=null&&obj.optString("lrcfile")!=""){
                        music.setLrc(MyApplication.BASELRCPATH+obj.optString("lrcfile"));
                    }
                    musics.add(music);
                }
            }else{
                ToastUtil.toast(MainActivity.this,job.optString("message"));
            }
        } catch (JSONException e) {
            Log.e("FragmnetYYGGeDan","数据解析出错");
        }
    }

    private void initData() {
        fragmentMain = new FragmentMain();
        showFragment1(fragmentMain);
        fragmentMain.setOnImgListener(new MyListener());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lastFragment==null||lastFragment==fragmentMain) {
            if (fragmentMain.isAdded()) {
                getSupportFragmentManager().beginTransaction().show(fragmentMain).commitAllowingStateLoss();
            }
        }else{
            if (lastFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().show(lastFragment).commitAllowingStateLoss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("---",ap.isLogin()+"");
        getSupportFragmentManager().beginTransaction().show(fragmentMain).commit();
        if(ap.isLogin()) {
            user=ap.getUser();
            if (user != null) {
                tvLogin.setVisibility(View.INVISIBLE);
                rl_info.setVisibility(View.VISIBLE);
                rl_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, EditPersonalInfoActivity.class);
                        startActivityForResult(intent,101);
                    }
                });
                if (user.getNickname() != null && user.getNickname() != "") {
                    tvNickname.setText(user.getNickname());
                }
                if (user.getHeart() != null && user.getHeart() != "") {
                    tvHeart.setText(user.getHeart());
                }
                if (user.getDaxue() != null && user.getDaxue()!= "") {
                    tvUniversity.setText(user.getDaxue());
                }
                if (user.getBirthday()!= null && user.getBirthday()!= "") {
                    tvBirthday.setText(user.getBirthday());
                }
                if (user.getAddress()!= null && user.getAddress()!= "") {
                    tvLocation.setText(user.getAddress());
                }
                if (user.getSex()!= null && user.getSex()!= "") {
                    if(user.getSex().equals("男")){
                        imgSex.setImageResource(R.drawable.ab1);
                    }else if(user.getSex().equals("女")){
                        imgSex.setImageResource(R.drawable.ab2);
                    }
                }
                if (user.getId()==-1) {
                    Glide.with(MainActivity.this).load(user.getImgpath()).error(R.drawable.me).into(imgPortrait);
                }else{
                    Glide.with(MainActivity.this).load(BASEPORTRAIT+user.getImgpath()).error(R.drawable.me).into(imgPortrait);
                }
               // Glide.with(MainActivity.this).load(user.getBackground()).error(R.drawable.afc).into(imgbackground);
            }
        }

    }
    private void showFragment1(FragmentMain mainFragment) {
        if (mainFragment.isVisible()) {
            return;
        }
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        if (lastFragment==null) {

        }else{
            t.hide(lastFragment).commit();
        }
        lastFragment=mainFragment;
        if(mainFragment.isAdded()){
            t.show(mainFragment);
        }else{
            t.add(R.id.main_container,mainFragment);
        }
        t.commit();
    }
    private void closeFragment(Fragment mainFragment) {
        android.support.v4.app.FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        if(mainFragment.isAdded()) {
            ft.hide(mainFragment);
            ft.commit();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mSlidingPaneLayout= (MySlidingPanelLayout) findViewById(R.id.sliding);
        imgPortrait= (ImageView) findViewById(R.id.img_main_portrait);
        imgSex= (ImageView) findViewById(R.id.img_sex);
        tvNickname= (TextView) findViewById(R.id.tv_nickname);
        tvHeart= (TextView) findViewById(R.id.tv_heart);
        tvBirthday= (TextView) findViewById(R.id.tv_birthday);
        tvLocation= (TextView) findViewById(R.id.tv_loc);
        tvUniversity= (TextView) findViewById(R.id.tv_univer);
        tvLogin= (TextView) findViewById(R.id.tv_main_login);
        imgPlay= (ImageView) findViewById(R.id.rb_song_playicon);
        imgPor= (ImageView) findViewById(R.id.img_song_cover);
        tvSong= (TextView) findViewById(R.id.tv_songname);
        tvSinger= (TextView) findViewById(R.id.tv_singer);
        rl_info= (RelativeLayout) findViewById(R.id.rl_info);
        rl_first= (FrameLayout) findViewById(R.id.rl_first);
        imgbackground= (ImageView) findViewById(R.id.img_b);
    }

    private class MyListener implements FragmentMain.OnImgListener {

        @Override
        public void leftImgCallback() {
            if(mSlidingPaneLayout.isOpen()){
                mSlidingPaneLayout.closePane();
            }else{
                mSlidingPaneLayout.openPane();
            }
        }

        @Override
        public void rightImgCallback() {
            ToastUtil.toast(MainActivity.this,"还没写");
        }
    }

    /**
     * 处理SlidingPanelLayout里面的点击事件
     * @param view
     */
    public void clickInMain(View view){
        if(lastFragment!=null){
            closeFragment(lastFragment);
        }
        switch (view.getId()){
            case R.id.img_main_portrait:  //头像
                if(ap.isLogin()){
//                    showFragment(menus.get(0));
                    resetPortrait();
                }
                break;
            case R.id.tv_main_login:  //登录、注册
                if(!ap.isLogin()){
                    Intent intent = new Intent(MainActivity.this, ChooseToLoginOrRegister.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_change:  //切换账号
                Intent intent2 = new Intent(MainActivity.this, ChooseToLoginOrRegister.class);
                startActivity(intent2);
                break;
            case R.id.main_rl_exit:  //退出
                finish();
                break;
            case R.id.main_rl_pwd:
                if(ap.isLogin()){
                    Intent intent3 = new Intent(MainActivity.this, ResetPwdActivity.class);
                    startActivity(intent3);
                }else{
                    mSlidingPaneLayout.closePane();
                    ToastUtil.toast(MainActivity.this,"您还没有登录哟，请先登录");
                }
                break;

        }
        mSlidingPaneLayout.closePane();
    }
    private void resetPortrait() {
        Intent intent= new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,10);
    }

    @Override
    public void onBackPressed() {
        if (fragmentMain.isAdded()&&fragmentMain.isVisible()) {
            finish();
        }else{
            showFragment1(fragmentMain);
        }
    }
    public void myClick(View view){
        switch (view.getId()){
            case R.id.rl_play_icon:
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("data",musics);
                intent.putExtra("position",pos);
                intent.putExtra("state",state);
                startActivity(intent);
                break;
            case R.id.rb_song_playicon:
                Intent intent1 = new Intent(MyService.mAction);
                if(!MyApplication.playFlag){
                    intent1.putExtra("cmd","chose_pos");
                    intent1.putExtra("data",musics);
                    intent1.putExtra("pos",0);
                    intent1.putExtra("flag",1);
                    MyApplication.playFlag=true;
                    sendBroadcast(intent1);
                }else{
                    if(state.equals("play")){
                        imgPlay.setImageResource(R.drawable.play);
                        state="stop";
                    }else{
                        imgPlay.setImageResource(R.drawable.stop);
                        state="play";
                    }
                    intent1.putExtra("cmd","play");
                    sendBroadcast(intent1);
                }

                break;
            case R.id.img_song_list:
                show();
                break;
        }
    }
    private void show() {
        dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.activity_menu);
        recyclerView = (RecyclerView) dialog.getWindow().findViewById(R.id.mListView_menu);
        imgType = (ImageView) dialog.getWindow().findViewById(R.id.img_mode);
        tvType= (TextView) dialog.getWindow().findViewById(R.id.tv_mode);
        tvCount= (TextView) dialog.getWindow().findViewById(R.id.tv_account);
        RelativeLayout rlDelete= (RelativeLayout) dialog.getWindow().findViewById(R.id.rl_delete);
        if(type==MyService.LIST_PLAY){  //顺序播放
            imgType.setImageResource(R.drawable.sx2);
            tvType.setText("顺序播放");
        }else if(type==MyService.LIST_RECYCLE){  //列表循环
            imgType.setImageResource(R.drawable.xh2);
            tvType.setText("列表循环");
        }else if(type==MyService.ONE_MUSIC){    //单曲
            imgType.setImageResource(R.drawable.dq2);
            tvType.setText("单曲循环");
        }else if(type==MyService.RANDOM_PLAY){  //随机
            imgType.setImageResource(R.drawable.sj2);
            tvType.setText("随机播放");
        }
        tvCount.setText("("+musics.size()+"首)");
        adapter=new MenuAdapter(musics,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter.setListener(new MyItemClickListener());
        imgType.setOnClickListener(new MyClickListener());

    }
    private class MyPlayStateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case MyService.PLAY_ACTION:
                    state=intent.getStringExtra("state");
                    pos = intent.getIntExtra("pos", 0);
                    if(state!=null){
                        if (state.equals("play")) {
                            imgPlay.setImageResource(R.drawable.stop);
                        }else {
                            imgPlay.setImageResource(R.drawable.play);
                        }
                    }
                    title = intent.getStringExtra("title");
                    ArrayList<Music> m= (ArrayList<Music>) intent.getSerializableExtra("data");
                    tvSinger.setText(intent.getStringExtra("author"));
                    tvSong.setText(title);
                    if(m!=null){
                        musics.clear();
                        musics.addAll(m);
                        if (adapter!=null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case MyApplication.CHANGELIST:
//                    MyApplication ap= (MyApplication) getApplication();
//                    musics=ap.getMusics();
//                    adapter.notifyDataSetChanged();
                    break;
                case "userinfochange":
                    if(ap.isLogin()) {
                        if (user != null) {
                            tvLogin.setVisibility(View.INVISIBLE);
                            rl_info.setVisibility(View.VISIBLE);
                            if (user.getNickname() != null && user.getNickname() != "") {
                                tvNickname.setText(user.getNickname());
                            }
                            if (user.getHeart() != null && user.getHeart() != "") {
                                tvHeart.setText(user.getHeart());
                            }
                            if (user.getBirthday()!= null && user.getBirthday() != "") {
                                tvBirthday.setText(user.getBirthday());
                            }
                            if (user.getAddress()!= null && user.getAddress()!= "") {
                                tvLocation.setText(user.getAddress());
                            }
                            if (user.getDaxue()!= null && user.getDaxue()!= "") {
                                tvUniversity.setText(user.getDaxue());
                            }
                            if(user.getSex()!=null&&user.getSex()!=""){
                                if(user.getSex().equals("男")){
                                    imgSex.setImageResource(R.drawable.ab1);
                                }else if(user.getSex().equals("女")){
                                    imgSex.setImageResource(R.drawable.ab2);
                                }
                            }
                            Glide.with(MainActivity.this).load(BASEPORTRAIT+user.getImgpath()).error(R.drawable.me).into(imgPortrait);
                        }
                    }
                    break;
            }
        }
    }

    private class MyItemClickListener implements MenuAdapter.OnMyItemClickListener {
        @Override
        public void itemClick(int pos) {
            Intent intent = new Intent(MyService.mAction);
            intent.putExtra("cmd","chose_pos");
            intent.putExtra("pos",pos);
            intent.putExtra("data",musics);
            sendBroadcast(intent);
            dialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        ap.setLogin(false);
    }
    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_mode:
                    sendBroadcastWithType();
                    break;
                case R.id.rl_delete:
                    recyclerView.removeAllViews();
                    musics.clear();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    private void sendBroadcastWithType() {
        Intent intent = new Intent(MyService.mAction);
        if(type==MyService.LIST_PLAY){  //顺序播放
            type=MyService.LIST_RECYCLE;
            imgType.setImageResource(R.drawable.xh2);
            tvType.setText("列表循环");
        }else if(type==MyService.LIST_RECYCLE){  //列表循环
            type=MyService.ONE_MUSIC;
            imgType.setImageResource(R.drawable.dq2);
            tvType.setText("单曲循环");
        }else if(type==MyService.ONE_MUSIC){    //单曲
            type=MyService.RANDOM_PLAY;
            imgType.setImageResource(R.drawable.sj2);
            tvType.setText("随机播放");
        }else if(type==MyService.RANDOM_PLAY){  //随机
            type=MyService.LIST_PLAY;
            imgType.setImageResource(R.drawable.sx2);
            tvType.setText("顺序播放");
        }
        intent.putExtra("cmd","type");
        intent.putExtra("type",type);
        sendBroadcast(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10&&resultCode==RESULT_OK&&data!=null){
            Uri uri = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            c = getContentResolver().query(uri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imgPath = c.getString(columnIndex);
            File file = new File(imgPath);
            if(file.exists()){
                files.add(file);
            }
            OkHttpUtils.post(PATH2)
                    .params("userid",user.getId())
                    .addFileParams("portimg",files)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            parse2(s);
                        }
                    });
        }else if(requestCode == 101 && resultCode == RESULT_OK && data != null){
            String newBackground = data.getStringExtra("newBackground");
            User user = (User) data.getSerializableExtra("user");
            Log.e("--user:",user.toString());
            tvUniversity.setText(user.getDaxue());
            tvLocation.setText(user.getAddress());
            tvHeart.setText(user.getHeart());
            tvBirthday.setText(user.getBirthday());
            if(user.getSex().equals("男")){
                imgSex.setImageResource(R.drawable.ab1);
            }else if(user.getSex().equals("女")){
                imgSex.setImageResource(R.drawable.ab2);
            }
            Glide.with(MainActivity.this).load(BASEBACKGROUND+newBackground).asBitmap().error(R.drawable.afc).into(imgbackground);
        }
    }

    /**
     * 修改头像
     * @param s
     */
    private void parse2(String s) {
        try {
            JSONObject job = new JSONObject(s.trim());
            if(job.optString("message").equals("文件上传成功")){
                JSONObject data = job.optJSONObject("data");
                String imgpath = data.optString("imgpath");
                user.setImgpath(imgPath);
                Glide.with(MainActivity.this).load(BASEPORTRAIT+imgPath).error(R.drawable.pp).into(imgPortrait);
                SharedPreferences.Editor edit = sp.edit();
                user.setImgpath(imgPath);
                edit.putString("portrait",imgPath).commit();
            }else{
                ToastUtil.toast(MainActivity.this,job.optString("message"));
            }
        } catch (JSONException e) {
            Log.e("FragmentPersonalInfo","数据解析出错");
        }
    }
}
