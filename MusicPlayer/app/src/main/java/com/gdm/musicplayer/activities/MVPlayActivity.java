package com.gdm.musicplayer.activities;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.gdm.musicplayer.R;
import com.gdm.musicplayer.application.MyApplication;
import com.gdm.musicplayer.bean.MV;
import com.gdm.musicplayer.bean.ScreenBean;
import com.gdm.musicplayer.utils.LocUtil;

import java.util.ArrayList;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class MVPlayActivity extends Activity {

    /** 当前视频路径 */
    private String path;
    private int current=-1;
    private ArrayList<MV> mvs=new ArrayList<>();
    /** 当前声音 */
    private int mVolume = -1;
    /** 最大音量 */
    private int mMaxVolume;
    /** 当前亮度 */
    private float mBrightness = -1f;
    /** 手势数目 */
    private int finNum = 0;
    /** 当前进度 */
    private long mProgress = 0;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private VideoView mVideoView;
    private GestureDetector gestDetector;
    private ScaleGestureDetector scaleDetector;
    private ScreenBean screenBean;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
//        if (!LibsChecker.checkVitamioLibs(this))
//            return;
        current=getIntent().getIntExtra("pos",-1);
        ArrayList<MV> m= (ArrayList<MV>) getIntent().getSerializableExtra("data");
        mvs.addAll(m);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.acy_play);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mMaxVolume = LocUtil.getMaxVolume(this);
        gestDetector = new GestureDetector(this, new SingleGestureListener());
        scaleDetector = new ScaleGestureDetector(this, new MultiGestureListener());
        screenBean = LocUtil.getScreenPix(this);
        init();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                current++;
                if(current!=mvs.size()){
                    init();
                }
            }
        });
    }

    private void init() {
        path=MyApplication.BASEMVPATH+mvs.get(current).getUrl();
        if (path.equals("")) {
            return;
        }else{
            mVideoView.setVideoPath(path);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
            });
        }
    }

    /** 定时隐藏 */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finNum = event.getPointerCount();
        if (1 == finNum) {
            gestDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    endGesture();
            }
        } else if (2 == finNum) {
            scaleDetector.onTouchEvent(event);
        }
        return true;
    }

    /** 手势结束 */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
        finNum = 0;
        mProgress = -2;
        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    /**
     * 视频缩放
     */
    public void changeLayout(int size) {
        mVideoView.setVideoLayout(size, 0);
    }

    /**
     * 声音大小
     * @param percent
     */
    public void changeVolume(float percent) {
        if (mVolume == -1) {
            mVolume = LocUtil.getCurVolume(this);
            if (mVolume < 0)
                mVolume = 0;
            // 显示
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        LocUtil.setCurVolume(this, index);

        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 亮度大小
     * @param percent
     */
    public void changeBrightness(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
            // 显示
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 播放进度
     * @param percent
     */
    public void changePregress(float percent) {
        if (mProgress < -1) {
            mVideoView.pause();
            mProgress = mVideoView.getCurrentPosition();
        }
        if (Math.abs(percent) > 0.1) {
            percent = (float) (percent / Math.abs(percent) * 0.1);
        }
        long index = (long) (percent * mVideoView.getDuration()) + mProgress;
        if (index > mVideoView.getDuration()) {
            index = mVideoView.getDuration();
        } else if (index < 0) {
            index = 0;
        }
        mVideoView.seekTo(index);
    }

    /**
     * 单点触屏
     * @author jin
     */
    private class SingleGestureListener implements
            android.view.GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (e1.getX() - e2.getX() > 100) {
                changePregress(-0.2f);
            } else if (e1.getX() - e2.getX() < -100) {
                changePregress(0.2f);
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (2 == finNum) {
                return false;
            }
            float moldX = e1.getX();
            float moldY = e1.getY();
            float y = e2.getY();
            float X = e2.getX();
            Log.d("onf", distanceX);
            if (moldX > screenBean.getsWidth() * 9.0 / 10)// 右边滑动
                changeVolume((moldY - y) / screenBean.getsHeight());
            else if (moldX < screenBean.getsWidth() / 10.0)// 左边滑动
                changeBrightness((moldY - y) / screenBean.getsHeight());
            else
                changePregress((X - moldX) / screenBean.getsWidth());
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }

    /**
     * 多点缩放
     * @author jin
     *
     */
    private class MultiGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 返回true ，才能进入onscale()函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            float oldDis = detector.getPreviousSpan();
            float curDis = detector.getCurrentSpan();
            if (oldDis - curDis > 50) {
                // 缩小
                changeLayout(0);
                Toast.makeText(MVPlayActivity.this, "缩小",Toast.LENGTH_SHORT).show();
            } else if (oldDis - curDis < -50) {
                // 放大
                changeLayout(1);
                Toast.makeText(MVPlayActivity.this, "放大", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
