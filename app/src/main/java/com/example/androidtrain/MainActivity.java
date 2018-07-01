package com.example.androidtrain;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidtrain.connectApp.ConnectAppActivity;
import com.example.androidtrain.fragment.HeadlinesFragment;
import com.example.androidtrain.media.CameraTestActivity;
import com.example.androidtrain.media.ControlCameraActivity;
import com.example.androidtrain.media.ManagePlaybackActivity;
import com.example.androidtrain.media.SurfaceTest.SurfaceTestActivity;
import com.example.androidtrain.recyclerview.RecyclerViewActivity;
import com.example.androidtrain.sharefile.ShareFileTest;
import com.example.androidtrain.sql.SqlTestActivity;
import com.example.androidtrain.uistyle.UIStyleActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity  {

    public static final String TAG = "MainActivity";

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    //ShareActionProvider创建简便的分享功能
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAllPermission();
        storeImage();
    }

    /** Called when the user clicks the Send button */
    public void sendMessageActivity(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void sendMessageFragment(View view){
        Intent intent = new Intent(this, ShowExchangeFragment.class);
        intent.putExtra(EXTRA_MESSAGE, "from MainActivity");
        startActivity(intent);
    }

    public void goToSqlTest(View view){
        Intent intent = new Intent(this, SqlTestActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "from MainActivity");
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //为ActionBar扩展菜单项
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setTextShare();
//        Uri uri = getIntent().getData();
        return true;
    }

    //设置分享内容
    private void setShareIntent(Intent shareIntent){

        if (mShareActionProvider != null){
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void setImageShare(){
        AssetManager assetManager = getAssets();
        File file = new File(Environment.getExternalStorageDirectory() + "/aTrainImages/", "iutest.jpg");
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
        setShareIntent(imageIntent);
    }

    private void setTextShare(){
        Intent textIntent = new Intent();
        textIntent.setAction(Intent.ACTION_SEND);
        textIntent.putExtra(Intent.EXTRA_TEXT, "i am lizhizhi");
        textIntent.setType("text/plain");
        setShareIntent(textIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //处理动作按钮的点击事件
        switch (item.getItemId()){
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {
        Log.d(TAG, "action_settings");

    }

    private void openSearch() {

        Log.d(TAG, "action_search");

    }

    public void goToConnectOtherAppTest(View view) {
        Intent intent = new Intent(this, ConnectAppActivity.class);
        startActivity(intent);
    }






    private static final int REQUEST_PERMISSION_CODE = 101;

    private void checkAllPermission() {

        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE,
        };

        boolean isGranted = checkPermissionAllGranted(permissions);
        //只要有一项权限未通过，就重新请求权限；请求所有权限，对于已经授权的权限自动忽略
        if (isGranted){

        }else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE);
        }
    }

    private boolean checkPermissionAllGranted(String[] strings) {
        //对所有权限进行检查，若有一项未通过，则返回false
        for(String permission: strings){
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    public void goToShareFile(View view) {
        Intent intent = new Intent(this, ShareFileTest.class);
        startActivity(intent );
    }

    private void storeImage(){
        AssetManager assetManager = getAssets();
        File filePath = new File(getFilesDir(), "images");
        if (!filePath.exists()){
            filePath.mkdirs();
        }
        for (int i = 0; i< 10 ; i++){
            File file = new File(filePath.getAbsoluteFile(), "IMG"+ i + ".jpg");
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
        }
    }

    public void recyclerViewTest(View view) {
        Intent intent = new Intent(this, RecyclerViewActivity.class);
        startActivity(intent);
    }

    public void managePlayback(View view) {
        Intent intent = new Intent(this, ManagePlaybackActivity.class);
        startActivity(intent);
    }

    public void simpleCamera(View view) {
        Intent intent = new Intent(this, CameraTestActivity.class);
        startActivity(intent);
    }

    public void uiStyleTest(View view) {
        Intent intent = new Intent(this, UIStyleActivity.class);
        startActivity(intent);
    }

    public void controlCamera(View view) {
        Intent intent = new Intent(this, SurfaceTestActivity.class);
        startActivity(intent);
    }
}
