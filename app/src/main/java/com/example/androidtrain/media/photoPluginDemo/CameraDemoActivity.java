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
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    float cropRate = 0.7f;
    DisplayMetrics screenMetrics;
    SurfaceView mSurfaceView;
    ImageView takePicture;
    SurfaceHolder holder;

    MaskView maskView;
    ImageView pictureShow;

    Button commitBtn;
    Button retakeBtn;

    TextView topTip;
    TextView bottomTip;

    Camera mCamera;

    String picturePath = null;

    int degree = 90;

    /** 聚焦 */
    private Camera.AutoFocusCallback mAutoFocusCallback;
    @Override
    public int getContentLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        maskView = (MaskView) findViewById(R.id.view_mask);
        pictureShow = (ImageView) findViewById(R.id.camera_image) ;
        commitBtn = (Button) findViewById(R.id.commit_picture);
        retakeBtn = (Button) findViewById(R.id.retake_picture);
        topTip = (TextView) findViewById(R.id.camera_tip1);
        bottomTip = (TextView) findViewById(R.id.camera_tip2);


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
        pictureShow.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        retakeBtn.setOnClickListener(this);
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
                mCamera.takePicture(null, null, new MyPictureCallback());
                mCamera.setOneShotPreviewCallback(null);
//                mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                    @Override
//                    public void onAutoFocus(boolean success, Camera camera) {
//                        if (success) {
//                            camera.setOneShotPreviewCallback(null);
//                        }
//
//                        camera.takePicture(null, null, new MyPictureCallback());
//                    }
//                });
                setViewVisible(false);
            }else if (id == R.id.camera_image){
                // 拍摄时自动对焦，不启用这个
                try {
                    mCamera.autoFocus(mAutoFocusCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (id == R.id.commit_picture){
                // 确认照片，返回照片目录 picturePath

            }else if (id == R.id.retake_picture){
                // 重新拍摄照片
                picturePath = null;
                pictureShow.setImageBitmap(null);
                mCamera.startPreview();
                setViewVisible(true);
            }
        }
    }

    public void setViewVisible(boolean waitTakePicture){
        if (waitTakePicture){
            takePicture.setVisibility(View.VISIBLE);
            commitBtn.setVisibility(View.GONE);
            retakeBtn.setVisibility(View.GONE);
            bottomTip.setVisibility(View.VISIBLE);
            topTip.setVisibility(View.VISIBLE);
        }else {
            takePicture.setVisibility(View.GONE);
            commitBtn.setVisibility(View.VISIBLE);
            retakeBtn.setVisibility(View.VISIBLE);
            bottomTip.setVisibility(View.GONE);
            topTip.setVisibility(View.GONE);
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
            try {
                mCamera = Camera.open(cameraIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            screenMetrics = getScreenWH();
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
            // 设置截图区域
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pictureShow.getLayoutParams();

            params.width = (int) (screenMetrics.widthPixels*cropRate);
            params.height = (int) (screenMetrics.heightPixels*cropRate);
            pictureShow.setLayoutParams(params);
            // 设置遮罩层xxh20141103新增
            if (maskView != null) {
                Rect screenCenterRect = createCenterScreenRect((int) (screenMetrics.widthPixels*cropRate)/*(previewSize.width * imageWidthRate)*/, (int) (screenMetrics.heightPixels*cropRate)/*(previewSize.height * imageHeightRate)*/);
                maskView.setCenterRect(screenCenterRect);
            }
        }
    }

    private Camera.Size findBestPictureSize(Camera.Parameters parameters) {
        List<Camera.Size> listSize = parameters.getSupportedPictureSizes();

        if (listSize == null || listSize.size() == 0){
            return mCamera.new Size(getScreenWH().widthPixels, getScreenWH().heightPixels);
        }

        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        int x = screenMetrics.widthPixels;int y = screenMetrics.heightPixels;
        Point screenResolution = null;

        // 存储屏幕高宽，以作近似查找时的标准
//        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
//            //横屏
//            screenResolution =  new Point(x, y);
//        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
//            //竖屏
//            screenResolution =  new Point(y, x);
//        }
        screenResolution =  new Point(x, y);
        // 绝对比较的赋值
        int bestX = 0;
        int bestY = 0;

        // 相对比较的赋值
        int nearX = 0;
        int nearY = 0;
        //绝对查找时的较优比较值
        int diff = Integer.MAX_VALUE;
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
            if (newX * screenResolution.y == newY * screenResolution.x){
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

            // 扩张宽度
            int extendW = 5;
            int extendH = 5;
            // 增加部分截取边距
            int x = (int) (bOld.getWidth() * (1 - cropRate) / 2 - extendW);
            int y = (int) (bOld.getHeight() * (1 - cropRate) / 2 - extendH);
            // 防止截取越界
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            int cropWidth = (int) (bOld.getWidth() * cropRate + extendW * 2);
            int cropHeight = (int) (bOld.getHeight() * cropRate + extendH * 2);
            if (x + cropWidth > bOld.getWidth()) {
                cropWidth = bOld.getWidth() - x;
            }
            if (y + cropHeight > bOld.getHeight()) {
                cropHeight = bOld.getHeight() - y;
            }
            Bitmap cropPicture = Bitmap.createBitmap(bOld, x, y, cropWidth, cropHeight);
            pictureShow.setImageBitmap(cropPicture);
            FileOutputStream out = null;
            try {
                File imageFile = new File(MainActivity.path + "CameraDemoActivity.jpeg");
                out = new FileOutputStream(imageFile);
                cropPicture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                picturePath = imageFile.getAbsolutePath();
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

        }
    }

    /**
     * 生成屏幕中间的矩形
     *
     * @param w
     *            目标矩形的宽度,单位px
     * @param h
     *            目标矩形的高度,单位px
     * @return
     */
    private Rect createCenterScreenRect(int w, int h) {
        int x1 = screenMetrics.widthPixels / 2 - w / 2;
        int y1 =  screenMetrics.heightPixels / 2 - h / 2;
        int x2 = x1 + w;
        int y2 = y1 + h;
        return new Rect(x1, y1, x2, y2);
    }
}
