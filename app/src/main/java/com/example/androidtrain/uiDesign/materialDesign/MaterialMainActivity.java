package com.example.androidtrain.uiDesign.materialDesign;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.TextView;

import com.example.androidtrain.R;

/**
 * Material设计规范：http://www.google.com/design/spec
 * 要在应用中使用 Material 主题，需要定义一个继承于 android:Theme.Material 的 style 文件：
 */
public class MaterialMainActivity extends Activity {

    TextView mainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_main);

        mainTextView = (TextView) findViewById(R.id.main_material_text);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21)
                mainTextView.setTranslationZ(100);

                onSomeButtonClicked();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 21)
            mainTextView.setTranslationZ(10);
    }

    private void onSomeButtonClicked(){
        Intent intent = new Intent(MaterialMainActivity.this, SecondMaterialActivity.class);
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().setExitTransition(new Explode());
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else {
            startActivity(intent);
        }
    }
}
