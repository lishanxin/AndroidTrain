package com.example.androidtrain.uiDesign.createView;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.androidtrain.R;

public class CreateViewActivity extends AppCompatActivity {
    private static final String TAG = "CreateView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_view);

        Uri uri = getIntent().getData();
        if (uri != null){
            System.out.print(uri.toString());
            Log.d(TAG, "aaa1----" + uri.toString());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null){
            System.out.print(uri.toString());
            Log.d(TAG, "bbb2----" + uri.toString());
        }
    }
}
