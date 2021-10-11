package com.wwg.addressnook_index;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通讯录索引View
 */
public class AddressBookIndex extends View {

    /**
     * 可设置属性
     */

    //默认文本颜色
    private int defaultTxtColor;
    //选中文本颜色
    private int selectTxtColor;
    //选中文本背景色
    private int txtSelectBackColor;
    //文本字号
    private float txtSize;
    //文本的横向内间距
    private float txtPaddingHorizontal;
    //文本的纵向内间距
    private float txtPaddingVertical;
    //是否开启选择动画
    private boolean enableAnimation;
    //要绘制的文本值
    private List<String> characters;
    //动画影响个数
    private int animationNum;
    //动画类型
    private int animationType;
    //弹窗颜色
    private int dialog_color;
    //弹窗颜色
    private int dialog_txtColor;
    //弹窗内文本大小
    private float dialog_txtSize;
    //弹窗大小
    private float dialog_size;

    /**
     * 内部封装属性
     */

    private TextPaint 文本画笔;
    private TextPaint 弹窗文本画笔;
    private Paint 文本背景画笔;
    private Paint 弹窗背景画笔;
    private Path 文本路径;
    private Path 弹窗圆形右侧尖尖路径;
    private Canvas 画布;

    //动画类型 - 仿微信弹窗
    private final int AnimationType_Dialog = 0;
    //动画类型 - 贝塞尔曲线
    private final int AnimationType_Bessel = 1;

    //交互回调
    private IndexCallBack callBack;

    //初始化完成
    private boolean isInited = false;
    //当前选中下标
    private int selectIndex = -1;
    //当前手指正在按压
    private boolean isTouching = false;
    //按压时的Y轴位置
    private int touchY = 0;
    //View高度
    private int viewHeight = 0;
    //所有文本中宽度的最大值
    private float maxTxtW = 0;
    //路径测量类
    private PathMeasure pathMeasure;
    //文本绘制点位
    private List<Map<String, Integer>> 文本点位 = new ArrayList<>();
    //贝塞尔控制点位
    private List<Map<String, Integer>> 贝塞尔点位 = new ArrayList<>();

    private static final Handler mHandler = new Handler(Looper.getMainLooper());


    public AddressBookIndex(Context context) {
        super(context);
        init(null);
    }

    public AddressBookIndex(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AddressBookIndex(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        文本画笔 = new TextPaint();
        弹窗文本画笔 = new TextPaint();
        文本背景画笔 = new Paint();
        弹窗背景画笔 = new Paint();
        文本路径 = new Path();
        弹窗圆形右侧尖尖路径 = new Path();
        pathMeasure = new PathMeasure();

        //获取XML中的属性
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AddressBookIndex);
            //获取默认文本颜色
            defaultTxtColor = typedArray.getColor(R.styleable.AddressBookIndex_defaultTxtColor, Color.parseColor("#000000"));
            //获取选中文本颜色
            selectTxtColor = typedArray.getColor(R.styleable.AddressBookIndex_selectTxtColor, Color.parseColor("#FFFFFF"));
            //获取选中文本的背景颜色
            txtSelectBackColor = typedArray.getColor(R.styleable.AddressBookIndex_txtSelectBackColor, Color.parseColor("#2B93FF"));
            //获取文本大小
            txtSize = typedArray.getDimension(R.styleable.AddressBookIndex_txtSize, sp2px(2));
            //获取文本的横向内间距
            txtPaddingHorizontal = typedArray.getDimension(R.styleable.AddressBookIndex_txtPaddingHorizontal, dp2px(2));
            //获取文本的纵向内间距
            txtPaddingVertical = typedArray.getDimension(R.styleable.AddressBookIndex_txtPaddingVertical, dp2px(2));
            //是否开启选择动画
            enableAnimation = typedArray.getBoolean(R.styleable.AddressBookIndex_enableAnimation, true);
            //动画影响个数
            animationNum = typedArray.getInteger(R.styleable.AddressBookIndex_animationNum, 4);
            if (animationNum < 0) {
                animationNum = 0;
            }
            //动画类型
            animationType = typedArray.getInteger(R.styleable.AddressBookIndex_animationType, 0);
            //弹窗颜色
            dialog_color = typedArray.getColor(R.styleable.AddressBookIndex_dialog_color, Color.parseColor("#CCCCCC"));
            //弹窗文本颜色
            dialog_txtColor = typedArray.getColor(R.styleable.AddressBookIndex_dialog_txtColor, Color.parseColor("#FFFFFF"));
            //弹窗文本字号
            dialog_txtSize = typedArray.getDimension(R.styleable.AddressBookIndex_dialog_txtSize, sp2px(26));
            //弹窗大小
            dialog_size = typedArray.getDimension(R.styleable.AddressBookIndex_dialog_size, dp2px(50));
        }

        //设置画笔颜色
        文本画笔.setColor(defaultTxtColor);
        弹窗文本画笔.setColor(dialog_txtColor);
        //设置文本大小
        文本画笔.setTextSize(txtSize);
        弹窗文本画笔.setTextSize(dialog_txtSize);
        //设置颜色防抖动
        文本画笔.setDither(true);
        弹窗文本画笔.setDither(true);
        //设置画笔的抗锯齿效果
        文本画笔.setAntiAlias(true);
        弹窗文本画笔.setAntiAlias(true);
        //设置文本居中
        文本画笔.setTextAlign(Paint.Align.CENTER);
        弹窗文本画笔.setTextAlign(Paint.Align.CENTER);

        //设置画笔颜色
        文本背景画笔.setColor(txtSelectBackColor);
        弹窗背景画笔.setColor(dialog_color);
        //设置颜色防抖动
        文本背景画笔.setDither(true);
        弹窗背景画笔.setDither(true);
        //设置画笔的抗锯齿效果
        文本背景画笔.setAntiAlias(true);
        弹窗背景画笔.setAntiAlias(true);
        //设置实心
        文本背景画笔.setStyle(Paint.Style.FILL);
        弹窗背景画笔.setStyle(Paint.Style.FILL);

    }

    /**
     * 设置交互回调
     *
     * @param callBack
     */
    public void setIndexCallBack(IndexCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (characters != null) {
            //测量每一个文本所需要的宽度,取其中最大的一个
            for (int i = 0; i < characters.size(); i++) {
                //测量绘制文本的宽度
                float txtW = 文本画笔.measureText(characters.get(i));
                maxTxtW = Math.max(maxTxtW, txtW);
            }

            viewHeight = MeasureSpec.getSize(heightMeasureSpec);

            if (isTouching) {
                if (animationType == AnimationType_Bessel) {
                    setMeasuredDimension((int) maxTxtW + getPaddingStart() + getPaddingEnd() + dp2px(30), viewHeight);
                } else {
                    setMeasuredDimension(
                            (int) maxTxtW + getPaddingStart() + getPaddingEnd()
                                    + dp2px(10)
                                    + (int) (dialog_size * 1.2)
                            , viewHeight);
                }
            } else {
                setMeasuredDimension((int) maxTxtW + getPaddingStart() + getPaddingEnd(), viewHeight);
            }

        } else {
            throw new NullPointerException("未设置索引数据!!!");
        }

    }

    /**
     * 计算手动设置字号是否超出可用高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.d("AddressBookIndex", "控件高度变化 --> " + h);

        //延迟更新UI,避免绘制出错
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //计算可用文本大小
                upDataFontSize();
            }
        }, 10);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        touchY = (int) event.getY();
        if (touchY < 0) {
            touchY = 0;
        }
        if (touchY > viewHeight) {
            touchY = viewHeight;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                isTouching = true;
                changeSelectIndex(touchY, false);
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                changeSelectIndex(touchY, true);
                break;
        }
        return true;
    }

    /**
     * 根据按压Y轴位置,改变选中下标
     *
     * @param touchY    当前按压Y轴位置
     * @param scrollRec 是否滚动列表
     */
    private void changeSelectIndex(float touchY, boolean scrollRec) {

        selectIndex = (int) touchY / (viewHeight / characters.size()) - 1;

        if (touchY % viewHeight / characters.size() != 0) {
            selectIndex++;
        }

        if (selectIndex < 0) {
            selectIndex = 0;
        }
        if (selectIndex > characters.size() - 1) {
            selectIndex = characters.size() - 1;
        }

        requestLayout();
        invalidate();

        if (callBack != null && scrollRec) {
            callBack.onSelectIndexChange(selectIndex);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        画布 = canvas;

        if (characters != null && !characters.isEmpty()) {
            //更新路径
            upDataPath();

            //绘制文本路径
//            drawDataPath();

            //绘制文本
            drawTxt();

            //绘制选中文本
            drawSelectTxt();
        }

    }

    /**
     * 获取可用文本大小
     *
     * @return
     */
    private void upDataFontSize() {
        if (characters != null) {
            if (characters.size() * (txtSize + txtPaddingVertical * 2) > viewHeight) {
                txtSize = viewHeight / characters.size() - txtPaddingVertical * 2;
                文本画笔.setTextSize(txtSize);
                maxTxtW = 0;
                requestLayout();
            }
        } else {
            throw new NullPointerException("未设置索引数据!!!");
        }
    }

    /**
     * 更新文本路径和文本绘制点
     */
    private void upDataPath() {

        文本路径.reset();

        //获取每一块的可用高度
        int txtUsableHeight = viewHeight / characters.size();
        //测量绘制文本高度
        Paint.FontMetrics fontMetrics = 文本画笔.getFontMetrics();
        float 绘制文本时基线的偏移量 = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int 文本X轴位置 = getWidth() - (int) maxTxtW / 2 - getPaddingEnd();

        if (isTouching && enableAnimation && animationNum > 0 && animationType == AnimationType_Bessel) {
            /*
            绘制贝塞尔曲线
            两段三阶贝塞尔曲线合成
             */
            //当前选中文本中心点所在的Y轴位置
            //往上取第三个文本点位,作为贝塞尔曲线的起点
            int 贝塞尔曲线点位_01_X = 文本X轴位置;
            int 贝塞尔曲线点位_01_Y = touchY - txtUsableHeight * animationNum;
            //第二个贝塞尔点位
            int 贝塞尔曲线点位_02_X = 文本X轴位置 / 10 * 9;
            int 贝塞尔曲线点位_02_Y = 贝塞尔曲线点位_01_Y + (touchY - 贝塞尔曲线点位_01_Y) / 10 * 4;
            //第三个贝塞尔点位
            int 贝塞尔曲线点位_03_X = 文本X轴位置 / 5;
            int 贝塞尔曲线点位_03_Y = 贝塞尔曲线点位_01_Y + (touchY - 贝塞尔曲线点位_01_Y) / 10 * 6;
            //第四个贝塞尔点位,为要显示的焦点位置
            int 贝塞尔曲线点位_04_X = (int) Math.max(maxTxtW + txtPaddingHorizontal, txtSize + txtPaddingVertical) / 2 + dp2px(2);
            int 贝塞尔曲线点位_04_Y = touchY;
            //第五个贝塞尔点位
            int 贝塞尔曲线点位_05_X = 文本X轴位置 / 5;
            int 贝塞尔曲线点位_05_Y = touchY + (touchY - 贝塞尔曲线点位_01_Y) / 10 * 4;
            //第六个贝塞尔点位
            int 贝塞尔曲线点位_06_X = 文本X轴位置 / 10 * 9;
            int 贝塞尔曲线点位_06_Y = touchY + (touchY - 贝塞尔曲线点位_01_Y) / 10 * 6;
            //第七个贝塞尔点位,作为贝塞尔曲线的终点
            int 贝塞尔曲线点位_07_X = 文本X轴位置;
            int 贝塞尔曲线点位_07_Y = txtUsableHeight * (selectIndex + 1 + animationNum) - txtUsableHeight / 2;

            Map<String, Integer> map_01 = new HashMap<>();
            map_01.put("贝塞尔点位X轴位置", 贝塞尔曲线点位_01_X);
            map_01.put("贝塞尔点位Y轴位置", 贝塞尔曲线点位_01_Y);

            Map<String, Integer> map_02 = new HashMap<>();
            map_02.put("贝塞尔点位X轴位置", 贝塞尔曲线点位_02_X);
            map_02.put("贝塞尔点位Y轴位置", 贝塞尔曲线点位_02_Y);

            Map<String, Integer> map_03 = new HashMap<>();
            map_03.put("贝塞尔点位X轴位置", 贝塞尔曲线点位_03_X);
            map_03.put("贝塞尔点位Y轴位置", 贝塞尔曲线点位_03_Y);

            Map<String, Integer> map_04 = new HashMap<>();
            map_04.put("贝塞尔点位X轴位置", 贝塞尔曲线点位_04_X);
            map_04.put("贝塞尔点位Y轴位置", 贝塞尔曲线点位_04_Y);

            Map<String, Integer> map_05 = new HashMap<>();
            map_05.put("贝塞尔点位X轴位置", 贝塞尔曲线点位_05_X);
            map_05.put("贝塞尔点位Y轴位置", 贝塞尔曲线点位_05_Y);

            Map<String, Integer> map_06 = new HashMap<>();
            map_06.put("贝塞尔点位X轴位置", 贝塞尔曲线点位_06_X);
            map_06.put("贝塞尔点位Y轴位置", 贝塞尔曲线点位_06_Y);

            Map<String, Integer> map_07 = new HashMap<>();
            map_07.put("贝塞尔点位X轴位置", 贝塞尔曲线点位_07_X);
            map_07.put("贝塞尔点位Y轴位置", 贝塞尔曲线点位_07_Y);

            贝塞尔点位.add(map_01);
            贝塞尔点位.add(map_02);
            贝塞尔点位.add(map_03);
            贝塞尔点位.add(map_04);
            贝塞尔点位.add(map_05);
            贝塞尔点位.add(map_06);
            贝塞尔点位.add(map_07);

            if (selectIndex > animationNum - 1 && selectIndex < characters.size() - animationNum) {
                /*
                上下范围都处于正常状态时
                 */
                //绘制默认直线
                文本路径.moveTo(文本X轴位置, 0);
                文本路径.lineTo(贝塞尔曲线点位_01_X, 贝塞尔曲线点位_01_Y);
                //第一段贝塞尔曲线
                文本路径.cubicTo(
                        贝塞尔曲线点位_02_X, 贝塞尔曲线点位_02_Y,
                        贝塞尔曲线点位_03_X, 贝塞尔曲线点位_03_Y,
                        贝塞尔曲线点位_04_X, 贝塞尔曲线点位_04_Y);
                //第二段贝塞尔曲线
                文本路径.cubicTo(
                        贝塞尔曲线点位_05_X, 贝塞尔曲线点位_05_Y,
                        贝塞尔曲线点位_06_X, 贝塞尔曲线点位_06_Y,
                        贝塞尔曲线点位_07_X, 贝塞尔曲线点位_07_Y);
                //终点
                文本路径.lineTo(文本X轴位置, viewHeight);

            } else {
                if (selectIndex > animationNum - 1) {
                    //绘制默认直线
                    文本路径.moveTo(文本X轴位置, 0);
                    文本路径.lineTo(贝塞尔曲线点位_01_X, 贝塞尔曲线点位_01_Y);
                    //第一段贝塞尔曲线
                    文本路径.cubicTo(
                            贝塞尔曲线点位_02_X, 贝塞尔曲线点位_02_Y,
                            贝塞尔曲线点位_03_X, 贝塞尔曲线点位_03_Y,
                            贝塞尔曲线点位_04_X, 贝塞尔曲线点位_04_Y);
                    //第二段贝塞尔曲线
                    文本路径.cubicTo(
                            贝塞尔曲线点位_05_X, 贝塞尔曲线点位_05_Y,
                            贝塞尔曲线点位_06_X, 贝塞尔曲线点位_06_Y,
                            贝塞尔曲线点位_07_X, 贝塞尔曲线点位_07_Y);
                } else {
                    //绘制默认直线
                    文本路径.moveTo(贝塞尔曲线点位_01_X, 贝塞尔曲线点位_01_Y);
                    //第一段贝塞尔曲线
                    文本路径.cubicTo(
                            贝塞尔曲线点位_02_X, 贝塞尔曲线点位_02_Y,
                            贝塞尔曲线点位_03_X, 贝塞尔曲线点位_03_Y,
                            贝塞尔曲线点位_04_X, 贝塞尔曲线点位_04_Y);
                    //第二段贝塞尔曲线
                    文本路径.cubicTo(
                            贝塞尔曲线点位_05_X, 贝塞尔曲线点位_05_Y,
                            贝塞尔曲线点位_06_X, 贝塞尔曲线点位_06_Y,
                            贝塞尔曲线点位_07_X, 贝塞尔曲线点位_07_Y);
                    //终点
                    文本路径.lineTo(文本X轴位置, viewHeight);
                }
            }

        } else {
            //绘制默认直线
            文本路径.moveTo(文本X轴位置, 0);
            文本路径.lineTo(文本X轴位置, viewHeight);
        }

        //关联路径
        pathMeasure.setPath(文本路径, false);

        //计算文本的绘制点
        文本点位.clear();
        for (int i = 0; i < characters.size(); i++) {

            int 文本所在块的中心点 = txtUsableHeight * (i + 1) - txtUsableHeight / 2;
            int 文本Y轴位置 = 文本所在块的中心点 + (int) 绘制文本时基线的偏移量;
            Map<String, Integer> map = new HashMap<>();

            //计算在路径上的文本X轴位置
            if (selectIndex > -1 && isTouching && animationNum > 0 && animationType == AnimationType_Bessel) {
                if (selectIndex - animationNum - 1 > i || i > selectIndex + animationNum - 1) {
                    //如果当前文本不在动画内,直接使用默认X轴位置
                    map.put("文本X轴位置", 文本X轴位置);
                } else {
                    //计算当前位置的贝塞尔速率(getBesselXForY()方法中有一行注释掉的根据Y轴位置计算速率的笨方法,最合理的是根据六元三次方程去计算)
                    float besselT = Math.abs((float) (文本Y轴位置 - touchY)) / (txtUsableHeight * animationNum) / 85 * 100;
                    //限定边界
                    if (besselT > 1) {
                        besselT = 1;
                    }
                    if (besselT < 0) {
                        besselT = 0;
                    }
                    //根据Y轴位置和速率计算X轴位置
                    map.put("文本X轴位置", getBesselXForY(besselT, 文本Y轴位置));
                }
            } else {
                map.put("文本X轴位置", 文本X轴位置);
            }

            map.put("文本Y轴位置", 文本Y轴位置);
            map.put("文本中心点Y轴位置", 文本所在块的中心点);
            文本点位.add(map);

        }

        isInited = true;

    }

    /**
     * 绘制文本路径
     */
    private void drawDataPath() {

        画布.drawColor(Color.parseColor("#FFFFC7"));

        Paint paint = new Paint();
        paint.setStrokeWidth(dp2px(1));
        paint.setColor(Color.parseColor("#2b93ff"));
        paint.setStyle(Paint.Style.STROKE);
        //设置颜色防抖动
        paint.setDither(true);
        //设置画笔的抗锯齿效果
        paint.setAntiAlias(true);

        //绘制路径
        画布.drawPath(文本路径, paint);

    }

    /**
     * 绘制文本
     */
    private void drawTxt() {

        文本画笔.setColor(defaultTxtColor);

        for (int i = 0; i < 文本点位.size(); i++) {

            Map<String, Integer> map = 文本点位.get(i);

            画布.drawText(
                    characters.get(i),
                    map.get("文本X轴位置"),
                    map.get("文本Y轴位置"),
                    文本画笔
            );

        }
    }

    /**
     * 绘制选中文本
     */
    private void drawSelectTxt() {

        if (selectIndex > -1) {

            Map<String, Integer> map = 文本点位.get(selectIndex);

            文本画笔.setColor(selectTxtColor);

            画布.drawCircle(
                    map.get("文本X轴位置"),
                    map.get("文本中心点Y轴位置"),
                    Math.max(maxTxtW + txtPaddingHorizontal, txtSize + txtPaddingVertical) / 2,
                    文本背景画笔);

            画布.drawText(
                    characters.get(selectIndex),
                    map.get("文本X轴位置"),
                    map.get("文本Y轴位置"),
                    文本画笔
            );

            //绘制弹窗
            if (isTouching && enableAnimation && animationType == AnimationType_Dialog) {

                //计算弹窗文本中心位置
                double 弹窗圆形中心点X轴位置 = dialog_size / 2;
                double 弹窗圆形中心点Y轴位置 = map.get("文本中心点Y轴位置");

                Paint.FontMetrics fontMetrics = 弹窗文本画笔.getFontMetrics();
                float 绘制文本时基线的偏移量 = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;

                int 弹窗文本X轴位置 = (int) 弹窗圆形中心点X轴位置;
                int 弹窗文本Y轴位置 = (int) (弹窗圆形中心点Y轴位置 + 绘制文本时基线的偏移量);

                //计算圆右侧尖尖的坐标
                float 上半段贝塞尔曲线起点X轴位置 = (float) (弹窗圆形中心点X轴位置 + dialog_size / 2 * Math.cos(Math.toRadians(315)));
                float 上半段贝塞尔曲线起点Y轴位置 = (float) (弹窗圆形中心点Y轴位置 + dialog_size / 2 * Math.sin(Math.toRadians(315)));
                float 上半段贝塞尔曲线终点X轴位置 = (float) (dialog_size*1.2);
                float 上半段贝塞尔曲线终点Y轴位置 = (float) (弹窗圆形中心点Y轴位置);
                float 上半段贝塞尔曲线控制点X轴位置 = 上半段贝塞尔曲线起点X轴位置 + (上半段贝塞尔曲线终点X轴位置 - 上半段贝塞尔曲线起点X轴位置)/3*2;
                float 上半段贝塞尔曲线控制点Y轴位置 = 上半段贝塞尔曲线起点Y轴位置 + (上半段贝塞尔曲线终点Y轴位置 - 上半段贝塞尔曲线起点Y轴位置)/3*2;
                float 下半段贝塞尔曲线起点X轴位置 = (float) (dialog_size*1.2);
                float 下半段贝塞尔曲线起点Y轴位置 = (float) (弹窗圆形中心点Y轴位置);
                float 下半段贝塞尔曲线终点X轴位置 = (float) (弹窗圆形中心点X轴位置 + dialog_size / 2 * Math.cos(Math.toRadians(45)));
                float 下半段贝塞尔曲线终点Y轴位置 = (float) (弹窗圆形中心点Y轴位置 + dialog_size / 2 * Math.sin(Math.toRadians(45)));
                float 下半段贝塞尔曲线控制点X轴位置 = 上半段贝塞尔曲线控制点X轴位置;
                float 下半段贝塞尔曲线控制点Y轴位置 = 下半段贝塞尔曲线起点Y轴位置 + (下半段贝塞尔曲线终点Y轴位置 - 下半段贝塞尔曲线起点Y轴位置)/3;

                弹窗圆形右侧尖尖路径.reset();
                弹窗圆形右侧尖尖路径.moveTo(上半段贝塞尔曲线起点X轴位置,上半段贝塞尔曲线起点Y轴位置);
                弹窗圆形右侧尖尖路径.quadTo(上半段贝塞尔曲线控制点X轴位置,上半段贝塞尔曲线控制点Y轴位置,上半段贝塞尔曲线终点X轴位置,上半段贝塞尔曲线终点Y轴位置);
                弹窗圆形右侧尖尖路径.quadTo(下半段贝塞尔曲线控制点X轴位置,下半段贝塞尔曲线控制点Y轴位置,下半段贝塞尔曲线终点X轴位置,下半段贝塞尔曲线终点Y轴位置);
                弹窗圆形右侧尖尖路径.close();

                画布.drawCircle(
                        (int) 弹窗圆形中心点X轴位置,
                        (int) 弹窗圆形中心点Y轴位置,
                        dialog_size / 2,
                        弹窗背景画笔);

                画布.drawText(
                        characters.get(selectIndex),
                        弹窗文本X轴位置,
                        弹窗文本Y轴位置,
                        弹窗文本画笔
                );

                画布.drawPath(弹窗圆形右侧尖尖路径,弹窗背景画笔);

            }

        }

    }

    /**
     * 设置索引数据
     *
     * @param characters
     */
    public void setData(List<String> characters) {
        this.characters = characters;

        if (animationNum * 2 > characters.size()) {
            animationNum = characters.size() / 2;
        }

        requestLayout();
        postInvalidate();
    }

    /**
     * 设置选中的下标
     *
     * @param selectIndex
     */
    public void setSelectIndex(int selectIndex) {

        this.selectIndex = selectIndex;
        postInvalidate();

    }

    /**
     * 根据Y轴位置计算贝塞尔曲线上的X轴位置
     *
     * @param besselT 当前速率
     * @param pointY  Y轴位置
     * @return 贝塞尔曲线上的X轴位置
     */
    private int getBesselXForY(float besselT, int pointY) {

        int pointX = 0;

        //获取Y轴的速率
//        float besselT = getBesselT(pointY);

        if (touchY >= pointY) {
            //上半段贝塞尔曲线时
            besselT = 1 - besselT;
            pointX = (int) (贝塞尔点位.get(0).get("贝塞尔点位X轴位置") * (1 - besselT) * (1 - besselT) * (1 - besselT)
                    + 贝塞尔点位.get(1).get("贝塞尔点位X轴位置") * 3 * besselT * (1 - besselT) * (1 - besselT)
                    + 贝塞尔点位.get(2).get("贝塞尔点位X轴位置") * 3 * (1 - besselT) * besselT * besselT
                    + 贝塞尔点位.get(3).get("贝塞尔点位X轴位置") * besselT * besselT * besselT);
        } else {
            pointX = (int) (贝塞尔点位.get(3).get("贝塞尔点位X轴位置") * (1 - besselT) * (1 - besselT) * (1 - besselT)
                    + 贝塞尔点位.get(4).get("贝塞尔点位X轴位置") * 3 * besselT * (1 - besselT) * (1 - besselT)
                    + 贝塞尔点位.get(5).get("贝塞尔点位X轴位置") * 3 * (1 - besselT) * besselT * besselT
                    + 贝塞尔点位.get(6).get("贝塞尔点位X轴位置") * besselT * besselT * besselT);
        }

        return pointX;
    }

    /**
     * 获取贝塞尔曲线上Y轴上指定位置的速率
     *
     * @param pointY Y轴上的位置
     * @return 速率
     */
    private float getBesselT(int pointY) {

        return computeBesselT(pointY, 0, 1);

    }

    /**
     * 计算贝塞尔曲线上Y轴上指定位置的速率
     *
     * @param pointY Y轴上的位置
     * @param startT 开始速率
     * @param endT   结束速率
     * @return 实际速率
     */
    private float computeBesselT(int pointY, float startT, float endT) {

        float t = startT;
        float minDistance = Float.MAX_VALUE;
        float minDistanceT = 0;
        float step = (endT - startT) / 10;
        for (int i = 0; i < 10; i++) {

            float y = 0;

            if (贝塞尔点位.get(3).get("贝塞尔点位Y轴位置") >= pointY) {
                y = 贝塞尔点位.get(0).get("贝塞尔点位Y轴位置") * (1 - t) * (1 - t) * (1 - t)
                        + 贝塞尔点位.get(1).get("贝塞尔点位Y轴位置") * 3 * t * (1 - t) * (1 - t)
                        + 贝塞尔点位.get(2).get("贝塞尔点位Y轴位置") * 3 * (1 - t) * t * t
                        + 贝塞尔点位.get(3).get("贝塞尔点位Y轴位置") * t * t * t;
            } else {
                y = 贝塞尔点位.get(3).get("贝塞尔点位Y轴位置") * (1 - t) * (1 - t) * (1 - t)
                        + 贝塞尔点位.get(4).get("贝塞尔点位Y轴位置") * 3 * t * (1 - t) * (1 - t)
                        + 贝塞尔点位.get(5).get("贝塞尔点位Y轴位置") * 3 * (1 - t) * t * t
                        + 贝塞尔点位.get(6).get("贝塞尔点位Y轴位置") * t * t * t;
            }

            float abs = Math.abs(pointY - y);
            if (abs < minDistance) {
                minDistance = abs;
                minDistanceT = t;
            }

            if (Math.abs(pointY - y) < 0.1) {
                return t;
            }
            t += step;
        }

        return computeBesselT(pointY, minDistanceT - step, minDistanceT + step);

    }

    private int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void setDefaultTxtColor(int defaultTxtColor) {
        this.defaultTxtColor = defaultTxtColor;
    }

    public void setSelectTxtColor(int selectTxtColor) {
        this.selectTxtColor = selectTxtColor;
    }

    public void setTxtSelectBackColor(int txtSelectBackColor) {
        this.txtSelectBackColor = txtSelectBackColor;
    }

    public void setTxtSize(float txtSize) {
        this.txtSize = txtSize;
    }

    public void setTxtPaddingHorizontal(float txtPaddingHorizontal) {
        this.txtPaddingHorizontal = txtPaddingHorizontal;
    }

    public void setTxtPaddingVertical(float txtPaddingVertical) {
        this.txtPaddingVertical = txtPaddingVertical;
    }

    public void setEnableAnimation(boolean enableAnimation) {
        this.enableAnimation = enableAnimation;
    }

    public void setAnimationNum(int animationNum) {
        this.animationNum = animationNum;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
    }

    public void setDialog_color(int dialog_color) {
        this.dialog_color = dialog_color;
    }

    public void setDialog_txtColor(int dialog_txtColor) {
        this.dialog_txtColor = dialog_txtColor;
    }

    public void setDialog_txtSize(float dialog_txtSize) {
        this.dialog_txtSize = dialog_txtSize;
    }

    public void setDialog_size(float dialog_size) {
        this.dialog_size = dialog_size;
    }

}
