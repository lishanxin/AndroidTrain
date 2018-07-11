package com.example.androidtrain.pictureAnimation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lizz on 2018/7/11.
 */

public class BitmapManagerAboveApiLevel11 {

    //当Bitmap从LruCache中移除时，将BitMap的软引用保存在HashSet中，方便inBitmap重用
    static Set<SoftReference<Bitmap>> mReusableBitmaps;

    private LruCache<String,BitmapDrawable> mMemoryCache;

    public void startStep(){
        if (Build.VERSION.SDK_INT >= 11){
            mReusableBitmaps = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
        }
        mMemoryCache = new LruCache<String,BitmapDrawable>(10){
            // Notify the removed entry that is no longer being cached.
            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        BitmapDrawable oldValue, BitmapDrawable newValue) {
                if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
                    // The removed entry is a recycling drawable, so notify it
                    // that it has been removed from the memory cache.
                    ((RecyclingBitmapDrawable) oldValue).setIsCached(false);
                } else {
                    // The removed entry is a standard BitmapDrawable.
                    if (Build.VERSION.SDK_INT >= 11) {
                        // We're running on Honeycomb or later, so add the bitmap
                        // to a SoftReference set for possible use with inBitmap later.
                        //Bitmap被移除时，调用HashSet来保存软引用
                        mReusableBitmaps.add
                                (new SoftReference<Bitmap>(oldValue.getBitmap()));
                    }
                }
            }
        };
    }

    public static Bitmap decodeSampledBitmapFromFile(String filename,
                                                     int reqWidth, int reqHeight, ImageCache cache) {
        //方案一：不选择inBitmap来进行优化
        final BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFile(filename, options);
        // If we're running on Honeycomb or newer, try to use inBitmap.
        //方案二：选择inBitmap来优化
        if (Build.VERSION.SDK_INT >= 11) {
            addInBitmapOptions(options, cache);
        }
        return BitmapFactory.decodeFile(filename, options);
    }

    private static void addInBitmapOptions(BitmapFactory.Options options,
                                           ImageCache cache) {
        // inBitmap only works with mutable bitmaps, so force the decoder to
        // return mutable bitmaps.
        //选择inBitmap前需要设置inMutable为true
        options.inMutable = true;

        if (cache != null) {
            // Try to find a bitmap to use for inBitmap.
            //判断Set中的软引用是否有可以复用的Bitmap，如果有，则赋值到inBitmap中
            Bitmap inBitmap = cache.getBitmapFromReusableSet(options);

            if (inBitmap != null) {
                // If a suitable bitmap has been found, set it as the value of
                // inBitmap.
                options.inBitmap = inBitmap;
            }
        }
    }


    private static class ImageCache {

        // This method iterates through the reusable bitmaps, looking for one
// to use for inBitmap:
        protected Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
            Bitmap bitmap = null;

            if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
                synchronized (mReusableBitmaps) {
                    final Iterator<SoftReference<Bitmap>> iterator
                            = mReusableBitmaps.iterator();
                    Bitmap item;

                    while (iterator.hasNext()) {
                        item = iterator.next().get();

                        if (null != item && item.isMutable()) {
                            // Check to see it the item can be used for inBitmap.
                            if (canUseForInBitmap(item, options)) {
                                bitmap = item;

                                // Remove from reusable set so it can't be used again.
                                iterator.remove();
                                break;
                            }
                        } else {
                            // Remove from the set if the reference has been cleared.
                            iterator.remove();
                        }
                    }
                }
            }
            return bitmap;
        }

        //判断是否可用与inBitmap；KITKAT之前，需要满足同等大小的位图才可以被重用；KITKAT之后，满足现在要使用的位图比原先的小就行。
        static boolean canUseForInBitmap(
                Bitmap candidate, BitmapFactory.Options targetOptions) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // From Android 4.4 (KitKat) onward we can re-use if the byte size of
                // the new bitmap is smaller than the reusable bitmap candidate
                // allocation byte count.
                int width = targetOptions.outWidth / targetOptions.inSampleSize;
                int height = targetOptions.outHeight / targetOptions.inSampleSize;
                int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
                return byteCount <= candidate.getAllocationByteCount();
            }

            // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
            return candidate.getWidth() == targetOptions.outWidth
                    && candidate.getHeight() == targetOptions.outHeight
                    && targetOptions.inSampleSize == 1;
        }


        /**
         * A helper function to return the byte usage per pixel of a bitmap based on its configuration.
         */
        static int getBytesPerPixel(Bitmap.Config config) {
            if (config == Bitmap.Config.ARGB_8888) {
                return 4;
            } else if (config == Bitmap.Config.RGB_565) {
                return 2;
            } else if (config == Bitmap.Config.ARGB_4444) {
                return 2;
            } else if (config == Bitmap.Config.ALPHA_8) {
                return 1;
            }
            return 1;
        }

    }

    private class RecyclingBitmapDrawable extends BitmapDrawable{
        private boolean isCached;
        public void setIsCached(boolean b) {
            isCached = b;
        }
    }


}
