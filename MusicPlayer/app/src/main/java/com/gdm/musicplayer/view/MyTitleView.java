package com.gdm.musicplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gdm.musicplayer.R;

/**
 * 自定义title
 * Created by Administrator on 2017/4/9 0009.
 */
public class MyTitleView extends LinearLayout {
    private int leftImgSrc;   //左边图片资源
    private int rightImgSrc;  //右边图片资源
    private String leftTextSrc;  //左边文字资源
    private String rightTextSrc; //右边文字资源
    private String midTextSrc; //中间文字资源

    private RadioGroup radioGroup;
    private RadioButton rbTextLeft;
    private RadioButton rbTextRight;
    private RadioButton rbTextMid;
    private ImageView imgLeft;
    private ImageView imgRight;
    private LayoutInflater inflater;
    private OnLeftImgClick onLeftImgClick = null;
    private OnRightImgClick onRightImgClick = null;
    private OnRadioGroupClick onRadioGroupClick = null;

    public MyTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        leftImgSrc=typedArray.getResourceId(R.styleable.TitleView_setLeftImage,0);
        rightImgSrc=typedArray.getResourceId(R.styleable.TitleView_setRightImage,0);
        leftTextSrc=typedArray.getString(R.styleable.TitleView_setLeftText);
        rightTextSrc=typedArray.getString(R.styleable.TitleView_setRightText);
        midTextSrc=typedArray.getString(R.styleable.TitleView_setMidText);
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.title, this);
        initView(view);
        initData();
        setClickListener();
    }
    /**
     * 设置左右图片的监听事件
     */
    private void setClickListener() {
        imgLeft.setOnClickListener(new MyListener());
        imgRight.setOnClickListener(new MyListener());
        radioGroup.setOnCheckedChangeListener(new MyCheckLishener());
    }

    private void initData() {
        imgLeft.setImageResource(leftImgSrc);
        imgRight.setImageResource(rightImgSrc);
        rbTextLeft.setText(leftTextSrc);
        rbTextRight.setText(rightTextSrc);
        rbTextMid.setText(midTextSrc);
    }

    private void initView(View view) {
        imgLeft= (ImageView) view.findViewById(R.id.title_img_left);
        imgRight= (ImageView) view.findViewById(R.id.title_img_right);
        radioGroup= (RadioGroup) view.findViewById(R.id.title_rg);
        rbTextLeft= (RadioButton) view.findViewById(R.id.title_text_left);
        rbTextRight= (RadioButton) view.findViewById(R.id.title_text_right);
        rbTextMid= (RadioButton) view.findViewById(R.id.title_text_mid);
    }

    /**
     * 设置左边图片
     * @param leftImgSrc
     */
    public void setLeftImgSrc(int leftImgSrc){
        imgLeft.setImageResource(leftImgSrc);
    }

    /**
     * 设置右边图片
     * @param rightImgSrc
     */
    public void setRightImgSrc(int rightImgSrc){
        imgRight.setImageResource(rightImgSrc);
    }

    /**
     * 设置左边文字
     * @param leftTextSrc
     */
    public void setLeftTextSrc(int leftTextSrc){
        rbTextLeft.setText(leftTextSrc);
    }
    /**
     * 设置右边文字
     * @param rightTextSrc
     */
    public void setRightTextSrc(int rightTextSrc){
        rbTextRight.setText(rightTextSrc);
    }/**
     * 设置中间文字
     * @param midTextSrc
     */
    public void setMidTextSrc(int midTextSrc){
        rbTextMid.setText(midTextSrc);
    }
    /**
     * 左边图片的回调接口
     */
    public interface OnLeftImgClick{
        void onClick(View v);
    }

    /**
     * 右边图片的回调接口
     */
    public interface OnRightImgClick{
        void onClick(View v);
    }
    /**
     * RadioGroup回调接口
     */
    public interface OnRadioGroupClick{
        void onCheckedChanged(RadioGroup group, int checkedId);
    }
    /**
     * 设置左边图片的监听事件
     * @param onLeftImgClick
     */
    public void setOnLeftImgClick(OnLeftImgClick onLeftImgClick){
        this.onLeftImgClick=onLeftImgClick;
    }

    /**
     * 设置右边图片的监听事件
     * @param onRightImgClick
     */
    public void setOnRightImgClick(OnRightImgClick onRightImgClick){
        this.onRightImgClick=onRightImgClick;
    }
    /**
     * 自定义RadioGroup点击事件
     * @param onRadioGroupClick
     */
    public void setOnRadioGroupClick(OnRadioGroupClick onRadioGroupClick){
        this.onRadioGroupClick=onRadioGroupClick;
    }
    /**
     * 点击事件处理
     */
    private class MyListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_img_left:
                    onLeftImgClick.onClick(v);
                    break;
                case R.id.title_img_right:
                    onRightImgClick.onClick(v);
                    break;
            }
        }
    }
    /**
     * RadioGroup点击事件监听
     */
    private class MyCheckLishener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            onRadioGroupClick.onCheckedChanged(group,checkedId);
        }
    }
    public RadioGroup getRadioGroup(){
        return radioGroup;
    }
}
