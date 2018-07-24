package com.example.androidtrain.media.testVideo;

/**
 * Created by user on 2017/3/7.
 */

import android.app.Activity;
import android.content.Intent;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Description: 视频录制
*/
public class VideoAgent {
    private static final String TAG = "VideoAgent";
    public static final int ACTION_VIDEO_CAPTURE = 0;
    public static final int ACTION_VIDEO_CAPTURE_SUCCESS = 1;
    public static final int ACTION_VIDEO_CAPTURE_FAIL = 2;

    public static final String VIDEO_maxTime = "VIDEO_maxTime";
    public static final String VIDEO_quality = "VIDEO_quality";
    public static final String VIDEO_rotation = "VIDEO_rotation";
    public static final String VIDEO_fps = "VIDEO_fps";

    File destFile;
    private Activity context;
    private android.app.Fragment fragment;
    private OnCameraCapture listener;
    Bundle data = new Bundle();

    private int maxTime = 60;
    private int quality = CamcorderProfile.QUALITY_QVGA;
    private int rotation = 0;
    private int fps = -1;

    private Class<? extends  Activity > videoActivityClazz;
    /**
     * 视频客户端构造函数，传入视频录制的activity
     * */
    public VideoAgent(Class<? extends  Activity > videoActivityClazz) {
        this.videoActivityClazz = videoActivityClazz;
    }

    public void startCamera(Activity activity, final File destFile, final OnCameraCapture listener) throws IOException {
        recovery(activity, null, destFile, listener);
        start();
    }

    public void startCamera(Activity activity, android.app.Fragment fragment, final File destFile, final OnCameraCapture listener) throws IOException {
        recovery(activity, fragment, destFile, listener);
        start();
    }

    public void recovery(Activity activity, android.app.Fragment fragment, final File destFile, final OnCameraCapture listener) {
        this.destFile = destFile;
        this.context = activity;
        this.fragment = fragment;
        this.listener = listener;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * @param quality
     * @Title: setQuality
     * @Description: CamcorderProfile.QUALITY_QVGA
     */
    public void setQuality(int quality) {
        this.quality = quality;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    private void start() {
        Intent intent = new Intent(context, videoActivityClazz);
        Uri uri = Uri.fromFile(destFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // set the image file //
        // name
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // set the video
        intent.putExtra(VIDEO_maxTime, maxTime); // set the video
        intent.putExtra(VIDEO_quality, quality); // set the video
        intent.putExtra(VIDEO_rotation, rotation); // set the video
        intent.putExtra(VIDEO_fps, fps); // set the video
        intent.putExtra("destFile",destFile.getAbsolutePath());
        if (fragment != null) {
            fragment.startActivityForResult(intent, ACTION_VIDEO_CAPTURE);
        } else {
            context.startActivityForResult(intent, ACTION_VIDEO_CAPTURE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) throws FileNotFoundException, IOException {
        if (resultCode == Activity.RESULT_CANCELED) {

        } else {
            switch (requestCode) {
                case ACTION_VIDEO_CAPTURE:
                    if (resultCode == Activity.RESULT_OK) {
                        Log.i(TAG, "视频文件已创建[" + destFile.getAbsolutePath() + "]");
                        if (listener != null) {
                            listener.loadMedia(destFile);
                        }
                    } else {
                        Toast.makeText(context, "拍摄失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnCameraCapture {
        public void loadMedia(File file);
    }
}

