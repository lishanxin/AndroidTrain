package com.example.androidtrain.permissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.hardware.Camera;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.AppOpsManagerCompat;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by sx on 2018/6/14.
 */

public enum PermissionManager {

    INSTANCE;

    /*
        例子
     */

    /*
    @ReactMethod
    public void openCamera(Callback callback){
        File file = new File(MainActivity.path);
        if (!file.exists()) {
            file.mkdir();
        }
        MainActivity.callback = callback;
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        fileName = String.valueOf(System.currentTimeMillis())+".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(MainActivity.path + fileName)));
        boolean checkCameraPermission = true;
        try {
            checkCameraPermission = PermissionManager.INSTANCE.isPermissionAllowed(getCurrentActivity(), AppOpsManager.OPSTR_CAMERA);
        } finally {
            if (checkCameraPermission){
                getReactApplicationContext().getCurrentActivity().startActivityForResult(intent,MainActivity.XT_CAMERA_REQUEST_CODE);
                Log.d(TAG, "CAMERA is allowed!");
            }else {
                PermissionManager.INSTANCE.showLocServiceDialog(getCurrentActivity(), "相机权限");
                Log.d(TAG, "CAMERA is disallowed!");
            }
        }
    }
    */

    //测试是否有该权限
    public boolean isPermissionAllowed(Context context, String opString){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            switch (opString){
                case AppOpsManager.OPSTR_CAMERA:
                    return cameraIsCanUse();
                case AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE:
                    return testFile(context).canWrite();
            }
        }else {
            if (AppOpsManagerCompat.MODE_ALLOWED != AppOpsManagerCompat.noteOp(context, opString,context.getApplicationInfo().uid,
                    context.getPackageName())){
                return false;
            };
        }
        return true;
    }

    public void showLocServiceDialog(final Activity activity, String permissionName){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(permissionName + "未开启，请至应用管理中开启该权限");
        /**
         * positive choice
         */
//        builder.setPositiveButton("去应用管理", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.setData(Uri.parse("package:" + activity.getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                activity.startActivity(intent);
//            }
//        });
        builder.setNegativeButton("确定", null);
        builder.show();
    }

    //优化相机权限判断，Api < 23时使用
    /**
     *  返回true 表示可以使用  返回false表示不可以使用
     */
    private boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * 测试读写权限而创建的文件
     * @param context application context
     * @return test file
     */
    public File testFile(Context context){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        return new File(path + "permissionTest.test");
    }
}
