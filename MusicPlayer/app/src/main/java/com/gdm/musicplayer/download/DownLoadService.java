package com.gdm.musicplayer.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 10789 on 2017-05-14.
 */

public class DownLoadService extends Service{
    private String TAG="downLoadService";
    private ExecutorService service;
    private DataBase db;
    private File root=new File(Environment.getExternalStorageDirectory(),"gdm");
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = Executors.newFixedThreadPool(3);
        db=DataBase.getDb(this);
        if (!root.exists()) {
            root.mkdirs();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"收到指令");
        String path = intent.getStringExtra("path");
        String name=intent.getStringExtra("name");
        Integer type=intent.getIntExtra("type",0);
        int i = db.insertData(name,type,0);
        new Thread(new DownLoadThread(path,name,0,i)).start();
        return super.onStartCommand(intent, flags, startId);
    }

    class DownLoadThread implements Runnable{
        public DownLoadThread(String path, String name, int type,int id) {
            this.path = path;
            this.name = name;
            this.type = type;
            this.id=id;
        }
        private int id;
        private String path;
        private String name;
        private int type;//0.表示音乐文件,1.表示MV文件
        @Override
        public void run() {
            Log.e(TAG,"下载开始");
            db.update(id,1);
            OkHttpUtils.get(path).execute(new FileCallback(root.getAbsolutePath(),name+".mp3") {
                @Override
                public void onSuccess(File file, Call call, Response response) {
                    Log.e(TAG,"下载完成"+file.getAbsolutePath());
                    Toast.makeText(DownLoadService.this, "下载完成", Toast.LENGTH_SHORT).show();
                    db.update(id,2);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    db.update(id,0);
                    Log.e(TAG,"下载错误"+e.getMessage());
                }
            });
        }
        public static final String ACTION="con.gdm.downloadinfo";
        private void sendDownLoadBrod(int now, int length, String name) {
            Intent intent = new Intent(ACTION);
            intent.putExtra("now",now);
            intent.putExtra("length",length);
            intent.putExtra("name",name);
            sendBroadcast(intent);
        }
    }
}
