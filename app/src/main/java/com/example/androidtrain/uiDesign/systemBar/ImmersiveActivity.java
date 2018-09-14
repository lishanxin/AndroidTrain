package com.example.androidtrain.uiDesign.systemBar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidtrain.R;

public class ImmersiveActivity extends AppCompatActivity {

    private static final String TAG = "ImmersiveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersive();
        setContentView(R.layout.activity_immersive);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setImmersive();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setImmersive();
    }

    //版本大于等于19
    /**
     * 1:新标签SYSTEM_UI_FLAG_IMMERSIVE与SYSTEM_UI_FLAG_HIDE_NAVIGATION和SYSTEM_UI_FLAG_FULLSCREEN一起
     * 使用的时候，导航栏和状态栏就会隐藏，让你的应用可以接受屏幕上任何地方的触摸事件。
     * 2:用户可以通过在边缘区域向内滑动来让系统栏重新显示。这个操作清空了SYSTEM_UI_FLAG_HIDE_NAVIGATION
     * (和SYSTEM_UI_FLAG_FULLSCREEN，如果有的话)两个标签，因此系统栏重新变得可见。如果设置了的话，这个操
     * 作同时也触发了View.OnSystemUiVisibilityChangeListener
     * 3:如果你想让系统栏在一段时间后自动隐藏的话，你应该使用SYSTEM_UI_FLAG_IMMERSIVE_STICKY标签。
     * 请注意，带有'sticky'的标签不会触发任何的监听器，因为在这个模式下展示的系统栏是处于临时(transient)的状态。
     */
    private void setImmersive(){
        View decorView = getWindow().getDecorView();

        decorView.setBackgroundColor(Color.parseColor("#e50011"));

        int uiOption = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.d(TAG, "visibility:" + visibility);
            }
        });

    }

    private void setImmersiveStick(){
        View decorView = getWindow().getDecorView();

        decorView.setBackgroundColor(Color.parseColor("#e50011"));

        int uiOption = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);
    }
}
