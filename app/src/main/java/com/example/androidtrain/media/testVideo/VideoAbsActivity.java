/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidtrain.media.testVideo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * This activity uses the camera/camcorder as the A/V source for the
 * {@link MediaRecorder} API. A {@link TextureView}
 * is used as the camera preview which limits the code to API 14+. This can be
 * easily replaced with a {@link android.view.SurfaceView} to run on older
 * devices.
 */
public abstract class VideoAbsActivity extends Activity {
    /**
     * 摄像头对象
     */
    protected Camera mCamera;

    /**
     * 录制器
     */
    protected MediaRecorder mMediaRecorder;
    /**
     * 录制状态为：true:录制中，false:未录制
     */
    protected boolean isRecording = false;
    protected static final String TAG = "VideoAbsActivity";

    class ComponpentGroup{
        /**
         * 实时画面
         */
        protected TextureView mPreview;
        /**
         * 录制按钮
         */
        protected View captureButton;
        /**
         * 停止按钮
         */
        protected View stopButton;
        /**
         * 保存按钮
         */
        protected View saveButton;
        /**
         * 切换镜头按钮
         */
        protected View changeButton;

        protected Button videoPlay;

        protected TextView recordtime;
    }
    //视频状态
    protected static final int STATUS_START = 1;
    protected static final int STATUS_RECORDING = 2;
    protected boolean recorded = false;

    protected  ComponpentGroup componpentGroup;
    Uri destUri;
    CamcorderProfile profile;
    /**
     * 调试模式，设置为true,会弹出调试信息
     */
    public static boolean DEBUG = false;

    /**
     * 最大录制时间
     */
    private int maxTime = 60;
    /**
     * 录制质量
     */
    private int quality = CamcorderProfile.QUALITY_QVGA;
    /**
     * 视频显示角度修复，默认为0度，不修复
     */
    private int rotationPrepare = 0;

    private int fps = -1;

    private String destFile;

    private boolean startRun;//计时线程

    private Activity currentActivity;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(VideoAgent.VIDEO_maxTime, maxTime);
        outState.putInt(VideoAgent.VIDEO_quality, quality);
        outState.putInt(VideoAgent.VIDEO_rotation, rotationPrepare);
        outState.putInt(VideoAgent.VIDEO_fps, fps);
        outState.putString("destFile",destFile);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        maxTime = savedInstanceState.getInt(VideoAgent.VIDEO_maxTime);
        quality = savedInstanceState.getInt(VideoAgent.VIDEO_quality);
        rotationPrepare = savedInstanceState.getInt(VideoAgent.VIDEO_rotation);
        fps = savedInstanceState.getInt(VideoAgent.VIDEO_fps);
        destFile = savedInstanceState.getString("destFile");
    }

    /**
     * 设置Layout布局文件，返回R.layout.xxxxx
     */
    protected abstract int onSetVideoLayout();
    /**
     * 点击录制的触发动作
     * */
    protected  abstract void act_capture();
    /**
     * 点击切换镜头的触发动作
     * */
    protected abstract  void act_change();
    /**
     * 点击停止的触发动作
     * */
    protected abstract  void act_stop();
    /**
     * 点击保存的触发动作
     * */
    protected abstract  void act_save();

    protected abstract void onSetVideoComponent(ComponpentGroup componpentGroup);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onSetVideoLayout());
        currentActivity = this;
        destUri = getIntent().getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);
        componpentGroup =new ComponpentGroup();
        onSetVideoComponent(componpentGroup);
        if (componpentGroup.mPreview == null || componpentGroup.captureButton == null || componpentGroup.stopButton == null || componpentGroup.changeButton == null || componpentGroup.saveButton == null) {
            throw new RuntimeException("视频录制组件未设置[实时画面:" + componpentGroup.mPreview + "," + "录制按钮:" + componpentGroup.captureButton + "," + "停止按钮:" + componpentGroup.stopButton + "," + "保存按钮:" + componpentGroup.saveButton + "," + "切换镜头按钮:" + componpentGroup.changeButton + "]");
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            maxTime = bundle.getInt(VideoAgent.VIDEO_maxTime);
            quality = bundle.getInt(VideoAgent.VIDEO_quality);
            rotationPrepare = bundle.getInt(VideoAgent.VIDEO_rotation);
            fps = bundle.getInt(VideoAgent.VIDEO_fps);
            destFile = bundle.getString("destFile");
        }
        act_stop();
        changeCamera();

        componpentGroup.mPreview.setSurfaceTextureListener(new SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                startPreview();

            }
        });
        componpentGroup.mPreview.setOnClickListener(fourceListener);
    }


    private OnClickListener fourceListener = new OnClickListener() {
        private volatile boolean lock = false;

        @Override
        public void onClick(View v) {
            if (mCamera != null && lock == false) {
                lock = true;
                mCamera.autoFocus(new AutoFocusCallback() {

                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        lock = false;

                    }
                });
            }

        }
    };

    int cameraId = -1;

    private void changeCamera() {
        if (cameraId == -1 || cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
    }

    private void initCamera() {
        if (cameraId == -1 || cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCamera = CameraHelper.getDefaultBackFacingCameraInstance();
            if (mCamera == null) {
                mCamera = CameraHelper.getDefaultFrontFacingCameraInstance();
                cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
        } else {
            mCamera = CameraHelper.getDefaultFrontFacingCameraInstance();
            if (mCamera == null) {
                mCamera = CameraHelper.getDefaultBackFacingCameraInstance();
                cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            }
        }
    }



    final Handler handler = new Handler(){
        private String secToTime(int time) {
            String timeStr = null;
            int hour = 0;
            int minute = 0;
            int second = 0;
            if (time <= 0)
                return "00:00";
            else {
                minute = time / 60;
                if (minute < 60) {
                    second = time % 60;
                    timeStr = unitFormat(minute) + ":" + unitFormat(second);
                } else {
                    hour = minute / 60;
                    if (hour > 99)
                        return "99:59:59";
                    minute = minute % 60;
                    second = time - hour * 3600 - minute * 60;
                    timeStr = this.unitFormat(hour) + ":" + this.unitFormat(minute) + ":" + this.unitFormat(second);
                }
            }
            return timeStr;
        }

        private String unitFormat(int i) {
            String retStr = null;
            if (i >= 0 && i < 10)
                retStr = "0" + Integer.toString(i);
            else
                retStr = "" + i;
            return retStr;
        }
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == 1){
                if(msg.arg1 == maxTime){
                    componpentGroup.stopButton.callOnClick();
                }

                componpentGroup.recordtime.setText(this.secToTime(msg.arg1));
            }
        }
    };
    /**
     * The capture button controls all user interaction. When recording, the
     * button click stops recording, releases
     * {@link MediaRecorder} and {@link Camera}.
     * When not recording, it prepares the {@link MediaRecorder}
     * and starts recording.
     *
     * @param view the view generating the event.
     */
    public void onCaptureClick(View view) {
        if (view.getId() == componpentGroup.captureButton.getId()) {
            startRun = false;
            if (!isRecording) {
                releaseCamera();

                act_capture();
                new MediaPrepareTask().execute(null, null, null);
                new Thread(new Runnable() {
                    private int time = 0;
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        while(!startRun){

                            time++;
                            Log.i("录像时间：",maxTime+"");
                            Message message = new Message();
                            message.what = 1;
                            message.arg1 = time;
                            handler.sendMessage(message);
                            if(time >=maxTime){
                                startRun = true;
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } else if (view.getId() == componpentGroup.stopButton.getId()) {
            if (isRecording) {
                startRun = true;
                act_stop();
                // stop recording and release camera
                if (mMediaRecorder != null) {
                    mMediaRecorder.stop(); // stop the recording
                }
                releaseMediaRecorder(); // release the MediaRecorder object
                if (mCamera != null) {
                    mCamera.lock();
                }
                isRecording = false;

                releaseCamera();
                startPreview();
            }
        } else if (view.getId() == componpentGroup.saveButton.getId()) {
            startRun = true;
            act_save();
            this.setResult(RESULT_OK);
            finish();
        } else if (view.getId() == componpentGroup.changeButton.getId()) {
            act_change();
            releaseCamera();
            changeCamera();
            startPreview();
        }else if(view.getId() == componpentGroup.videoPlay.getId()){
            Uri data = Uri.fromFile(new File(destFile));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(data, "video/mp4");
            startActivity(intent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();

    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if
            // the activity pauses.
            mCamera.lock();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean prepareVideoRecorder() {
        // BEGIN_INCLUDE (configure_preview)
        initCamera();
        setRoation();
        try {
            // Requires API level 11+, For backward compatibility use {@link
            // setPreviewDisplay}
            // with {@link SurfaceView}
            mCamera.setPreviewTexture(componpentGroup.mPreview.getSurfaceTexture());
        } catch (IOException e) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
            return false;
        }
        // END_INCLUDE (configure_preview)

        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();
        //设置视频的角度,rotate
        mMediaRecorder.setOrientationHint(90);
        try {
            profile = CamcorderProfile.get(cameraId, quality);
        } catch (Exception e) {
            try {
                profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QVGA);
            } catch (Exception e1) {
                try {
                    profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QCIF);
                } catch (Exception e2) {
                    try {
                        profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
                    } catch (Exception e3) {
                        try {
                            profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
                        } catch (Exception e4) {
                            try {
                                profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_CIF);
                            } catch (Exception e5) {
                                try {
                                    profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_720P);
                                } catch (Exception e6) {
                                    try {
                                        profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P);
                                    } catch (Exception e7) {
                                        try {
                                            profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_1080P);
                                        } catch (Exception e8) {
                                            e8.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setVideoEncodingBitRate(5*1024*1024);
        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        if (profile != null) {
            if(fps!=-1){
                profile.videoFrameRate=fps;
            }
            mMediaRecorder.setProfile(profile);
            VideoAbsActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    String text = "分辨率:" + profile.videoFrameWidth + "X" + profile.videoFrameHeight
                            + "\n帧数:" + profile.videoFrameRate
                            + "\n速率:" + profile.videoBitRate
                            + "\n编码:" + profile.videoCodec
                            + "\n质量:" + profile.quality;
                    if (DEBUG) {
                        Toast.makeText(VideoAbsActivity.this, text, Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }
        //设置录制的帧数
        if(fps!=-1){
            mMediaRecorder.setCaptureRate(fps);
        }

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(destUri.getPath());
        // END_INCLUDE (configure_media_recorder)

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (Exception e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    /**
     * Asynchronous task for preparing the {@link MediaRecorder}
     * since it's a long blocking operation.
     */
    class MediaPrepareTask extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();
                recorded = true;
                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                VideoAbsActivity.this.finish();
            }

        }
    }

    private void startPreview() {
        initCamera();
        setRoation();
        try {
            mCamera.setPreviewTexture(componpentGroup.mPreview.getSurfaceTexture());
        } catch (IOException t) {
        }
        mCamera.startPreview();
    }

    private void setRoation() {
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        Log.i(TAG, "重力感应：" + rotation);
        if (Surface.ROTATION_90 == rotation) {
            mCamera.setDisplayOrientation((0 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_180 == rotation) {
            mCamera.setDisplayOrientation((270 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_270 == rotation) {
            mCamera.setDisplayOrientation((180 + rotationPrepare) % 360);
        } else if (Surface.ROTATION_0 == rotation) {
            mCamera.setDisplayOrientation((90 + rotationPrepare) % 360);
        }

    }

}