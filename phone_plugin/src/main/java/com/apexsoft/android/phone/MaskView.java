package com.apexsoft.android.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 遮罩层
 * @author xxh
 *
 */
public class MaskView extends AppCompatImageView {
    private static final String TAG = "MaskView";
    private Paint mLinePaint;
    private Paint mAreaPaint;
    private Rect mCenterRect = null;
  
  
    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);  
        initPaint();  
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
        Point p = new Point(w_screen, h_screen);
        widthScreen = p.x;  
        heightScreen = p.y;  
    }  
  
    private void initPaint(){  
        //绘制中间透明区域矩形边界的Paint  
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLUE);
        mLinePaint.setStyle(Style.STROKE);
        mLinePaint.setStrokeWidth(5f);  
        mLinePaint.setAlpha(30);  
  
        //绘制四周阴影区域  
        mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAreaPaint.setColor(Color.GRAY);
        mAreaPaint.setStyle(Style.FILL);
        mAreaPaint.setAlpha(150);  
          
          
          
    }  
    public void setCenterRect(Rect r){
        Log.i(TAG, "setCenterRect...");
        this.mCenterRect = r;  
        postInvalidate();  
    }  
    public void clearCenterRect(Rect r){
        this.mCenterRect = null;  
    }  
  
    int widthScreen, heightScreen;  
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub  
        Log.i(TAG, "onDraw...");
        if(mCenterRect == null)  
            return;  
        //绘制四周阴影区域  
        canvas.drawRect(0, 0, widthScreen, mCenterRect.top, mAreaPaint);  
        canvas.drawRect(0, mCenterRect.bottom + 1, widthScreen, heightScreen, mAreaPaint);  
        canvas.drawRect(0, mCenterRect.top, mCenterRect.left - 1, mCenterRect.bottom  + 1, mAreaPaint);  
        canvas.drawRect(mCenterRect.right + 1, mCenterRect.top, widthScreen, mCenterRect.bottom + 1, mAreaPaint);  
  
        //绘制目标透明区域  
       // canvas.drawRect(mCenterRect, mLinePaint);  
        super.onDraw(canvas);  
    }  
  
  
  
}  
