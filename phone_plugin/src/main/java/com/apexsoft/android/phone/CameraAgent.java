package com.apexsoft.android.phone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CameraAgent {
	public final static String ACTION_IMAGE_CAPTURE_INTENT = "android.apexsoft.media.action.IMAGE_CAPTURE";
	public static final int ACTION_IMAGE_CAPTURE = 3000;
	public final static int FILE_SELECTED = 4;
	public static final int ACTION_IMAGE_CAPTURE_SUCCESS = 100;
	public static final int ACTION_IMAGE_CAPTURE_FAIL = 200;
	public static final int CROP = 1;
	public static final String IMAGE_WIDTH_RATE = "IMAGE_WIDTH_RATE";
	public static final String IMAGE_HEIGHT_RATE = "IMAGE_HEIGHT_RATE";
	public static final String IMAGE_TAKE_TIME = "IMAGE_TAKE_TIME";
	public static final String IMAGE_TIP = "IMAGE_TIP";

	public static final String IMAGE_REPAIRE_DEGREE = "IMAGE_REPAIRE_DEGREE";
	public static final String IMAGE_CROP_CAMERA = "IMAGE_CROP_CAMERA";
	public static final String IMAGE_CROP_MaxWidth = "IMAGE_CROP_MaxWidth";
	public static final String IMAGE_CROP_MaxHeight = "IMAGE_CROP_MaxHeight";
	public static final String IMAGE_CROP_Quality = "IMAGE_CROP_Quality";
	public static final String IMAGE_CanRotaion = "IMAGE_CanRotaion";

	File tempFile;
	File destFile;
	private Activity context;
	private android.app.Fragment fragment;
	private android.support.v4.app.Fragment fragmentv4;
	private OnCameraCapture listener;
	Bundle data = new Bundle();

	private int taketime = 1;
	private float imageWidthRate = 0.5f;
	private float imageHeightRate = 0.5f;
	private String cameraTip = null;
	private boolean repaireDegree = true;
	private boolean cropCamera = false;
	private int MaxWidthPx = 1000;
	private int MaxHeightPx = 1000;
	private int quality = 100;
	private boolean canRotaion=false;
	private boolean isSelect = false;
	private CordovaPlugin plugin;



	/**
	 * @Title: setCameraConfig
	 * @Description: TODO
	 * @param taketime
	 *            2:拍摄两张
	 * @param imageWidthRate
	 * @param imageHeightRate
	 */
	public void setCameraConfig(Integer taketime, Float imageWidthRate, Float imageHeightRate) {
		if (taketime != null && (taketime == 2 || taketime == 1)) {
			this.taketime = taketime;
		}
		if (imageWidthRate != null) {
			this.imageWidthRate = imageWidthRate;
		}
		if (imageHeightRate != null) {
			this.imageHeightRate = imageHeightRate;
		}
	}

	public void setCameraTip(String cameraTip) {
		this.cameraTip = cameraTip;
	}

	public void repaireDegree(boolean repaireDegree) {
		this.repaireDegree = repaireDegree;
	}

	public void cropCamera(boolean cropCamera) {
		this.cropCamera = cropCamera;
	}
	public void rotaionCamera(boolean canRotaion) {
		this.canRotaion = canRotaion;
	}
	public void setImageQuality(int quality) {
		this.quality = quality>100?100:quality;
	}
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	/**
	 * 请采用cropFixedSize
	 * @param width
	 * @param height
	 */
	@Deprecated
	public void cropCameraSize(int width, int height) {
		this.MaxHeightPx = height;
		this.MaxWidthPx = width;
	}


	/**
	 * 截图宽高修正，有些相机按绿框的宽高比截图，存在过高，过宽的问题</br>
	 * 采用参数修正，负数，基于比例，减少指定像素，正数是基于比例增加像素
	 * @param width
	 * @param height
	 */
	public void cropFixedSize(int width, int height) {
		this.MaxHeightPx = height;
		this.MaxWidthPx = width;
	}


	public void startCamera(Activity activity, final File destFile, final OnCameraCapture listener) throws IOException {
		recovery(null,activity, null, null, destFile, listener);
		start();
	}
	public void startCamera(CordovaPlugin plugin, final File destFile, final OnCameraCapture listener) throws IOException {
		recovery(plugin,null, null, null, destFile, listener);
		start();
	}


	public void startCamera(Activity activity, android.app.Fragment fragment, final File destFile, final OnCameraCapture listener) throws IOException {
		recovery(null,activity, fragment, null, destFile, listener);
		start();
	}

	public void startCamera(Activity activity, android.support.v4.app.Fragment fragment, final File destFile, final OnCameraCapture listener) throws IOException {
		recovery(null,activity, null, fragment, destFile, listener);
		start();
	}

	public void recovery(CordovaPlugin cordovaPlugin,Activity activity, android.app.Fragment fragment, android.support.v4.app.Fragment fragmentv4, final File destFile, final OnCameraCapture listener) {
		this.destFile = destFile;
		this.context = activity;
		this.fragment = fragment;
		this.fragmentv4 = fragmentv4;
		this.listener = listener;
		this.plugin=cordovaPlugin;
		if(cordovaPlugin!=null){
			this.context=cordovaPlugin.cordova.getActivity();
		}
		try {
			tempFile = FileUtil.createNewFile(FileUtil.getTempFolder(context), "temp.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void start() {
		takePhoto();
//		if(isSelect()){
//
//			final AlertDialog dialog = new AlertDialog.Builder(context).create();
//			dialog.show();
//			Window win = dialog.getWindow();
//			win.setContentView(R.layout.select_alert);
//			TextView camera = (TextView) win.findViewById(R.id.camera);
//			TextView photo = (TextView) win.findViewById(R.id.photo);
//			TextView cancel = (TextView) win.findViewById(R.id.cancel);
//			camera.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					takePhoto();
//					dialog.dismiss();
//				}
//			});
//
//			photo.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					selectPicture();
//					dialog.dismiss();
//				}
//			});
//
//			cancel.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//
//		}else{
//			takePhoto();
//		}
	}

	// 拍照上传
	private void takePhoto(){
		Intent intent = new Intent(context, CameraActivity.class);
		Uri uri = Uri.fromFile(tempFile);
		data.putParcelable(MediaStore.EXTRA_OUTPUT, uri);
		data.putFloat(IMAGE_WIDTH_RATE, imageWidthRate);
		data.putFloat(IMAGE_HEIGHT_RATE, imageHeightRate);
		data.putInt(IMAGE_TAKE_TIME, taketime);
		data.putString(IMAGE_TIP, cameraTip);
		data.putBoolean(IMAGE_REPAIRE_DEGREE, repaireDegree);
		data.putBoolean(IMAGE_CROP_CAMERA, cropCamera);
		data.putInt(IMAGE_CROP_MaxWidth, MaxWidthPx);
		data.putInt(IMAGE_CROP_MaxHeight, MaxHeightPx);
		data.putInt(IMAGE_CROP_Quality, quality);
		data.putBoolean(IMAGE_CanRotaion, canRotaion);
		intent.putExtras(data);
		if (fragment != null) {
			fragment.startActivityForResult(intent, ACTION_IMAGE_CAPTURE);
		} else if (fragmentv4 != null) {
			fragmentv4.startActivityForResult(intent, ACTION_IMAGE_CAPTURE);
		}
		else if(plugin!=null){
			plugin.cordova.startActivityForResult(plugin,intent,ACTION_IMAGE_CAPTURE);
		}else {
			context.startActivityForResult(intent, ACTION_IMAGE_CAPTURE);
		}
	}

	//选择图片
	private void selectPicture(){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");

		if (fragment != null) {
			fragment.startActivityForResult(Intent.createChooser(intent, "选择图片"),FILE_SELECTED);
		} else if (fragmentv4 != null) {
			fragmentv4.startActivityForResult(Intent.createChooser(intent, "选择图片"),FILE_SELECTED);
		} else {
			context.startActivityForResult(Intent.createChooser(intent, "选择图片"),FILE_SELECTED);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) throws FileNotFoundException, IOException {
		if (resultCode == Activity.RESULT_CANCELED) {

		} else {
			switch (requestCode) {
				case ACTION_IMAGE_CAPTURE:
					if (resultCode == ACTION_IMAGE_CAPTURE_SUCCESS) {
						FileUtil.saveFile(destFile.getAbsolutePath(), new FileInputStream(tempFile));
//					LogUtils.d("截图文件已创建[" + destFile.getAbsolutePath() + "]");
						if (listener != null) {
							listener.loadPicture(destFile);
						}
					} else {
						Toast.makeText(context, "拍摄失败", Toast.LENGTH_LONG).show();
					}

					break;

//			case FILE_SELECTED:
//				Uri result = data == null || resultCode != android.app.Activity.RESULT_OK  ? null
//						: data.getData();
//
//				String uriPath = FileUtil.getRealPathFromURI(
//						context, result);
//				File file =new File(uriPath);
//
//				if (listener != null) {
//					listener.loadPicture(file);
//				}
//				break;
				default:
					break;
			}
		}
	}

	public interface OnCameraCapture {
		public void loadPicture(File file);
	}
}
