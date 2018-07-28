package com.example.androidtrain.media.SurfaceTest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lizz on 2018/7/2.
 */

public class MySurfaceViewTest  extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG = "MySurfaceActivity";

    //角度补偿，为0，则不补偿
    private int rotationPrepare = 0;

    private static boolean repaireDegree = true;

    private SurfaceHolder holder;
//    private MyThread myThread;
    private MyCameraThread myCameraThread;

    private Activity mActivity;
    Camera mCamera;

    private OnSetSurfaceLayout mOnSetSurfaceLayout;

    public View.OnClickListener onFocusClickListener = new View.OnClickListener() {
        private  boolean lock = false;
        @Override
        public void onClick(View v) {
            Log.d(TAG, "click");
            if (!lock && mCamera !=null){
                lock = true;
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        lock = false;
                        try {
                            mCamera.takePicture(null, null, new MySurfaceViewTest.MyPictureCallback());
                        }catch (RuntimeException e){
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };

    public View.OnClickListener takePhotoClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
                mCamera.takePicture(null, null, new MySurfaceViewTest.MyPictureCallback());
            }catch (RuntimeException e){
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public MySurfaceViewTest(Activity activity) {
        super(activity);

        holder = getHolder();

        holder.addCallback(this);

        mActivity = activity;

    }

    protected void setOnSetSurfaceLayoutListener(OnSetSurfaceLayout onSetSurfaceLayout){
        mOnSetSurfaceLayout = onSetSurfaceLayout;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setShow();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        myThread.setRun(true);
//        myThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        myThread.setRun(false);
    }

    private void setShow(){
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        try {
            Camera camera = Camera.open();
            setCamera(camera);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCamera(Camera camera) {

        if (camera != null){
            try {
//                setRoation(camera);
                setCameraDisplayOrientation(mActivity, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
                setPictureRotation(camera);
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = findBestPictureSize(parameters, camera);
                parameters.setPictureSize(size.width, size.height);
                parameters.setPreviewSize(1440,1080);
                Camera.Size previewSize = parameters.getPreviewSize();
                if (mOnSetSurfaceLayout != null){
                    mOnSetSurfaceLayout.OnSet(parameters);
                }
                camera.setParameters(parameters);

                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera = camera;
            camera.startPreview();
        }
    }

    private void setPictureRotation(Camera camera) {
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        Camera.Parameters parameters = camera.getParameters();
        Log.i(TAG, "重力感应：" + rotation);
        if (Surface.ROTATION_90 == rotation) {
            parameters.setRotation((0 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_180 == rotation) {
            parameters.setRotation((270 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_270 == rotation) {
            parameters.setRotation((180 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_0 == rotation) {
            parameters.setRotation((90 + rotationPrepare) % 360);
        }
        camera.setParameters(parameters);
    }

    //设置Camera图片展示角度
    private void setRoation(Camera camera) {
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        Log.i(TAG, "重力感应：" + rotation);
        if (Surface.ROTATION_90 == rotation) {
            camera.setDisplayOrientation((0 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_180 == rotation) {
            camera.setDisplayOrientation((270 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_270 == rotation) {
            camera.setDisplayOrientation((180 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_0 == rotation) {
            camera.setDisplayOrientation((90 + rotationPrepare) % 360);
        }

    }

    public static void setCameraDisplayOrientation(Activity activity,
              int cameraId, android.hardware.Camera camera) {
          android.hardware.Camera.CameraInfo info =
                  new android.hardware.Camera.CameraInfo();
          android.hardware.Camera.getCameraInfo(cameraId, info);
          int rotation = activity.getWindowManager().getDefaultDisplay()
                           .getRotation();
          int degrees = 0;
          switch (rotation) {
              case Surface.ROTATION_0: degrees = 0; break;
              case Surface.ROTATION_90: degrees = 90; break;
              case Surface.ROTATION_180: degrees = 180; break;
              case Surface.ROTATION_270: degrees = 270; break;
          }

          int result;
          if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
              result = (info.orientation + degrees) % 360;
              result = (360 - result) % 360;  // compensate the mirror
          } else {  // back-facing
              result = (info.orientation - degrees + 360) % 360;
          }
          camera.setDisplayOrientation(result);
     }


    public static class MyPictureCallback implements Camera.PictureCallback {

        protected void saveBitmapToSD(Bitmap bt) {
            File bOldFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aImage/", "testFile.jpg" );
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(bOldFile);
                bt.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = false;
            Bitmap bOld = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            saveBitmapToSD(bOld);
        }
    }

    private Camera.Size findBestPreviewSize(Camera.Parameters parameters, Camera camera) {
        int degree = getPreviewDegree(mActivity);
        int srcWidth = 0;
        int srcHeight = 0;
        if (degree == 0 || degree == 180) {
            srcWidth = parameters.getPictureSize().width;
            srcHeight = parameters.getPictureSize().height;
        } else {
            srcWidth = parameters.getPictureSize().height;
            srcHeight = parameters.getPictureSize().width;
        }

        int supportPreviewWidth = getScreenWH().widthPixels;
        int supportPreviewHeight = getScreenWH().heightPixels;
        float wbl = (float) srcWidth / supportPreviewWidth;
        float hbl = (float) srcHeight / supportPreviewHeight;
        // float bl = wbl > hbl ? wbl : hbl;
        int width = (int) (srcWidth / wbl);// bl);
        int height = (int) (srcHeight / hbl);// bl);

        // int width =(int) (srcWidth / bl);
        // int height = (int) (srcHeight / bl);
        return camera.new Size(width, height);
    }

    private Camera.Size findBestPictureSize(Camera.Parameters parameters, Camera camera) {
        int diff = Integer.MIN_VALUE;
        String pictureSizeValueString = parameters.get("picture-size-values");

        // saw this on Xperia
        if (pictureSizeValueString == null) {
            pictureSizeValueString = parameters.get("picture-size-value");
        }

        if (pictureSizeValueString == null) {
            return camera.new Size(getScreenWH().widthPixels, getScreenWH().heightPixels);
        }

//		LogUtils.d("pictureSizeValueString : " + pictureSizeValueString);
        int bestX = 0;
        int bestY = 0;

        for (String pictureSizeString : pictureSizeValueString.split(",")) {
            pictureSizeString = pictureSizeString.trim();

            int dimPosition = pictureSizeString.indexOf('x');
            if (dimPosition == -1) {
//				LogUtils.e("Bad pictureSizeString:" + pictureSizeString);
                continue;
            }

            int newX = 0;
            int newY = 0;

            try {
                newX = Integer.parseInt(pictureSizeString.substring(0, dimPosition));
                newY = Integer.parseInt(pictureSizeString.substring(dimPosition + 1));
            } catch (NumberFormatException e) {
//				LogUtils.e("Bad pictureSizeString:" + pictureSizeString);
                continue;
            }

            Point screenResolution = new Point(getScreenWH().widthPixels, getScreenWH().heightPixels);

            int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
            if (newDiff == diff) {
                bestX = newX;
                bestY = newY;
                break;
            } else if (newDiff > diff) {
                if (((3 * newX) == (4 * newY)) && bestX < newX) {
                    bestX = newX;
                    bestY = newY;
                    diff = newDiff;
                }
            }
        }

        if (bestX > 0 && bestY > 0) {
            return camera.new Size(bestX, bestY);
        }
        return null;
    }

    protected DisplayMetrics getScreenWH() {
        DisplayMetrics dMetrics = new DisplayMetrics();
        dMetrics = this.getResources().getDisplayMetrics();
        return dMetrics;
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

    public interface OnSetSurfaceLayout {
        public void OnSet(Camera.Parameters parameters);
    }
}
