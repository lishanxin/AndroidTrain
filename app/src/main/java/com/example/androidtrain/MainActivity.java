package com.example.androidtrain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidtrain.buildingconnect.connectwirelessly.ConnectNSDActivity;
import com.example.androidtrain.buildingconnect.connectwirelessly.WifiDirectActivity;
import com.example.androidtrain.buildingconnect.networkOps.NetWorkOpsActivity;
import com.example.androidtrain.buildingconnect.volleyTrain.VolleyActivity;
import com.example.androidtrain.connectApp.ConnectAppActivity;
import com.example.androidtrain.media.CameraTestActivity;
import com.example.androidtrain.media.ManagePlaybackActivity;
import com.example.androidtrain.media.SurfaceTest.SurfaceTestActivity;
import com.example.androidtrain.media.testVideo.VideoActivity;
import com.example.androidtrain.media.testVideo.VideoAgent;
import com.example.androidtrain.pictureAnimation.EffectiveBitmapActivity;
import com.example.androidtrain.pictureAnimation.animation.CrossfadeActivity;
import com.example.androidtrain.pictureAnimation.cardflip.CardFlipActivity;
import com.example.androidtrain.pictureAnimation.layoutchange.LayoutChangesActivity;
import com.example.androidtrain.pictureAnimation.opengles.OpenGLES20Activity;
import com.example.androidtrain.pictureAnimation.bitmaptest.ImageDetailActivity;
import com.example.androidtrain.pictureAnimation.screenslide.ScreenSlideActivity;
import com.example.androidtrain.pictureAnimation.zoom.ZoomActivity;
import com.example.androidtrain.print.PrintPhotoActivity;
import com.example.androidtrain.recyclerview.RecyclerViewActivity;
import com.example.androidtrain.sharefile.ShareFileTest;
import com.example.androidtrain.sql.SqlTestActivity;
import com.example.androidtrain.textReader.TextReaderActivity;
import com.example.androidtrain.uiDesign.createView.CreateViewActivity;
import com.example.androidtrain.uistyle.UIStyleActivity;
import com.example.androidtrain.userExperience.designNavigation.CollectionDemoActivity;
import com.example.androidtrain.userExperience.designNavigation.NavDrawerActivity;
import com.example.androidtrain.userExperience.designNavigation.UpNavigatorActivity;
import com.example.androidtrain.userExperience.notifyUser.BigNotificationActivity;
import com.example.androidtrain.userExperience.notifyUser.NotificationBuilderActivity;
import com.example.androidtrain.userExperience.notifyUser.NotificationNavigatorActivity;
import com.example.androidtrain.userExperience.notifyUser.ProgressNotificationActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity  {

    public static final String TAG = "MainActivity";

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    //ShareActionProvider创建简便的分享功能
    private ShareActionProvider mShareActionProvider;

    public static String path =Environment.getExternalStorageDirectory() + "/androidTrain/";
    public static String fileName ;

    public static int count = 0;


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
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS,

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

    public void printTest(View view) {
        Intent intent = new Intent(this, PrintPhotoActivity.class);
        startActivity(intent);
    }

    public void effectBitmap(View view) {
        goToActivity(EffectiveBitmapActivity.class);
    }
    public void uiBitmap(View view) {
        goToActivity(ImageDetailActivity.class);
    }

    public void OPENGL(View view) {
        goToActivity(OpenGLES20Activity.class);
    }

    public void Viewfade(View view) {
        goToActivity(CrossfadeActivity.class );
    }

    public void screenSlideViewPager(View view) {
        goToActivity(ScreenSlideActivity.class);
    }


    public void cardFlip(View view) {
        goToActivity(CardFlipActivity.class);
    }

    public void zoomAnimation(View view) {
        goToActivity(ZoomActivity.class);
    }

    public void layoutChangesAnimate(View view) {
        goToActivity(LayoutChangesActivity.class);
    }
    public void videoMediaTest(View view) {
        VideoAgent.OnCameraCapture listener = new VideoAgent.OnCameraCapture() {

            @Override
            public void loadMedia(File file) {
                Toast.makeText(MainActivity.this, file.getAbsolutePath()+"-小大:"+(file.length()/1024)+"K", Toast.LENGTH_SHORT).show();


            }
        };
        File file = new File(MainActivity.path);
        if (!file.exists()) {
            file.mkdir();
        }
//        Intent intent = new Intent();
//        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
////        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,10 * 1024 * 1024L);
//        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,maxRecordVideoTime);
        MainActivity.fileName = String.valueOf(System.currentTimeMillis()) + ".mp4";
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(MainActivity.path + MainActivity.fileName)));


//        Intent intent = new Intent(getReactApplicationContext(), MainVideoActivity.class);
//        getReactApplicationContext().getCurrentActivity().startActivityForResult(intent, MainActivity.XT_VIDEO_REQUEST_CODE);


        VideoAgent va = new VideoAgent(VideoActivity.class);
        va.setQuality(CamcorderProfile.QUALITY_CIF);
        va.setMaxTime(1800);
        try {
            String filePath = MainActivity.path + MainActivity.fileName;
            va.startCamera(this, new File(filePath), listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectNSD(View view) {
        goToActivity(ConnectNSDActivity.class);
    }

    public void connectWifiDirect(View view) {
        goToActivity(WifiDirectActivity.class);
    }
    private void goToActivity(Class cl){
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }

    public void textReader(View view) {
        goToActivity(TextReaderActivity.class);
    }

    public void netWork(View view) {
        goToActivity(NetWorkOpsActivity.class);
    }

    public void volleyTest(View view) {
        goToActivity(VolleyActivity.class);
    }

    public void navSwipeView(View view) {
        goToActivity(CollectionDemoActivity.class);
    }

    public void navDrawer(View view) {
        goToActivity(NavDrawerActivity.class);
    }

    public void upNavigator(View view) {
        goToActivity(UpNavigatorActivity.class);
    }

    public void notificationBuilder(View view) {
        goToActivity(NotificationBuilderActivity.class);
    }

    public void notificationNavigator(View view) {
        goToActivity(NotificationNavigatorActivity.class);
    }

    public void notificationBigView(View view) {
        goToActivity(BigNotificationActivity.class);
    }

    public void notificationProgress(View view) {
        goToActivity(ProgressNotificationActivity.class);
    }

    public void createView(View view) {
        goToActivity(CreateViewActivity.class);
    }
}
