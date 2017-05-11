package com.gdm.musicplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.gdm.musicplayer.R;

/**
 * Created by Administrator on 2017/4/13 0013.
 *
 */
public class CircleImageView extends ImageView {
    private int circleWidth;  //外圆的宽
    private int circleColor= Color.WHITE;  //外圆的颜色
    private Paint paint;  //画笔
    private Bitmap bitmap;  //图片资源
    private int viewWidth;  //view的宽
    private int viewHeight; //view的高
    public CircleImageView(Context context) {
        super(context,null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        paint=new Paint();
        initAttrs(context,attrs,defStyleAttr);
    }

    /**
     * 初始化资源
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array=null;
        if(array!=null) {
            array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            for (int i = 0; i < array.length(); i++) {
                int attr = array.getIndex(i);
                if (attr == R.styleable.CircleImageView_circleWidth) {
                    this.circleWidth = (int) array.getDimension(attr, 5);
                } else if (attr == R.styleable.CircleImageView_circleColor) {
                    this.circleColor = array.getColor(attr, Color.WHITE);
                }
            }
        }

        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=measureWidth(widthMeasureSpec);
        int height=measureWidth(heightMeasureSpec);
        viewWidth=width-circleWidth*2;
        viewHeight=height-circleWidth*2;
        setMeasuredDimension(viewWidth,viewHeight);
    }

    private int measureWidth(int widthMeasureSpec) {
        int result=0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            result=viewWidth;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        paint=new Paint();
        paint.setColor(circleColor);
        paint.setAntiAlias(true);
        loadImg();
        if(bitmap!=null){
            int min = Math.min(viewHeight, viewWidth);
            int circleCenter=min/2;
            bitmap=Bitmap.createScaledBitmap(bitmap,min,min,false);
            canvas.drawCircle(circleCenter+circleWidth,circleCenter+circleWidth,circleCenter+circleColor,paint);
            canvas.drawBitmap(creatCircleBitmap(bitmap,min),circleWidth,circleWidth,null);
        }

    }

    private Bitmap creatCircleBitmap(Bitmap img, int min) {
        Bitmap bitmap=null;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        bitmap=Bitmap.createBitmap(min,min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(min/2,min/2,min/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(img,0,0,paint);
        return bitmap;
    }

    /**
     * 加载图片
     */
    private void loadImg() {
        Drawable drawable = this.getDrawable();
        if(drawable!=null){
//            bitmap=((GlideBitmapDrawable)drawable).getBitmap();
        }
    }
    public void setCircleWidth(int width){
        this.circleWidth=width;
        this.invalidate();
    }
}
