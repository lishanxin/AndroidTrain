package com.example.androidtrain.uiDesign.systemBar;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.androidtrain.R;

public class HideStatusBarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideLowerIceCream();
        setContentView(R.layout.activity_hide_status_bar);
    }

    //<!--4.0及以下使用设置主题来隐藏状态栏 android:theme="@android:style/Theme.Holo.NoActionBar.Fullscreen" -->
    //还可以通过以下方法来隐藏状态栏
    private void hideLowerIceCream(){
        if (Build.VERSION.SDK_INT < 16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
