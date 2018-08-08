package com.example.androidtrain.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androidtrain.R;

//Gms由于api更新，无法根据AndroidTrain来学习
public class LocationActivity extends AppCompatActivity {

    TextView locationText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
    }

    public void locationButton(View view) {
        switch (view.getId()){
            case R.id.button_current_location:
                break;
        }
    }


}
