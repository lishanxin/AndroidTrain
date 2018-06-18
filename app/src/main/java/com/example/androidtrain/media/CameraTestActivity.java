package com.example.androidtrain.media;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.androidtrain.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraTestActivity extends AppCompatActivity {

    private static final String TAG = "CameraTest";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView mImageView;
    File photoFile;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test);

        initView();
        dispatchTakePictureIntent();
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.camera_image);
    }

    /**
     * 注意在调用startActivityForResult()方法之前，先调用resolveActivity()，
     * 这个方法会返回能处理该Intent的第一个Activity
     * （译注：即检查有没有能处理这个Intent的Activity）。
     * 执行这个检查非常重要，因为如果在调用startActivityForResult()时，
     * 没有应用能处理你的Intent，应用将会崩溃。
     */
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            //Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }else {
            Log.d(TAG, "cannot use camera");
        }
    }

    /**
     * 若没有传递EXTRA_OUTPUT
     * Android的相机应用会把拍好的照片编码为缩小的Bitmap，
     * 使用extra value的方式添加到返回的Intent当中，
     * 并传送给onActivityResult()，对应的Key为"data"。
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            if (data != null){
                //获取拍照的缩略图
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageView.setImageBitmap(imageBitmap);

            }else {
                galleryAddPic();
                try {
                    setPic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //创建照片存储目录
    private File createImageFile() throws IOException{
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /*prefix*/
                ".jpg", /*suffix*/
                storageDir
        );

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //将照片添加到相册
    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Log.d(TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        Log.d(TAG, "contentUri.toString: " + contentUri.toString() + ";contentUri.path: " + contentUri.getPath());
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * 在有限的内存下，管理许多全尺寸的图片会很棘手。
     * 如果发现应用在展示了少量图片后消耗了所有内存，
     * 我们可以通过缩放图片到目标视图尺寸，之后再载入到内存中的方法，
     * 来显著降低内存的使用.仅影响显示图片，不影响原图。
     */
    private void setPic(){
        //Get the dimensions fo the View
        /*
        //由于用此种方法得到的width与height都为0，具体原因，请查看：https://stackoverflow.com/questions/3591784/views-getwidth-and-getheight-returns-0
        int targetW = mImageView.getMeasuredWidth();
        int targetH = mImageView.getMeasuredHeight();
        */
        //此处应该用ImageView的宽高替代
        int targetW = 600;
        int targetH = 600;

        //Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        //Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
