package com.example.androidtrain.uiDesign.materialDesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.androidtrain.R;

/**
 * Material设计规范：http://www.google.com/design/spec
 * 要在应用中使用 Material 主题，需要定义一个继承于 android:Theme.Material 的 style 文件：
 */
public class MaterialMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_main);
    }
}
