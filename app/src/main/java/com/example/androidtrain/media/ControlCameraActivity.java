package com.example.androidtrain.media;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidtrain.R;

import java.io.IOException;
import java.util.List;

public class ControlCameraActivity extends AppCompatActivity implements View.OnClickListener{

    Camera mCamera;
    Preview mPreview;
    List<Camera.Size> mSuppotedPreviewSizes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mPreview = new Preview(this);
//        safeCameraOpen(20);
        setContentView(mPreview);
    }

    /**
     *如果我们在执行Camera.open()方法的时候Camera正在被另外一个应用使用，
     * 那么函数会抛出一个Exception，我们可以利用try语句块进行捕获
     */
    private boolean safeCameraOpen(int id){
        boolean qOpened = false;

        releaseCameraAndPreview();
        mCamera = Camera.open();
        qOpened = (mCamera != null);

        mPreview.setCamera(mCamera);
        return qOpened;
    }

    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View v) {

    }

    class Preview extends ViewGroup implements SurfaceHolder.Callback{
        SurfaceView mSurfaceView;
        SurfaceHolder mHolder;

        Preview(Context context){
            super(context);

            mSurfaceView = new SurfaceView(context);
            addView(mSurfaceView);

            //Install a SurfaceHolder.Callback so we get notified when the
            //underlying surface is created and destroyed.
            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
//            safeCameraOpen(20);

            mCamera = Camera.open();

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                mCamera.release();
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //Now that the size is known, set up the camera parameters and begin
            // the preview.
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreview.getWidth(), mPreview.getHeight());
            requestLayout();
            mCamera.setParameters(parameters);

            mCamera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null){
                mCamera.stopPreview();
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }

        public void setCamera(Camera camera){
            if (mCamera == camera) {
                return;
            }
            stopPreviewAndFreeCamera();
            mCamera = camera;

            if (mCamera != null){
                List<Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
                mSuppotedPreviewSizes = localSizes;
                requestLayout();

                try {
                    mCamera.setPreviewDisplay(mHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Important: Call startPreview() to Start updating the preview
                //surface. Preview must be started before you can take a picture.
                mCamera.startPreview();
            }
        }

        /**
         * When this function returns, mCamera will be null.
         */
        private void stopPreviewAndFreeCamera(){
            if (mCamera != null){
                mCamera.stopPreview();

                mCamera.release();

                mCamera = null;
            }
        }
    }
}
