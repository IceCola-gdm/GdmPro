package com.gdm.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.R;
import com.gdm.musicplayer.activities.ManageGedanActivity;
import com.gdm.musicplayer.bean.MList;
import com.gdm.musicplayer.bean.MusicList;
import com.gdm.musicplayer.bean.User;
import com.gdm.musicplayer.utils.ToastUtil;
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
 * Created by Administrator on 2017/4/11 0011.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int HEAD=0;
    private static int CONTENT=1;
    private static int BOTTOM=2;
    private MusicListAdapter adt;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MusicList> beens;
    private ArrayList<MusicList> content;
    private AlertDialog myDialog;
    private EditText ed;
    private Button btn_submit;
    private Button btn_cancel;
    private String gedan;
    private AlertDialog dialog;
    private MyApplication app;
    private User user=null;
    private ArrayList<MList> lists;
    private ArrayList<File> files=new ArrayList<>();

    public MyRecyclerViewAdapter(Context context, ArrayList<MusicList> beens,ArrayList<MusicList> content,MyApplication app) {
        this.context = context;
        this.beens = beens;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.content=content;
        this.app=app;
        user=app.getUser();
        lists=new ArrayList<>();
        adt=new MusicListAdapter(lists,context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        RecyclerView.ViewHolder holder=null;
        switch (viewType){
            case 0:
                view=inflater.inflate(R.layout.fragment_my_recycler_head2,parent,false);
                holder=new HolderOne(view);
                break;
            case 1:
                view=inflater.inflate(R.layout.fragment_my_recycler_content,parent,false);
                holder=new HolderTwo(view);

                break;
            case 2:
                view=inflater.inflate(R.layout.fragment_my_recycler_bottom,parent,false);
                holder=new HolderThree(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HolderOne) {
            //加载头部的逻辑
            final MusicList bean = beens.get(position);
            final HolderOne h= (HolderOne) holder;
            h.textViewTitle.setText(bean.getTitle());
            h.textViewCount.setText("("+bean.getNum()+")");
            Glide.with(context).load(bean.getImgPath()).into(h.imageView);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, bean.getmClass());
                    intent.putExtra("data",beens.get(position).getM());
                    context.startActivity(intent);
                }
            });
        }else if (holder instanceof HolderTwo){
            final HolderTwo h= (HolderTwo) holder;
            h.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (h.contentparent.getVisibility()==View.VISIBLE) {
                        h.imageViewArrow.setImageResource(R.drawable.right);
                        h.contentparent.setVisibility(View.GONE);
                    }else{
                        h.imageViewArrow.setImageResource(R.drawable.down);
                        h.contentparent.setVisibility(View.VISIBLE);
                    }
                }
            });
            h.imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //创建歌单
                    if(!app.isLogin()){
                        ToastUtil.toast(context,"还没有登录哟");
                    }else{
                        show();
                    }
                }
            });
        }
    }

    private void show() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_my_gedan_setting, null);
        dialog.getWindow().setContentView(view);
        RelativeLayout rl_new= (RelativeLayout) view.findViewById(R.id.rl_new);
        RelativeLayout rl_manage= (RelativeLayout) view.findViewById(R.id.rl_manage);
        rl_new.setOnClickListener(new MyListener());
        rl_manage.setOnClickListener(new MyListener());
    }

    @Override
    public int getItemCount() {
        return beens.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==beens.size()) {
            return CONTENT;
        }else {
            return HEAD;
        }
    }

    private class HolderOne extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;
        public TextView textViewCount;
        public HolderOne(View view) {
            super(view);
            imageView= (ImageView) view.findViewById(R.id.img_head_my);
            textViewTitle= (TextView) view.findViewById(R.id.tv_head_my);
            textViewCount= (TextView) view.findViewById(R.id.tv_head_my_count);
        }
    }

    private class HolderTwo extends RecyclerView.ViewHolder {
        public ImageView imageViewArrow;
        public ImageView imageViewEdit;
        public TextView textViewTitle;
        public TextView textViewCount;
        public RecyclerView content;
        public RelativeLayout title,contentparent;
        public HolderTwo(View view) {
            super(view);
            title= (RelativeLayout) view.findViewById(R.id.title);
            imageViewArrow= (ImageView) view.findViewById(R.id.img_content_arrow);
            textViewTitle= (TextView) view.findViewById(R.id.tv_content_title);
            textViewCount= (TextView) view.findViewById(R.id.tv_content_count);
            imageViewEdit= (ImageView) view.findViewById(R.id.img_content_setting);
            content= (RecyclerView) view.findViewById(R.id.fragment_my_recycler_content);
            content.setAdapter(adt);
            content.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
            contentparent= (RelativeLayout) view.findViewById(R.id.contentparent);
            selectGedan();
        }
    }

    private void selectGedan() {
        OkHttpUtils.post("http://120.24.220.119:8080/music/music/getTypeList")
                .params("userid",user.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject js = new JSONObject(s);
                            String message = js.getString("message");
                            lists.clear();
                            if (message.equals("查询成功")) {
                                JSONArray data = js.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    int id = obj.getInt("id");
                                    int userid = obj.getInt("userid");
                                    String name = obj.getString("name");
                                    String discription = obj.getString("discription");
                                    String imgpath = obj.getString("imgpath");
                                    MList list = new MList();
                                    list.setImgpath(imgpath);
                                    list.setName(name);
                                    list.setDiscription(discription);
                                    list.setUserid(userid);
                                    list.setId(id);
                                    lists.add(list);
                                }
                                adt.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private class HolderThree extends RecyclerView.ViewHolder {
        public ImageView imageViewIcon;
        public ImageView imageViewSetting;
        public TextView textViewTitle;
        public TextView textViewCount;
        public HolderThree(View view) {
            super(view);
            imageViewIcon= (ImageView) view.findViewById(R.id.img_content_icon);
            textViewTitle= (TextView) view.findViewById(R.id.tv_bottom_gedanname);
            textViewCount= (TextView) view.findViewById(R.id.tv_bottom_yinyuecount);
            imageViewSetting= (ImageView) view.findViewById(R.id.img_gedanbianji);
        }
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_new:
                    showDialog();
                    dialog.dismiss();
                    break;
                case R.id.rl_manage:
                    Intent intent = new Intent();
                    intent.setClass(context,ManageGedanActivity.class);
                    context.startActivity(intent);
                    dialog.dismiss();
                    break;
                case R.id.btn_submit:
                    gedan=ed.getText().toString();
                    createNewGeDan();
                    myDialog.dismiss();
                    break;
                case R.id.btn_cancel:
                    myDialog.dismiss();
                    break;
            }
        }
    }

    private void createNewGeDan() {
        OkHttpUtils.post(MyApplication.BASEPATH+"/music/addmusiclist")
                .params("id",user.getId())
                .params("name",gedan)
                .params("discription","")
                .params("type",3)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject js = new JSONObject(s);
                            String message = js.getString("message");
                            if (message.equals("新增成功")) {

                                selectGedan();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void showDialog() {
        myDialog = new AlertDialog.Builder(context).create();
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

    private class ChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

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
