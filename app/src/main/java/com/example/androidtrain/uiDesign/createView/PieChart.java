package com.example.androidtrain.uiDesign.createView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.androidtrain.R;

/**
 * Created by lizz on 2018/8/24.
 */

public class PieChart extends View {

    int mWidth;
    int mHeight;

    boolean mShowText;
    int mTextPos;

    Paint mTextPaint;
    Paint mPiePaint;
    Paint mShadowPaint;

    int mTextColor = 0xff252525;
    float mTextHeight = 0.0f;
    float mTextWidth = 0.0f;

    Shader mShader;
    Path mPath;

    //动画效果
    private ValueAnimator valueAnimator;
    private float animateValue = 0.0f;

    /**
     * 自定义属性，需要在项目中添加资源，放置于res/values/attrs.xml文件中。
     */
    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //直接从AttributeSet取值，无法确认其类型。（例，字符串或Int）
        //通常通过obtainStyledAttributes()来获取属性值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.PieChart, 0, 0
        );

        try {
            mShowText = typedArray.getBoolean(R.styleable.PieChart_showText, false);
            mTextPos = typedArray.getInteger(R.styleable.PieChart_labelPosition, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            typedArray.recycle();
        }

        init();
        initAnimation();
    }

    //初始化操作，耗资源，需要在onDraw方法前执行，否则会造成卡顿
    private void init(){
        // 构建Paint时直接加上去锯齿属性
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0){
            mTextHeight = mTextPaint.getTextSize();
        }else {
            mTextPaint.setTextSize(mTextHeight);
        }
        mTextPaint.setStyle(Paint.Style.STROKE);

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);
        mPiePaint.setTextSize(mTextHeight);
        mPiePaint.setTextSize(3);

        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xff436EEE);
        //设置绘制时图片边缘效果，可以有模糊和浮雕；
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));


        //cx,cy表示中心点位置
        mShader = new SweepGradient(
                150.0f, 75.0f,
                new int[]{
                        0xffFF1493,
                        0xffFFFF00,
                        0xff00FF00,
                        0xff4876FF
                },
                new float[]{
                        0,
                        (float) (360 - 270) / 360.0f,
                        (float) (360 - 90) / 360.0f,
                        1.0f
                }
        );
        //直线
//        mPath = getPath1();

        //图形
//        mPath = getPath2();

        mPath = drawSin();
    }

    /**
     * 如果你的view不需要特别的控制它的大小，唯一需要重写的方法是onSizeChanged()).
     * onSizeChanged()，当你的view第一次被赋予一个大小时，或者你的view大小被更改时会被执行
     *
     * 当你的view被设置大小时，layout manager(布局管理器)假定这个大小包括所有的view
     * 的内边距(padding)。当你计算你的view大小时，你必须处理内边距的值
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        //Account for the label
        if (mShowText) xpad += mTextWidth;

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        // Figure out how big we can make the pie
        float diameter = Math.min(ww, hh);
    }

    //
    // Measurement functions. This example uses a simple heuristic: it assumes that
    // the pie chart should be at least as wide as its label.
    //


    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) mTextWidth;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextWidth * 2;
    }



    /**
     * 精确控制View宽高
     * @return
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        // Try fo r a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = (w - (int) mTextWidth) + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minh);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //使用此项设置，会使画布变小，部分图形无法显示
//        setMeasuredDimension(w, h);
    }


    /**
     * 绘制
     * 绘制文字使用drawText()。指定字体通过调用setTypeface(), 通过setColor()来设置文字颜色.
     * 绘制基本图形使用drawRect(), drawOval(), drawArc(). 通过setStyle()来指定形状是否需要filled, outlined.
     * 绘制一些复杂的图形，使用Path类. 通过给Path对象添加直线与曲线, 然后使用drawPath()来绘制图形. 和基本图形一样，paths也可以通过setStyle来设置是outlined, filled, both.
     * 通过创建LinearGradient对象来定义渐变。调用setShader()来使用LinearGradient。
     * 通过使用drawBitmap来绘制图片.
     *
     * 绘制什么，由Canvas处理
     * 如何绘制，由Paint处理
     * 简单来说：Canvas定义你在屏幕上画的图形，而Paint定义颜色，样式，字体，
     * @return
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//移动整个坐标系
//        canvas.translate(500,500);

        //Draw the shadow
        RectF rectF = new RectF(0, 0, 100, 140);
        canvas.drawOval(rectF, mShadowPaint);

        // Draw the label text
        canvas.drawText("自定义组件", 100, 200, mTextPaint);

        // Draw the pie slices
        RectF mBounds = new RectF(100, 0, 200, 150);
        mPiePaint.setShader(mShader);
        //角度0为x轴正方向，角度增加，为顺时针增加着色
        canvas.drawArc(mBounds, 0, 270, true, mPiePaint);
        canvas.drawText("start", 200, 75, mTextPaint);
        canvas.drawText("end", 150, mTextPaint.getTextSize(), mTextPaint);

        // Draw the pointer
        canvas.drawLine(200,0, 400, 400, mTextPaint);
        canvas.drawLine(200,0,200,400, mTextPaint);
        canvas.drawLine(200,400,400,400, mTextPaint);
        canvas.drawText("(400,400)", 400, 400, mTextPaint);

        canvas.drawCircle(400,200,50, mTextPaint);

//        canvas.translate(mWidth / 2, mHeight / 2);  // 移动坐标系到屏幕中心
//        canvas.scale(1,-1);                         // <-- 注意 翻转y坐标轴

        canvas.drawPath(mPath, mPiePaint);

    }

    

    public boolean isShowText(){
        return mShowText;
    }

    /**
     * 当view的某些内容发生变化的时候，需要调用invalidate来通知系统对这个view进行redraw，
     * 当某些元素变化会引起组件大小变化时，需要调用requestLayout方法。
     * @param showText
     */
    public void setShowText(boolean showText){
       mShowText = showText;
       invalidate();
       requestLayout();
    }

    //设置直线路径
    public Path getPath1() {
        Path path = new Path();
        path.lineTo(200,200);
        path.moveTo(200,100);//将绘制点移动至此点,并以此为起点
        path.lineTo(200,0);
        path.setLastPoint(300,0);//将上个点替换成这个点。相关线条随之改变
        path.lineTo(300,100);
        path.close();//封闭路径。close的作用是封闭路径，与连接当前最后一个点和第一个点并不等价。如果连接了最后一个点和第一个点仍然无法形成封闭图形，则close什么 也不做。

        return path;
    }

    //绘制图形
    public Path getPath2(){
        Path path = new Path();
        Path src = new Path();
        //Path.Direction.CCW逆时针|Path.Direction.CW顺时针。(left,top)点为起始点。
        path.addRect(-200,-200,200,200, Path.Direction.CW);
        src.addCircle(0,0,100, Path.Direction.CW);

        path.addPath(src, 0, 150);

        Path arc = new Path();
        arc.lineTo(100, 100);
        RectF oval = new RectF(0,0,300,300);
//        arc.addArc(oval, 0, 270);
        //false 表示不移动最后一个点，而是连接。
        arc.arcTo(oval, 0, 270, false);
        path.addPath(arc);

        return path;
    }

    //绘制波浪线，正弦函数
    public Path drawSin(){
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight = dm.heightPixels;
        float A = 40.0f, w = 0.008f, K = 0.0f;
        float fi = animateValue;
        Path path = new Path();
        float originalHeight = (float)(2 * screenHeight / 3);
        path.moveTo(0, originalHeight);

        float y;
        for (float x = 0; x <= screenWidth; x += 1){
            y = (float) (A * Math.sin(w * x + fi) + K);
            path.lineTo(x, originalHeight - y);
        }
        path.lineTo(screenWidth, screenHeight);
        path.lineTo(0, screenHeight);
        path.close();

        return path;
    }

    //根据改变fi值，来实现动画
    private void initAnimation(){
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight = dm.heightPixels;
        valueAnimator = ValueAnimator.ofFloat(0, screenWidth);
        valueAnimator.setDuration(screenWidth * 1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animateValue = (float)animation.getAnimatedValue();
                mPath = drawSin();
                /**
                 * 刷新页面调取onDraw方法，通过改变fi，达到移动效果
                 */
                invalidate();
            }
        });
        valueAnimator.start();
    }


}
