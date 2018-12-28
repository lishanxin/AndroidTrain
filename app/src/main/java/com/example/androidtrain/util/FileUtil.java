package com.example.androidtrain.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.androidtrain.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by sx on 2018/12/28.
 */

public class FileUtil {
    /**
     * Base64数据转换为Jpg格式的文件
     * @param base64 Base64的数据
     * @param fileName 保存后的文件名
     * @return
     */
    private String saveBase64ToImage(String base64, String fileName) {
        File dir = new File(MainActivity.path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        FileOutputStream out = null;
        File file = new File(dir, fileName);
        try {
            byte[] decode = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 将base64文件写入File中
     * @param base64
     * @param fileName
     * @return
     */
    private String saveBase64ToFile(String base64, String fileName){
        File dir = new File(MainActivity.path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        // 创建文件对象
        File file = new File(dir, fileName);
        try
        {
            // 向文件写入对象写入信息
            FileWriter fileWriter = new FileWriter(file);

            // 写文件
            fileWriter.write(base64);
            // 关闭
            fileWriter.close();
        }
        catch (IOException e)
        {
            //
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}
