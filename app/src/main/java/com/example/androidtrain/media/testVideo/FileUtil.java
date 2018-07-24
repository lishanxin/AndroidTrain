package com.example.androidtrain.media.testVideo;

/**
 * Created by user on 2017/3/7.
 */


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: 文件处理工具类
 *
 * @Author zhengjin
 * @Date 2014-1-29 下午2:29:00
 * @Version 1.0
 */
public class FileUtil {
    /**
     * @Title: existSDcard
     * @Description: SD开是否可用
     * @return
     */
    public static boolean existSDcard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Title: getTempFolder
     * @Description: 临时文件目录
     * @return
     */
    public static File getTempFolder(Context context) {
        File tempFolder;
        String tempFolderName = "temp_" + context.getPackageName().replace(".", "_");
        if (FileUtil.existSDcard()) {
            tempFolder = new File(Environment.getExternalStorageDirectory(), tempFolderName);
        } else {
            tempFolder = new File(context.getFilesDir(), tempFolderName);
        }
        if (!tempFolder.exists()) {
            boolean createdResult = tempFolder.mkdirs();
        }
        return tempFolder;
    }

    /**
     * @Title: clearTempFolder
     * @Description: 清空临时文件
     */
    public static void clearTempFolder(Context context) {
        File folder = getTempFolder(context);
        File[] files = folder.listFiles();
        if(files==null){
            return;
        }
        for (File file : files) {
            file.delete();
        }
    }

    /**
     * @Title: saveFile
     * @Description: 保存文件
     * @param filename
     * @param is
     * @return
     * @throws IOException
     */
    public static File saveFile(String filename, InputStream is) throws IOException {
        File file = new File(filename);
        FileUtil.createNewFile(file);
        FileOutputStream fos = new FileOutputStream(file);
        try {
            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            is.close();
        }
        return file;
    }

    public static File createNewFile(File folder, String filename) throws IOException {
        File file = new File(folder, filename);
        createNewFile(file);
        return file;
    }

    public static File createNewFile(File file) throws IOException {
        File parent=file.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /*
     * Uri转String
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
