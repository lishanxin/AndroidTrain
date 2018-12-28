package com.example.androidtrain.media.photoPluginDemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.apex.common.app.Activity;
import com.example.androidtrain.MainActivity;
import com.example.androidtrain.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sx on 2018/12/27.
 */

public class CameraDemoActivity extends Activity implements View.OnClickListener {

    public static void show(Context context){
        Intent intent = new Intent(context, CameraDemoActivity.class);
        context.startActivity(intent);
    }

    private static boolean repaireDegree = true;
    SurfaceView mSurfaceView;
    ImageView takePicture;
    ImageView pictureShow;
    SurfaceHolder holder;

    Camera mCamera;

    int degree = 90;

    /** 聚焦 */
    private Camera.AutoFocusCallback mAutoFocusCallback;
    @Override
    public int getContentLayoutId() {
        return R.layout.activity_camera_demo;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        mSurfaceView = (SurfaceView)findViewById(R.id.camera_surfaceview);
        takePicture = (ImageView) findViewById(R.id.camera_save);
        pictureShow = (ImageView) findViewById(R.id.show_picture_stored);


        holder = mSurfaceView.getHolder();

        holder.setKeepScreenOn(true);
        holder.setSizeFromLayout();
        holder.addCallback(new SurfaceCallback());
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mAutoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera c) {
                if (success) {
                    mCamera.setOneShotPreviewCallback(null);
                }
            }
        };

        takePicture.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (mCamera != null){
            int id = v.getId();
            if (id == R.id.camera_save){
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            camera.setOneShotPreviewCallback(null);
                        }

                        camera.takePicture(null, null, new MyPictureCallback());
                    }
                });
            }
        }
    }


    class SurfaceCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            int cameraCount = Camera.getNumberOfCameras();
            int cameraIndex = 0;
            for(int i = 0; i < cameraCount; i++){
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                    cameraIndex = i;
                }
            }
            mCamera = Camera.open(cameraIndex);
            try{
                mCamera.setPreviewDisplay(holder);
            }catch (IOException e){
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
                mCamera.setDisplayOrientation(getPreviewDegree(CameraDemoActivity.this));
            }

            updateCameraParameters();
            mCamera.startPreview();
            mCamera.autoFocus(mAutoFocusCallback);

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    // 主要设置相机显示相关参数
    private void updateCameraParameters() {
        if (mCamera != null){
            Camera.Parameters parameters = mCamera.getParameters();
            mCamera.setParameters(parameters);
            // 设置相片格式
            parameters.setPictureFormat(ImageFormat.JPEG);
            // 低版本会报错
            parameters.setRotation(getPreviewDegree(CameraDemoActivity.this));
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // 设置测光
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
                if (parameters.getMaxNumMeteringAreas() > 0){// 检查是否支持测光区域
                    List<Camera.Area> meteringArea = new ArrayList<>();
                    Rect areaRect = new Rect(-100, -100, 100, 100);//在图像中心指定一个区域
                    meteringArea.add(new Camera.Area(areaRect, 1000));
                    parameters.setMeteringAreas(meteringArea);
                }
            }

            // 设置图片大小
            Camera.Size pictureSize = findBestPictureSize(parameters);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);

            mCamera.setParameters(parameters);
        }
    }

    private Camera.Size findBestPictureSize(Camera.Parameters parameters) {
        List<Camera.Size> listSize = parameters.getSupportedPictureSizes();

        if (listSize == null || listSize.size() == 0){
            return mCamera.new Size(getScreenWH().widthPixels, getScreenWH().heightPixels);
        }

        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        DisplayMetrics screenMetrics = getScreenWH();
        int x = screenMetrics.widthPixels;int y = screenMetrics.heightPixels;
        Point screenResolution = null;

        // 存储屏幕高宽，以作近似查找时的标准
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            screenResolution =  new Point(x, y);
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            screenResolution =  new Point(y, x);
        }

        // 绝对比较的赋值
        int bestX = 0;
        int bestY = 0;

        // 相对比较的赋值
        int nearX = 0;
        int nearY = 0;
        //绝对查找时的较优比较值
        int diff = Integer.MIN_VALUE;
        // 近似查找时的较优比较值
        float nearDiff = Float.MAX_VALUE;
        // 判断是否已经进行过绝对查找的赋值
        boolean haveAbsoluteValue = false;
        // 开始循环遍历所支持的相片尺寸
        for (Camera.Size size: listSize){
            // 遍历出来的size宽高存储位置
            int newX = size.width;
            int newY = size.height;

            // 判断使用的是近似查找还是绝对查找
            if ((((float)newX/screenResolution.x) == ((float)newY/screenResolution.y))){
                //绝对查找，进行比较，此处也可以用平方和来进行比较
                int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
                if (newDiff == diff){
                    //如果比较值相同，则选择高像素的值进行存储
                    if (newX>bestX){
                        bestX = newX;
                        bestY = newY;
                    }
                    break;
                }else if (newDiff < diff){
                    //如果比原先的差值小，则替换掉原来的值
                    haveAbsoluteValue = true;
                    if (newX>bestX){
                        bestX = newX;
                        bestY = newY;
                        diff = newDiff;
                    }
                }
            }else if (!haveAbsoluteValue){//如果没有进行过绝对查找，才会使用近似查找的方法进行查找
                //进行近似查找
                //近似查找比较值
                float newNearDiff = Math.abs((float) newX/newY - (float)screenResolution.x/screenResolution.y);
                if (newNearDiff < nearDiff){
                    nearX = newX;
                    nearY = newY;
                    nearDiff = newNearDiff;
                }
            }
        }

        if (bestX > 0 && bestY > 0) {
            return mCamera.new Size(bestX, bestY);
        }else if(nearX>0 && nearY > 0){
            return mCamera.new Size(nearX, nearY);
        } else{
            return mCamera.new Size(screenMetrics.widthPixels, screenMetrics.heightPixels);
        }
    }

    public static int getPreviewDegree(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                if (repaireDegree) {
                    degree = 90;
                } else {
                    degree = 0;
                }
                break;
            case Surface.ROTATION_90:
                if (repaireDegree) {
                    degree = 0;
                } else {
                    degree = 90;
                }
                break;
            case Surface.ROTATION_180:
                if (repaireDegree) {
                    degree = 270;
                } else {
                    degree = 180;
                }
                break;
            case Surface.ROTATION_270:
                if (repaireDegree) {
                    degree = 180;
                } else {
                    degree = 270;
                }
                break;
        }
        return degree;
    }

    protected DisplayMetrics getScreenWH() {
        DisplayMetrics dMetrics = new DisplayMetrics();
        dMetrics = this.getResources().getDisplayMetrics();
        return dMetrics;
    }

    private class MyPictureCallback implements Camera.PictureCallback {
        /**
         * Called when image data is available after a picture is taken.
         * The format of the data depends on the context of the callback
         * and {@link Camera.Parameters} settings.
         *
         * @param data   a byte array of the picture data
         * @param camera the Camera service object
         */
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.stopPreview();
            camera.setPreviewCallback(null) ;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = false;
            Bitmap bOld = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            FileOutputStream out = null;
            try {
                File imageFile = new File(MainActivity.path + "CameraDemoActivity.jpeg");
                out = new FileOutputStream(imageFile);
                bOld.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                if (out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            pictureShow.setImageBitmap(bOld);
            pictureShow.setVisibility(View.VISIBLE);
        }
    }
}
