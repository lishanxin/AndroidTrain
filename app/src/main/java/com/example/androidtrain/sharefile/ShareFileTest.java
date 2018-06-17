package com.example.androidtrain.sharefile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.androidtrain.R;
import com.example.androidtrain.recyclerview.MainAdapter;

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

    private RecyclerView mMainRecyclerView;
    private List<String> mList;

    private RecyclerView.LayoutManager mLayoutManager;

    private File mPrivateRootDir;

    private File mImagesDir;

    File[] mImageFiles;

    String mImageFilenames;

    Intent mResultIntent;

    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file_test);

        setShare();
        initView();
        initAdapter();
    }

    private void setShare() {
        mResultIntent = new Intent("com.example.androidtrain.ACTION_RETURN_FILE");
        mPrivateRootDir = getFilesDir();
        mImagesDir = new File(mPrivateRootDir, "images");
        mImageFiles = mImagesDir.listFiles();


    }

    //初始化控件
    private void initView() {
        mMainRecyclerView = (RecyclerView) findViewById(R.id.share_recycler_view);
    }

    //初始化Adapter
    private void initAdapter(){
        //设置布局管理器
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<File> files = Arrays.asList(mImageFiles);
        ShareAdapter shareAdapter = new ShareAdapter(files);
        shareAdapter.setOnItemClickListener(new ShareAdapter.OnItemClickListener() {
            @Override
            public void onClick(ShareAdapter.ViewHolder viewHolder, int position) {
                try {
                    /*官方
                     */
                    File requestFile = files.get(position);
                    try {
                        fileUri =  FileProvider.getUriForFile(ShareFileTest.this, "com.example.androidtrain.fileprovider", requestFile);
                    } catch (IllegalArgumentException e) {
                        Log.e("File Selector",
                                "The selected file can't be shared: " +
                                        requestFile.getAbsolutePath());
                        e.printStackTrace();
                    }

                    if (fileUri != null){
                        mResultIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        mResultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
                        ShareFileTest.this.setResult(Activity.RESULT_OK, mResultIntent);
                    }else {
                        mResultIntent.setDataAndType(null, "");
                        ShareFileTest.this.setResult(RESULT_CANCELED,
                                mResultIntent);
                    }
                } catch (Exception e) {
                    mResultIntent.setDataAndType(null, "");
                    setResult(Activity.RESULT_CANCELED, mResultIntent);
                    e.printStackTrace();
                }
                finish();
            }
        });
        mMainRecyclerView.setAdapter(shareAdapter);
    }

    public void getPath() {
        Log.d(TAG, getFilesDir().toString());
    }
}
