package com.example.androidtrain.media.SurfaceTest;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;

import java.util.List;

/**
 * Created by lizz on 2018/7/2.
 */

public class SurfaceTestActivity extends BaseActivity {

    private static final String TAG = "SurfaceTest";

    MySurfaceViewTest surfaceView;

    FrameLayout frameLayout;

    Button takePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_surface_test);


        frameLayout = (FrameLayout) findViewById(R.id.surface_test_container);
        surfaceView = new MySurfaceViewTest(this);
        surfaceView.setOnClickListener(surfaceView.onFocusClickListener);
        surfaceView.setOnSetSurfaceLayoutListener(new MySurfaceViewTest.OnSetSurfaceLayout() {
            @Override
            public void OnSet(Camera.Parameters parameters) {
                ViewTreeObserver viewTreeObserver = surfaceView.getViewTreeObserver();
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final DisplayMetrics dm = getResources().getDisplayMetrics();
                        final int screenWidth = dm.widthPixels;
                        final int screenHeight = dm.heightPixels;
                        Log.d(TAG, "DisplayMetrics: width/height"  + screenWidth + "/" + screenHeight);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) surfaceView.getLayoutParams();
                        layoutParams.height = screenHeight;
                        layoutParams.width = 720* screenHeight/960;
                        surfaceView.setLayoutParams(layoutParams);
                        surfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

                List<Camera.Size> list = parameters.getSupportedPictureSizes();
                for (Camera.Size size: list){
                    Log.d(TAG, "Picture Size Width:Height  " + size.width + ":" + size.height + "=" + (float)size.width/size.height);
                }
                list = parameters.getSupportedPreviewSizes();
                for (Camera.Size size: list){
                    Log.d(TAG, "Preview Size Width:Height  " + size.width + ":" + size.height+ "=" + (float)size.width/size.height);
                }
                Log.d(TAG, "Width:Height  " + parameters.getPreviewSize().width + ":" + parameters.getPreviewSize().height);
            }
        });
        frameLayout.addView(surfaceView);
//        takePhoto = (Button) LayoutInflater.from(this).inflate(R.layout.fragment_surface_test_button, null);
////        takePhoto = (Button) view.findViewById(R.id.surface_test_take_photo);
//
//        frameLayout.addView(takePhoto);
//
//        takePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    surfaceView.mCamera.takePicture(null, null, new MySurfaceViewTest.MyPictureCallback());
//                }catch (RuntimeException e){
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
