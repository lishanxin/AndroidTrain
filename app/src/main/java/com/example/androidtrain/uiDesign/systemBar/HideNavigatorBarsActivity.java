package com.example.androidtrain.uiDesign.systemBar;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.androidtrain.R;

public class HideNavigatorBarsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_navigator_bars);

        hideNavigator();
    }


    //4.1及以上版本
    //隐藏导航栏：View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  View.SYSTEM_UI_FLAG_FULLSCREEN
    //让视图显示在导航栏下方：View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    private void hideNavigator(){
        if (Build.VERSION.SDK_INT >= 16){
            View decorView = getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
