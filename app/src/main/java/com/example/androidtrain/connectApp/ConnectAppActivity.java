package com.example.androidtrain.connectApp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.androidtrain.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void callPhone(View view) {
        Uri number = Uri.parse("tel:5551234");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }


    public void checkMap(View view) {
        Uri location = Uri.parse("geo:120,30");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void checkWebPage(View view) {
        Uri location = Uri.parse("http://www.baidu.com");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void sendImage(View view) {
        AssetManager assetManager = getAssets();
        File file = new File(Environment.getExternalStorageDirectory(), "iutest.jpg");
        try {
            InputStream in = assetManager.open("iu.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
        Uri location = Uri.parse("file://" + file.getAbsolutePath());

        Intent imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_SEND);
        imageIntent.putExtra(Intent.EXTRA_STREAM, location);
        imageIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(imageIntent, "打开图片"));
    }

}
