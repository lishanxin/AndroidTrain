package com.example.androidtrain.uiDesign.systemBar;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.androidtrain.R;

public class HideStatusBarActivity extends Activity {

    private static final String TAG = "HideStatusBar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideLowerIceCream();
        setContentView(R.layout.activity_hide_status_bar);
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideUperIceCream();
        //在标题栏下展示内容
//        setContentUnderStatusBar();
    }

    //<!--4.0及以下使用设置主题来隐藏状态栏 android:theme="@android:style/Theme.Holo.NoActionBar.Fullscreen" -->
    //还可以通过以下方法来隐藏状态栏
    private void hideLowerIceCream(){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //在4.1及以上可使用此种方式来隐藏，这个方式不好用，使用hideLowerIceCream()方法来替代。
    /**
     * 需注意以下几点：
     * 1：一旦UI标签被清除(比如跳转到另一个Activity),如果你还想隐藏状态栏你就必须再次设定它
     * 2：在不同的地方设置UI标签是有所区别的。如果你在Activity的onCreate()方法中隐藏系统栏，当用户按下
     * home键系统栏就会重新显示。当用户再重新打开Activity的时候，onCreate()不会被调用，所以系统栏还会
     * 保持可见。如果你想让在不同Activity之间切换时，系统UI保持不变，你需要在onResume()与
     * onWindowFocusChaned()里设定UI标签。
     * 3：setSystemUiVisibility()仅仅在被调用的View显示的时候才会生效
     * 4：当从View导航到别的地方时，用setSystemUiVisibility()设置的标签会被清除
     */
    private void hideUperIceCream(){
        if (Build.VERSION.SDK_INT >= 16){
            View decorView = getWindow().getDecorView();
            //Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);



            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            try {
                ActionBar actionBar = getActionBar();
                if (actionBar != null)actionBar.hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * 让内容显示在状态栏后面，以稳定布局。
     * 这个放在onWindowFocusChanged里会出问题，放在onResume里正常。
     */
    private void setContentUnderStatusBar(){
        if (Build.VERSION.SDK_INT >= 16){
            View decorView = getWindow().getDecorView();
            //Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);

            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            try {
                ActionBar actionBar = getActionBar();
                if (actionBar != null)actionBar.hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //全屏显示
//        hideUperIceCream();
        if (hasFocus){
            Log.d(TAG, "SystemUiVisibility: " + getWindow().getDecorView().getSystemUiVisibility());
        }
    }
}
