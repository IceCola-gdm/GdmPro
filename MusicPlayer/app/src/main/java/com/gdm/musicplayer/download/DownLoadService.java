package com.gdm.musicplayer.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.transition.Visibility;
import android.util.Log;
import android.widget.Toast;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.bean.Music;
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
    private Notification notification;
    private NotificationManager manager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = Executors.newFixedThreadPool(3);
        manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        db=DataBase.getDb(this);
        if (!root.exists()) {
            root.mkdirs();
        }
    }
    private synchronized void showNotify(){
        if (notification!=null) {
            manager.cancelAll();
        }
        Notification.Builder bd = new Notification.Builder(this);
        bd.setContentTitle("音悦下载中");
        bd.setContentText("当前共有"+count+"首歌正在下载");
        bd.setVisibility(Notification.VISIBILITY_PUBLIC);
        bd.setSmallIcon(R.drawable.ic_launcher);
        notification=bd.build();
        manager.notify("download",1,notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"收到指令");
        Music m= (Music) intent.getSerializableExtra("music");
        if(m!=null){
            int i = db.insertMusic(m);
            new Thread(new DownLoadThread(m.getFileUrl(),m.getName(),0,m.getId())).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private int count=0;
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
        private int type;//0.表示音乐文件
        @Override
        public void run() {
            Log.e(TAG,"下载开始");
            count++;
            showNotify();
            db.update(name,1,path);
            OkHttpUtils.get(path).execute(new FileCallback(root.getAbsolutePath(),name+".mp3") {
                @Override
                public void onSuccess(File file, Call call, Response response) {
                    Log.e(TAG,"下载完成"+file.getAbsolutePath());
                    count--;
                    db.update(name,2,file.getAbsolutePath());
                    showNotify();
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    db.update(id,0,path);
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
