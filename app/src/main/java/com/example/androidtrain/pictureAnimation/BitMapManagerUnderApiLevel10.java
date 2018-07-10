package com.example.androidtrain.pictureAnimation;

import android.graphics.Bitmap;

/**
 * Created by lizz on 2018/7/10.
 * 使用了引用计数的方法（mDisplayRefCount 与 mCacheRefCount）来追踪一个Bitmap目前是否有被显示或者是在缓存中。并且在下面列举的条件满足时，回收Bitmap：
 mDisplayRefCount 与 mCacheRefCount 的引用计数均为 0；
 bitmap不为null, 并且它还没有被回收。
 */

public class BitMapManagerUnderApiLevel10 {
    private int mCacheRefCount = 0;
    private int mDisplayRefCount = 0;
    private boolean mHasBeenDisplayed = false;
    // Notify the drawable that the displayed state has changed.
// Keep a count to determine when the drawable is no longer displayed.
    public void setIsDisplayed(boolean isDisplayed) {
        synchronized (this) {
            if (isDisplayed) {
                mDisplayRefCount++;
                mHasBeenDisplayed = true;
            } else {
                mDisplayRefCount--;
            }
        }
        // Check to see if recycle() can be called.
        checkState();
    }

    // Notify the drawable that the cache state has changed.
// Keep a count to determine when the drawable is no longer being cached.
    public void setIsCached(boolean isCached) {
        synchronized (this) {
            if (isCached) {
                mCacheRefCount++;
            } else {
                mCacheRefCount--;
            }
        }
        // Check to see if recycle() can be called.
        checkState();
    }

    private synchronized void checkState() {
        // If the drawable cache and display ref counts = 0, and this drawable
        // has been displayed, then recycle.
        if (mCacheRefCount <= 0 && mDisplayRefCount <= 0 && mHasBeenDisplayed
                && hasValidBitmap()) {
            getBitmap().recycle();
        }
    }

    private synchronized boolean hasValidBitmap() {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }

    private Bitmap getBitmap() {
        return null;
    }


}
