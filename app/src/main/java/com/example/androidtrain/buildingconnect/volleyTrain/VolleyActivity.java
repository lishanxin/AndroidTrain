package com.example.androidtrain.buildingconnect.volleyTrain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.androidtrain.R;

public class VolleyActivity extends AppCompatActivity {

    ImageView volleyImage;
    NetworkImageView volleyNetworkImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        volleyImage = (ImageView)findViewById(R.id.volley_show_image);
        volleyNetworkImage = (NetworkImageView) findViewById(R.id.activity_volley_network_image);
    }

    public void volleyAction(View view) {
        switch (view.getId()){
            case R.id.start_simple_volley:
                new SimpleRequestDemo(this, volleyImage, volleyNetworkImage)
                    .testNetworkImageView();
                break;
            case R.id.cancel_simple_volley:
                break;
            default:
                break;
        }
    }
}
