//package com.example.androidtrain.media.SurfaceTest;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.res.Configuration;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.ImageFormat;
//import android.graphics.Matrix;
//import android.graphics.PixelFormat;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.hardware.Camera;
//import android.hardware.Camera.AutoFocusCallback;
//import android.hardware.Camera.CameraInfo;
//import android.hardware.Camera.Parameters;
//import android.hardware.Camera.PictureCallback;
//import android.hardware.Camera.Size;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@SuppressLint("NewApi")
//public class CameraActivity extends Activity implements OnClickListener {
//
//	/** 相机 */
//	private Camera camera;
//	/** 相机参数 */
//	private Parameters parameters = null;
//	/** 框架布局器 */
//	private SurfaceView surfaceView;
//	private ImageView iv;
//	private MaskView maskView;
//	/** 照相 */
//	private Button takepicture;
//	/** 照相 */
//	private Button takepictureZm;
//	/** 照相 */
//	private Button takepictureFm;
//	/** 确定 */
//	private Button makesure;
//	/** 确定 */
//	private Button makemerge;
//	/** 重拍 */
//	private Button retakepicture;
//	/** 重拍 */
//	private Button makecancle;
//	/** 旋转 */
//	private Button rotaion;
//	private TextView cameraTip;
//
//	private TextView cameraTip1;
//
//	private TextView cameraTip2;
//	/** 返回的结果代码 */
//	public final static int RESULT_CODE = 5;
//
//	private float imageWidthRate = 0.5f;
//	private float imageHeightRate = 0.5f;
//	/** 聚焦 */
//	private AutoFocusCallback mAutoFocusCallback;
//
//	private Uri destUri = null;
//	private int taketime = 1;
//	private int takeIndex = 0;
//	private boolean cropCamera = false;
//	private boolean canRotaion = false;
//	Bitmap[] bNew;
//	private String cameraTipText = null;
//	private String cameraTip1Text = null;
//	private String cameraTip2Text = null;
//	private static int MaxWidthPx = 1000;
//	private static int MaxHeightPx = 1000;
//	private static boolean repaireDegree = true;
//	private static int quality = 100;
//	private String mCardType = "身份证";
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.android_camera);
//		Bundle bundle = getIntent().getExtras();
//		if (bundle != null) {
//			destUri = bundle.getParcelable(MediaStore.EXTRA_OUTPUT);
//			imageWidthRate = bundle.getFloat(CameraAgent.IMAGE_WIDTH_RATE);
//			imageHeightRate = bundle.getFloat(CameraAgent.IMAGE_HEIGHT_RATE);
//			taketime = bundle.getInt(CameraAgent.IMAGE_TAKE_TIME);
//			bNew = new Bitmap[taketime];
//			cameraTipText = bundle.getString(CameraAgent.IMAGE_TIP);
//			repaireDegree = bundle.getBoolean(CameraAgent.IMAGE_REPAIRE_DEGREE, true);
//			cropCamera = bundle.getBoolean(CameraAgent.IMAGE_CROP_CAMERA, false);
//			MaxWidthPx = bundle.getInt(CameraAgent.IMAGE_CROP_MaxWidth, MaxWidthPx);
//			MaxHeightPx = bundle.getInt(CameraAgent.IMAGE_CROP_MaxHeight, MaxHeightPx);
//			quality = bundle.getInt(CameraAgent.IMAGE_CROP_Quality, quality);
//			canRotaion = bundle.getBoolean(CameraAgent.IMAGE_CanRotaion, canRotaion);
//			mCardType = bundle.getString(CameraAgent.IMAGE_CARD_TYPE, mCardType);
//		}
//		layoutInit();
//		mAutoFocusCallback = new AutoFocusCallback() {
//			@Override
//			public void onAutoFocus(boolean success, Camera c) {
//				if (success) {
//					camera.setOneShotPreviewCallback(null);
//				}
//			}
//		};
//		showPz(true);
//	}
//
//	private void showPz(boolean isShow) {
//		if (isShow) {
//			if (taketime == 2) {
//				takepictureFm.setVisibility(View.VISIBLE);
//				takepictureZm.setVisibility(View.VISIBLE);
//				takepicture.setVisibility(View.GONE);
//				if (bNew[0] != null && bNew[1] != null) {
//					makemerge.setVisibility(View.VISIBLE);
//				} else {
//					makemerge.setVisibility(View.GONE);
//				}
//			} else {
//				takepictureFm.setVisibility(View.GONE);
//				takepictureZm.setVisibility(View.GONE);
//				takepicture.setVisibility(View.VISIBLE);
//				makemerge.setVisibility(View.GONE);
//			}
//			retakepicture.setVisibility(View.GONE);
//			makesure.setVisibility(View.GONE);
//			rotaion.setVisibility(View.GONE);
//		} else {
//			takepictureFm.setVisibility(View.GONE);
//			takepictureZm.setVisibility(View.GONE);
//			takepicture.setVisibility(View.GONE);
//		}
//
//	}
//
//	@SuppressWarnings("deprecation")
//	private void layoutInit() {
//
//		surfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);
//		surfaceView.getHolder().setSizeFromLayout();
//		surfaceView.getHolder().setKeepScreenOn(true);
//		surfaceView.getHolder().addCallback(new SurfaceCallback());
//		surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);// translucent半透明
//																	// transparent透明
//		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		iv = (ImageView) findViewById(R.id.camera_image);
//		iv.setOnClickListener(this);
//		takepicture = (Button) findViewById(R.id.camera_take);
//		takepicture.setOnClickListener(this);
//		takepictureZm = (Button) findViewById(R.id.camera_take_sfzzm);
//		takepictureZm.setOnClickListener(this);
//		takepictureFm = (Button) findViewById(R.id.camera_take_sfzfm);
//		takepictureFm.setOnClickListener(this);
//		makesure = (Button) findViewById(R.id.camera_sure);
//		makesure.setOnClickListener(this);
//		makemerge = (Button) findViewById(R.id.camera_merge);
//		makemerge.setOnClickListener(this);
//		retakepicture = (Button) findViewById(R.id.camera_retake);
//		retakepicture.setOnClickListener(this);
//		makecancle = (Button) findViewById(R.id.camera_cacel);
//		makecancle.setOnClickListener(this);
//		rotaion = (Button) findViewById(R.id.camera_rotaion);
//		rotaion.setOnClickListener(this);
//		maskView = (MaskView) findViewById(R.id.view_mask);
//		cameraTip = (TextView) findViewById(R.id.camera_tip);
//		cameraTip1 = (TextView) findViewById(R.id.camera_tip1);
//		cameraTip2 = (TextView) findViewById(R.id.camera_tip2);
//		if (cameraTipText != null && !"null".equals(cameraTipText) && !"".equals(cameraTipText)) {
//			cameraTip.setText(cameraTipText);
//		}
//		if (mCardType!=null){
//			cameraTip1.setText("请横握手机拍摄，将" +  mCardType + "置于绿色框内");
//			cameraTip2.setText("请保持" + mCardType + "内容清晰");
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		if (camera != null) {
//			int id = v.getId();
//			if (id == R.id.camera_take) {
//				try {
//					camera.autoFocus(new AutoFocusCallback() {
//
//						@Override
//						public void onAutoFocus(boolean success, Camera camera) {
//							if (success) {
//								camera.setOneShotPreviewCallback(null);
//							}
//							showPz(false);
////							NotificationManager mgr = (NotificationManager) CameraActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
////							Notification nt = new Notification();
////							Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(R.raw.camera_km) + "/"
////									+ getResources().getResourceTypeName(R.raw.camera_km) + "/" + getResources().getResourceEntryName(R.raw.camera_km));
////							LogUtils.d(uri.toString());
////							nt.sound = uri;
////							int soundId = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
////							mgr.notify(soundId, nt);
//
//							try {
//								camera.takePicture(null, null, new MyPictureCallback());
//							}catch (RuntimeException e){
//								e.printStackTrace();
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							// hideView();
//
//						}
//					});
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//添加拍照声音
//				/*final SoundPool pool =new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
//				 final int sourceid = pool.load(CameraActivity.this, R.raw.camera_click, 0);
//				 pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//
//			           public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//			                    // TODO Auto-generated method stub
//			        	   pool.play(sourceid, 2, 2, 0, 0, 1);
//			           }
//			     });*/
//
//			} else if (id == R.id.camera_take_sfzzm) {
//				try {
//					camera.autoFocus(new AutoFocusCallback() {
//
//						@Override
//						public void onAutoFocus(boolean success, Camera camera) {
//							if (success) {
//								camera.setOneShotPreviewCallback(null);
//							}
//							takeIndex = 0;
//							showPz(false);
//							camera.takePicture(null, null, new MyPictureCallback());
//							// hideView();
//
//						}
//					});
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			} else if (id == R.id.camera_take_sfzfm) {
//				try {
//					camera.autoFocus(new AutoFocusCallback() {
//
//						@Override
//						public void onAutoFocus(boolean success, Camera camera) {
//							if (success) {
//								camera.setOneShotPreviewCallback(null);
//							}
//							takeIndex = 1;
//							showPz(false);
//							camera.takePicture(null, null, new MyPictureCallback());
//							// hideView();
//
//						}
//					});
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			} else if (id == R.id.camera_image) {
//				// 拍摄时自动对焦，不启用这个
//				try {
//					camera.autoFocus(mAutoFocusCallback);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} else if (id == R.id.camera_retake) {
//				showPz(true);
//				reTakePicture();
//			} else if (id == R.id.camera_cacel) {
//				setResult(Activity.RESULT_CANCELED);
//				finish();
//			} else if (id == R.id.camera_sure && taketime == 1) {
//				try {
//					boolean result = false;
//
//					if (taketime == 2) {
//						result = ImageUtil.saveBitmap2File(ImageUtil.createBitmep(bNew[0], bNew[1]), new File(destUri.getPath()), quality);
//					} else {
//						result = ImageUtil.saveBitmap2File(bNew[takeIndex], new File(destUri.getPath()), quality);
//					}
//					if (result) {
//						setResult(CameraAgent.ACTION_IMAGE_CAPTURE_SUCCESS);
//					} else {
//						setResult(CameraAgent.ACTION_IMAGE_CAPTURE_FAIL);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//					setResult(CameraAgent.ACTION_IMAGE_CAPTURE_FAIL);
//
//				}
//				finish();
//			} else if (id == R.id.camera_merge && taketime == 2) {
//				try {
//					boolean result = false;
//
//					if (taketime == 2) {
//						result = ImageUtil.saveBitmap2File(ImageUtil.createBitmep(bNew[0], bNew[1]), new File(destUri.getPath()), quality);
//					} else {
//						result = ImageUtil.saveBitmap2File(bNew[takeIndex], new File(destUri.getPath()), quality);
//					}
//					if (result) {
//						setResult(CameraAgent.ACTION_IMAGE_CAPTURE_SUCCESS);
//					} else {
//						setResult(CameraAgent.ACTION_IMAGE_CAPTURE_FAIL);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//					setResult(CameraAgent.ACTION_IMAGE_CAPTURE_FAIL);
//
//				}
//				finish();
//			} else if (id == R.id.camera_sure && taketime == 2) {
//				showPz(true);
//				reTakePicture();
//			} else if (id == R.id.camera_rotaion) {
//				bNew[takeIndex] = adjustPhotoRotation(bNew[takeIndex], 90);
//				iv.setImageBitmap(bNew[takeIndex]);
//			}
//
//		}
//	}
//
//	private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
//
//		Matrix m = new Matrix();
//		m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//		try {
//			Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
//			return bm1;
//
//		} catch (OutOfMemoryError ex) {
//		}
//		return null;
//
//	}
//
//	private void reTakePicture() {
//		iv.setImageBitmap(null);
//		camera.startPreview();
//		iv.setVisibility(View.VISIBLE);
//		maskView.setVisibility(View.VISIBLE);
//		cameraTip.setVisibility(View.VISIBLE);
//	}
//
//	protected void saveBitmapToSD(Bitmap bt) {
//		File bOldFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aImage/", "testFile.jpg" );
//		FileOutputStream out = null;
//		try {
//			 out = new FileOutputStream(bOldFile);
//			bt.compress(Bitmap.CompressFormat.JPEG, 90, out);
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//		private final class MyPictureCallback implements PictureCallback {
//
//		@Override
//		public void onPictureTaken(byte[] data, Camera camera) {
//			camera.stopPreview();
//			camera.setPreviewCallback(null) ;
//			if (cropCamera) {
//				BitmapFactory.Options opts = new BitmapFactory.Options();
//				opts.inJustDecodeBounds = false;
//				Bitmap bOld = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
//				saveBitmapToSD(bOld);
//
//				Rect rectPreview = new Rect();
//				surfaceView.getGlobalVisibleRect(rectPreview);
//				Rect rectResult = new Rect();
//				iv.getGlobalVisibleRect(rectResult);
//				Log.d("CameraActivity", "bigger: " + rectPreview.toString() + "; small: " + rectResult.toString());
//				DisplayMetrics dm = new DisplayMetrics();
//				getWindowManager().getDefaultDisplay().getMetrics(dm);
//				int test1 = dm.heightPixels;
//				int test2 = dm.widthPixels;
//				float widthStartRate = (float) (rectResult.left - rectPreview.left)/(rectPreview.right - rectPreview.left);
//				float heightStartRate = (float) (rectResult.top - rectPreview.top)/(rectPreview.bottom - rectPreview.top);
//
//				float widthRate = (float) (rectResult.right - rectResult.left)/(rectPreview.right - rectPreview.left);
//				float heightRate = (float) (rectResult.bottom - rectResult.top)/(rectPreview.bottom - rectPreview.top);
//
//				int bOldWidth = bOld.getWidth();
//				int bOldHeight = bOld.getHeight();
//
//				float scaleWidth =(float) bOldWidth/dm.widthPixels;
//				float scaleHeight = (float)bOldHeight/dm.heightPixels;
//
//				float rate = (float) scaleHeight/scaleWidth;
//
//				int widthStart = (int) Math.abs(bOld.getWidth()*widthStartRate);
//				int heightStart = (int) Math.abs(bOld.getHeight()*heightStartRate*rate);
//
//				int cropWidth = (int) Math.abs(bOld.getWidth()*widthRate);
//				int cropHeight = (int) Math.abs(bOld.getHeight()*heightRate/rate);
//
//				/*
//				// 扩张宽度
//				//int extendW = (int) (MaxWidthPx * 0.03);
//				//int extendH = (int) (MaxHeightPx * 0.03);
//				int extendW = 0;
//				int extendH = 0;
//				// 增加部分截取边距
//				int x = (int) (bOld.getWidth() * (1 - imageWidthRate) / 2 - extendW);
//				int y = (int) (bOld.getHeight() * (1 - imageHeightRate) / 2 - extendH);
//				// 防止截取越界
//				if (x < 0) {
//					x = 0;
//				}
//				if (y < 0) {
//					y = 0;
//				}
//				int cropWidth = (int) (bOld.getWidth() * imageWidthRate+ extendW * 2);
//				int cropHeight = (int) (bOld.getHeight() * imageHeightRate + extendH * 2);
//				if (x + cropWidth > bOld.getWidth()) {
//					cropWidth = bOld.getWidth() - x;
//				}
//				if (y + cropHeight > bOld.getHeight()) {
//					cropHeight = bOld.getHeight() - y;
//				}
//				*/
//				//调试代码，测量图片布局尺寸
//				bNew[takeIndex] = Bitmap.createBitmap(bOld, widthStart, heightStart, cropWidth, cropHeight);
//				if (canRotaion) {
//					iv.setImageBitmap(bNew[takeIndex]);
//				}
//
//			} else {
//				BitmapFactory.Options opts = new BitmapFactory.Options();
//				opts.inJustDecodeBounds = false;
//				Bitmap bOld = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
//				bNew[takeIndex] = Bitmap.createBitmap(bOld, 0, 0, (int) (bOld.getWidth()), (int) (bOld.getHeight()));
//				if (canRotaion) {
//					iv.setImageBitmap(bNew[takeIndex]);
//				}
//
//			}
//			retakepicture.setVisibility(View.VISIBLE);
//			makesure.setVisibility(View.VISIBLE);
//			if (canRotaion) {
//				rotaion.setVisibility(View.VISIBLE);
//			}
//		}
//	}
//
//	private final class SurfaceCallback implements Callback {
//
//		@Override
//		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//		}
//
//		@Override
//		public void surfaceCreated(SurfaceHolder holder) {
//			int cameraCount= Camera.getNumberOfCameras();
//			int cameraIndex=0;
//			for(int i = 0; i < cameraCount; i++   ) {
//				CameraInfo cameraInfo = new CameraInfo();;
//                Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
//                if(cameraInfo.facing== CameraInfo.CAMERA_FACING_BACK){
//                	cameraIndex=i;
//                }
//			}
//			camera = Camera.open(cameraIndex);
//			try {
//				camera.setPreviewDisplay(holder);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//
//			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//				camera.setDisplayOrientation(getPreviewDegree(CameraActivity.this));
//			}
//			updateCameraParameters();
//			camera.startPreview();
//			camera.autoFocus(mAutoFocusCallback);
//
//		}
//
//		@Override
//		public void surfaceDestroyed(SurfaceHolder holder) {
//			if (camera != null) {
//				camera.release();
//				camera = null;
//			}
//		}
//	}
//
//	public static int getPreviewDegree(Activity activity) {
//		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//		int degree = 0;
//		switch (rotation) {
//		case Surface.ROTATION_0:
//			if (repaireDegree) {
//				degree = 90;
//			} else {
//				degree = 0;
//			}
//			break;
//		case Surface.ROTATION_90:
//			if (repaireDegree) {
//				degree = 0;
//			} else {
//				degree = 90;
//			}
//			break;
//		case Surface.ROTATION_180:
//			if (repaireDegree) {
//				degree = 270;
//			} else {
//				degree = 180;
//			}
//			break;
//		case Surface.ROTATION_270:
//			if (repaireDegree) {
//				degree = 180;
//			} else {
//				degree = 270;
//			}
//			break;
//		}
//		return degree;
//	}
//
//	private void updateCameraParameters() {
//		if (camera != null) {
//			parameters = camera.getParameters();
//			// 设置相机
//			parameters.setPictureFormat(ImageFormat.JPEG);
//			// 低版本会报错
//			parameters.setRotation(getPreviewDegree(CameraActivity.this));
//			parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
//			// 设置测光
//			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//				if (parameters.getMaxNumMeteringAreas() > 0) { // 检查是否支持测光区域
//					List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
//
//					Rect areaRect1 = new Rect(-100, -100, 100, 100); // 在图像的中心指定一个区域
//					meteringAreas.add(new Camera.Area(areaRect1, 1000)); // 设置宽度到60%
//					parameters.setMeteringAreas(meteringAreas);
//				}
//			}
//			// 设置图片大小
//			Size pictureSize = findBestPictureSize(parameters);
////			LogUtils.d("width:" + pictureSize.width + "|height:" + pictureSize.height);
//			parameters.setPictureSize(pictureSize.width, pictureSize.height);
//
//			// 设置预览大小(不设置，随系统)
//			Size previewSize = findBestPreviewSize(parameters);
////			LogUtils.d("pwidth:" + previewSize.width + "|pheight:" + previewSize.height);
//			// parameters.setPreviewSize(previewSize.width, previewSize.height);
//
//			// 设置截图区域
//			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
//
//			params.width = (int) (previewSize.width * imageWidthRate);
//			params.height = (int) (previewSize.height * imageHeightRate);
//			iv.setLayoutParams(params);
//
//			// 设置预览区域大小
//			FrameLayout.LayoutParams paramsSur = (FrameLayout.LayoutParams) surfaceView.getLayoutParams();
//			paramsSur.height = previewSize.height;
//			paramsSur.width = previewSize.width;
//			surfaceView.setLayoutParams(paramsSur);
//			camera.setParameters(parameters);
//			// 设置遮罩层xxh20141103新增
//			if (maskView != null) {
//				Rect screenCenterRect = createCenterScreenRect((int) params.width/*(previewSize.width * imageWidthRate)*/, (int) params.height/*(previewSize.height * imageHeightRate)*/);
//				maskView.setCenterRect(screenCenterRect);
//			}
//		}
//	}
//
//	/**
//	 * 生成屏幕中间的矩形
//	 *
//	 * @param w
//	 *            目标矩形的宽度,单位px
//	 * @param h
//	 *            目标矩形的高度,单位px
//	 * @return
//	 */
//	private Rect createCenterScreenRect(int w, int h) {
//		int x1 = DisplayUtil.getScreenMetrics(this).x / 2 - w / 2;
//		int y1 = DisplayUtil.getScreenMetrics(this).y / 2 - h / 2;
//		int x2 = x1 + w;
//		int y2 = y1 + h;
//		return new Rect(x1, y1, x2, y2);
//	}
//
//	private Size findBestPreviewSize(Parameters parameters) {
//		int degree = getPreviewDegree(CameraActivity.this);
//		int srcWidth = 0;
//		int srcHeight = 0;
//		if (degree == 0 || degree == 180) {
//			srcWidth = parameters.getPictureSize().width;
//			srcHeight = parameters.getPictureSize().height;
//		} else {
//			srcWidth = parameters.getPictureSize().height;
//			srcHeight = parameters.getPictureSize().width;
//		}
//
//		int supportPreviewWidth = getScreenWH().widthPixels;
//		int supportPreviewHeight = getScreenWH().heightPixels;
//		float wbl = (float) srcWidth / supportPreviewWidth;
//		float hbl = (float) srcHeight / supportPreviewHeight;
//		// float bl = wbl > hbl ? wbl : hbl;
//		int width = (int) (srcWidth / wbl);// bl);
//		int height = (int) (srcHeight / hbl);// bl);
//
//		// int width =(int) (srcWidth / bl);
//		// int height = (int) (srcHeight / bl);
//		return camera.new Size(width, height);
//	}
//
//    //Camera PictureSize适配
//    private Camera.Size findBestPictureSizeBetter(Camera.Parameters parameters, Camera camera) {
//        int diff = Integer.MIN_VALUE;
//        float nearDiff = Integer.MAX_VALUE;
//        String pictureSizeValueString = parameters.get("picture-size-values");
//        DisplayMetrics screenMetrics = getScreenWH();
//        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
//        int ori = mConfiguration.orientation; //获取屏幕方向
//
//        int x = screenMetrics.widthPixels;int y = screenMetrics.heightPixels;
//        Point screenResolution = null;
//
//        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
//            //横屏
//            screenResolution =  new Point(x, y);
//        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
//            //竖屏
//            screenResolution =  new Point(y, x);
//        }
//        // saw this on Xperia
//        if (pictureSizeValueString == null) {
//            pictureSizeValueString = parameters.get("picture-size-value");
//        }
//
//        if (pictureSizeValueString == null) {
//            return camera.new Size(getScreenWH().widthPixels, getScreenWH().heightPixels);
//        }
//
////		LogUtils.d("pictureSizeValueString : " + pictureSizeValueString);
//        int bestX = 0;
//        int bestY = 0;
//
//        int nearX = 0;
//        int nearY = 0;
//
//        for (String pictureSizeString : pictureSizeValueString.split(",")) {
//            pictureSizeString = pictureSizeString.trim();
//
//            int dimPosition = pictureSizeString.indexOf('x');
//            if (dimPosition == -1) {
////				LogUtils.e("Bad pictureSizeString:" + pictureSizeString);
//                continue;
//            }
//
//            int newX = 0;
//            int newY = 0;
//
//            try {
//                newX = Integer.parseInt(pictureSizeString.substring(0, dimPosition));
//                newY = Integer.parseInt(pictureSizeString.substring(dimPosition + 1));
//            } catch (NumberFormatException e) {
////				LogUtils.e("Bad pictureSizeString:" + pictureSizeString);
//                continue;
//            }
//
//            int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
//
//            //近似查找
//            float newNearDiff = Math.abs((float) newX/newY - (float)screenResolution.x/screenResolution.y);
//            if (newDiff == diff) {
//                bestX = newX;
//                bestY = newY;
//                break;
//            } else if (newDiff > diff) {
//                if ((((float)newX/screenResolution.x) == ((float)newY/screenResolution.y)) && bestX < newX) {
//                    bestX = newX;
//                    bestY = newY;
//                    diff = newDiff;
//                }else if ((((float)newX/screenResolution.x) != ((float)newY/screenResolution.y)) && newNearDiff < nearDiff){
//                    nearX = newX;
//                    nearY = newY;
//                    nearDiff = newNearDiff;
//                }
//            }
//        }
//
//        if (bestX > 0 && bestY > 0) {
//            return camera.new Size(bestX, bestY);
//        }else if (nearX>0 && nearY > 0){
//            return camera.new Size(nearX, nearY);
//        }else {
//            return camera.new Size(getScreenWH().widthPixels, getScreenWH().heightPixels);
//        }
//    }
//
//	protected DisplayMetrics getScreenWH() {
//		DisplayMetrics dMetrics = new DisplayMetrics();
//		dMetrics = this.getResources().getDisplayMetrics();
//		return dMetrics;
//	}
//
//}
