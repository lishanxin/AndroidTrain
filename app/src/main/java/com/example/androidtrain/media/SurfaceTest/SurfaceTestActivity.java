package com.example.androidtrain.media.SurfaceTest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lizz on 2018/7/2.
 */

public class SurfaceTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MySurfaceViewTest(this));
    }
}
