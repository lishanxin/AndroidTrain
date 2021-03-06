package com.example.androidtrain.pictureAnimation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;

import java.lang.ref.WeakReference;

//如何加载缩放的图片，如何并行加载图片；
// 如何缓存图片：1，内存缓存，速度快，不过程序被杀死后无法保存；2，磁盘缓存，速度没有内存缓存块，不过能够在程序被杀死后还保存着经过处理的bitmap
public class EffectiveBitmapActivity extends BaseActivity {

    ImageView mImageView;

    Bitmap mPlaceHolderBitmap;

    //图片内存缓存准备
    private LruCache<String,Bitmap> mMemoryCache;

    //图片磁盘缓存准备
//    private Disk

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effective_bitmap);

        mPlaceHolderBitmap = BitmapFactory.decodeFile("file:///android_asset/images/jiguang_socialize_cp_link.png");

        mImageView = (ImageView) findViewById(R.id.effect_bitmap_view);
        loadBitmap(R.drawable.iu, mImageView);

        //缓存图片
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

//    static class DiskLruCache extends LruCache<String,Bitmap>{
//
//        /**
//         * @param maxSize for caches that do not override {@link #sizeOf}, this is
//         *                the maximum number of entries in the cache. For all other caches,
//         *                this is the maximum sum of the sizes of the entries in this cache.
//         */
//        public DiskLruCache(int maxSize) {
//            super(maxSize);
//        }
//
//    }

    //内存缓存图片
    public void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if (getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key, bitmap);
        }
    }

    //获取内存缓存的图片
    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    //在不产生bitmap的情况下，读取位图的尺寸与类型。
    public void readBounds(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile("file:///android_asset/images/iu.jpg", options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
    }

    //设置BitmapFactory.Options中设置inSampleSize的值，可以得到缩小版的图。512*384的图片，设置inSampleSize为2，那么会产出一个256*192的图片。
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        //Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth){
            final int halfHeight = height/2;
            final int halfWidth = width/2;

            //Calculate the largest inSampleSize value that is a power of 2 and keeps both
            //height and width larger than the requested height and width
            //为了显示不至于模糊，实际图片的宽度和高度肯定要大于屏幕需求的宽度和高度。
            //先将图片实际的高宽/2，如果得到的结果没有大于屏幕需求的高宽，则表示不需要缩小图片，设置inSimpleSize为1；否者表示可以缩小，设置inSimpleSize*2。。。
            while ((halfHeight/inSampleSize) > reqHeight
                    && (halfWidth/inSampleSize) > reqWidth){
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //获取缩小后图片
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

//    public void loadBitmap(int resId, ImageView imageView){
//        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
//        task.execute(resId);
//    }

    //可用于需要处理并发机制的ListView或者GridView控件中
    public void loadBitmap(int resId, ImageView imageView){
        if (cancelPotentialWork(resId, imageView)){
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);

            //将drawable 绑定到ImageView中
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    //使用LruCache来管理Bitmap(内存缓存）
    public void loadBitmapLruCache(int resId, ImageView imageView){
        final String imageKey = String.valueOf(resId);

        final  Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null){
            mImageView.setImageBitmap(bitmap);
        } else {
            mImageView.setImageResource(R.drawable.icon_jiguang);
            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
            task.execute(resId);
        }
    }


    //尝试从内存缓存中加载图片
    public void loadBitmapWithCache(int resId, ImageView imageView){
        final String imageKey = String.valueOf(resId);
        final  Bitmap bitmap = getBitmapFromMemCache(imageKey);

        if (bitmap != null){
            mImageView.setImageBitmap(bitmap);
        }else {
            //设置placeholder
            mImageView.setImageResource(R.drawable.icon_jiguang);
            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
            task.execute(resId);
        }
    }

    //检查是否有另一个正在执行的任务与该ImageView关联了气来，如果的确是这样，它通过执行cancel（）方法来取消另一个任务。
    public static boolean cancelPotentialWork(int resId, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null){
            final int bitmapData = bitmapWorkerTask.data;
            if (bitmapData == 0 || bitmapData != resId){
                //如果此ImageView有关联过Task，且此task的图片来源与现在的不一致，则取消之前关联过的task的运行
                bitmapWorkerTask.cancel(true);
            } else {
                // 如果有关联过Task且图片来源与现在的一致，则返回false，让现在的Task不执行
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    //从Drawable中获取绑定的AsyncTask
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView){
        if (imageView != null){
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable){
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    //解析图片属于耗时操作，需要放在异步线程中进行
    class BitmapWorkerTask extends AsyncTask<Integer,Object,Bitmap> {

        private final WeakReference imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            //Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            data  = integers[0];
            final Bitmap bitmap =  decodeSampledBitmapFromResource(getResources(), data, 100, 100);

            //为图片的内存缓存增加的步骤
            addBitmapToMemoryCache(String.valueOf(data), bitmap);

            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap
        @Override
        protected void onPostExecute(Bitmap bitmap) {
             if (isCancelled()){
                 bitmap = null;
             }
            if (imageViewReference != null && bitmap != null){
                final ImageView imageView = (ImageView) imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

                if (this == bitmapWorkerTask && imageView != null){
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    //为了处理并发的图片加载，防止加载位置错误，需创建一个专用的Drawable的子类来存储任务的引用
    static class AsyncDrawable extends BitmapDrawable{

        private final WeakReference bitmapWorkerTaskReference;
        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask(){
            return (BitmapWorkerTask) bitmapWorkerTaskReference.get();
        }
    }

}
