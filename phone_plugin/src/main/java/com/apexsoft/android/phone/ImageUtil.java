package com.apexsoft.android.phone;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

	/**
	 * @Title: getImageRealSize
	 * @Description: 返回图片的实际宽高
	 * @param imageFileName
	 * @return
	 */
	public static int[] getImageRealSize(String imageFileName) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imageFileName, opt);
		return new int[] { opt.outWidth, opt.outHeight };
	}

	/**
	 * @Title: getSampleSize
	 * @Description: 获取压缩比
	 * @param realWidth
	 * @param realHeight
	 * @param width
	 * @param height
	 * @return
	 */
	private static int getSampleSize(int realWidth, int realHeight, int width, int height) {
		if (width <= 0 || height <= 0) {
			width = realWidth;
			height = realHeight;
		}
		float widthRate = ((float) realWidth) / width;
		float heightRate = ((float) realHeight) / height;

		if (widthRate < 0 && heightRate < 0) {
			return 1;
		}
		if (widthRate > heightRate) {
			return Math.round(widthRate);
		} else {
			return Math.round(heightRate);
		}
	}

	/**
	 * @Title: getBitmap
	 * @Description: 文件获取合合适大小图片
	 * @param imageFile
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(File imageFile, int width, int height) {
		return getBitmap(imageFile.getAbsolutePath(), width, height);
	}

	/**
	 * @Title: getBitmap
	 * @Description: 文件获取合合适大小图片
	 * @param imageFileName
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(String imageFileName, int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = false;
		int[] wh = getImageRealSize(imageFileName);
		opt.inSampleSize = getSampleSize(wh[0], wh[1], width, height);
		try {
			return BitmapFactory.decodeFile(imageFileName, opt);
		} catch (OutOfMemoryError e) {
//			LogUtils.e("getBitmap内存溢出",e);
			return null;
		}
	}

	/**
	 * 保存bitmap为文件（仅支持jpg和png）
	 * 
	 * @param bitmap
	 * @param desFile
	 * @param quality
	 * @return
	 * @throws IOException
	 */
	public static boolean saveBitmap2File(Bitmap bitmap, File desFile, int quality) throws IOException {
		if (desFile == null || bitmap == null) {
			return false;
		}
		if (!desFile.exists()) {
			FileUtil.createNewFile(desFile);
		}
		CompressFormat format = null;
		if (desFile.getPath().indexOf(".png") > -1) {
			format = CompressFormat.PNG;
		} else if (desFile.getPath().indexOf(".jpg") > -1 || desFile.getPath().indexOf(".jpeg") > -1) {
			format = CompressFormat.JPEG;
		} else {
			return false;
		}
		bitmap.compress(format, quality, new FileOutputStream(desFile));
		return true;
	}

	/**
	 * @Title: compressImageFile
	 * @Description: 图片问价压缩
	 * @param sourceFile
	 *            原图
	 * @param desFile
	 *            压缩图
	 * @param longFrame
	 *            长边框最大值
	 * @param shortFrame
	 *            短边框最大值
	 * @param quality
	 *            压缩质量 0-100
	 * @return
	 * @throws IOException
	 */
	public static boolean compressImageFile(File sourceFile, File desFile, int longFrame, int shortFrame, int quality) throws IOException {
		int[] wh = getImageRealSize(sourceFile.getAbsolutePath());
		Bitmap bitmap = null;
		if (wh[0] > wh[1]) {
			bitmap = getBitmap(sourceFile, longFrame, shortFrame);
		} else {
			bitmap = getBitmap(sourceFile, shortFrame, longFrame);
		}
		saveBitmap2File(bitmap, desFile, quality);
		if(bitmap!=null){
			bitmap.recycle();
		}
		return true;
	}

	/**
	 * 合成图片
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static Bitmap createBitmep(Bitmap b1, Bitmap b2) {
		if (b1 == null || b2 == null)
			return null;
		int w1 = b1.getWidth();
		int h1 = b1.getHeight();
		int h2 = b2.getHeight();
		Bitmap newb = Bitmap.createBitmap(w1, h1 + h2 + 2, Config.ARGB_8888);
		Canvas c = new Canvas(newb);
		c.drawBitmap(b1, 0, 0, null);
		c.drawBitmap(b2, 0, h1 + 2, null);
		c.save();
		c.restore();
		return newb;
	}

	/**
	 * 根据路径删除图片
	 *
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 根据文件删除图片
	 *
	 * @param file
	 */
	public static void deleteTempFile(File file) {
		if (file.exists()) {
			file.delete();
		}
	}
}
