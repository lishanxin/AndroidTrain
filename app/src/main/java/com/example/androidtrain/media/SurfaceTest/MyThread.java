package com.example.androidtrain.media.SurfaceTest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * Created by lizz on 2018/7/2.
 */

public class MyThread extends Thread {

    private SurfaceHolder holder;
    private boolean run;

    public MyThread(SurfaceHolder holder){
        this.holder = holder;
        run = true;
    }

    @Override
    public void run() {
        int counter = 0;
        Canvas canvas = null;
        while (run){
            //具体绘制工作
            try {
                canvas = holder.lockCanvas();

                canvas.drawColor(Color.WHITE);

                Paint p = new Paint();

                p.setColor(Color.BLACK);
                p.setTextSize(30);

                Rect rect = new Rect(100, 50, 380, 330);
                canvas.drawRect(rect, p);
                canvas.drawText("Interval = " + (counter++) + "seconds", 100, 410, p);
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (canvas != null){
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public boolean isRun(){
        return run;
    }

    public void setRun(boolean run){
        this.run = run;
    }
}
