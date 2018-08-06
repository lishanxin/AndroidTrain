package com.example.androidtrain.buildingconnect.volleyTrain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.androidtrain.R;

public class VolleyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
    }

    public void volleyAction(View view) {
        switch (view.getId()){
            case R.id.start_simple_volley:
                break;
            case R.id.cancel_simple_volley:
                break;
            default:
                break;
        }
    }
}
