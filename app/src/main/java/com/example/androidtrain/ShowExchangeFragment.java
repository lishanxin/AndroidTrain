package com.example.androidtrain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.androidtrain.fragment.HeadlinesFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowExchangeFragment extends FragmentActivity implements HeadlinesFragment.OnHeadlinesSelectedListener{

    private static final String TAG = "ShowExchangeFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_article);

        //文件存储测试SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("haha", Context.MODE_PRIVATE);
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("inttest", 22);
        editor.commit();
        int inttest = sharedPreferences.getInt("haha", -1);


        //文件存储测试Internal Storage，External storage
        testFileStore();

        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return;
            }

            HeadlinesFragment firstFragment = new HeadlinesFragment();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }

    private void testFileStore() {
        Log.d(TAG, "getFilesDir()" + getFilesDir().getAbsolutePath());
        Log.d(TAG, "getCacheDir()" + getCacheDir().getAbsolutePath());

        String filename = "myfile";
        String string = "Hello World!";
        FileOutputStream outputStream;

        try{
            //将文件写到internal目录中
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private File getTempFile(Context context, String url){
        File file = null;
        try{
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        }catch (IOException e){
            e.printStackTrace();
        }

        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

    //public文件创建
    public File getAlbumStorageDir(String albumName){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()){
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    //private文件创建
    public File getAlbumStorageDir(Context context, String albumName){
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()){
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onArticleSelected(int position) {
        Log.d(TAG, position + "");
    }
}
