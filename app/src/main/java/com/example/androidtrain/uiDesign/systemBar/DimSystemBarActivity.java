package com.example.androidtrain.uiDesign.systemBar;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.androidtrain.R;
//淡化系统栏：状态栏与导航栏
public class DimSystemBarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dim_system_bar);

        // This example uses decor view, but you can use any visible view.
        View decorView = getWindow().getDecorView();
        //淡化标签，不是说不可见了，只是淡化了些元素
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();



    }
}
