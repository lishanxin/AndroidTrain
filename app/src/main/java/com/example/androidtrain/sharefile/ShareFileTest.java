package com.example.androidtrain.sharefile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.androidtrain.R;

import junit.framework.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShareFileTest extends AppCompatActivity {


    private final static String TAG = "ShareFileTest";

    private File mPrivateRootDir;

    private File mImagesDir;

    File[] mImageFiles;

    String mImageFilenames;

    Intent mResultIntent;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file_test);

        setShare();
    }

    private void setShare() {

        mResultIntent = new Intent("com.example.androidtrain.ACTION_RETURN_FILE");
        mPrivateRootDir = Environment.getExternalStorageDirectory();
        mImagesDir = new File(mPrivateRootDir, "images");
        mImageFiles = mImagesDir.listFiles();

        setResult(Activity.RESULT_CANCELED, null);
    }



    public void getPath() {
        Log.d(TAG, getFilesDir().toString());
    }
}
