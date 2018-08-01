package com.example.androidtrain.pictureAnimation.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;

//View间渐变Demo
//http://hukai.me/android-training-course-in-chinese/animations/crossfade.html
public class CrossfadeActivity extends BaseActivity {

    private View mContentView;
    private View mLoadingView;
    private int mShortAnimationDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfade);

        mContentView = findViewById(R.id.crossfade_content);
        mLoadingView = findViewById(R.id.loading_spinner);

        // Initially hide the content view.
        mContentView.setVisibility(View.GONE);

        //Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        crossfade();
                    }
                });
            }
        }).start();
    }

    /*
    实现渐变动画：
    1:对于正在淡入的View，设置它的alpha值为0并且设置visibility为 VISIBLE（记住他起初被设置成了 GONE）。
    这样View就变成可见的了，但是此时它是透明的。
    2:对于正在淡入的View，把alpha值从0动态改变到1。同时，对于淡出的View，把alpha值从1动态变到0。
    3:使用Animator.AnimatorListener中的 onAnimationEnd()，设置淡出View的visibility为GONE。即使alpha值为0，
    也要把View的visibility设置成GONE来防止 view 占据布局空间，还能把它从布局计算中忽略，加速处理过程。
     */
    private void crossfade(){
        //Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view
        mContentView.animate().alpha(1f).setDuration(mShortAnimationDuration).setListener(null);

        //Animate the loading view to 0% opacity, After the animation ends,
        // Set its visibility to GONE as an optmization step (it won't
        // participate in layout passes, etc.)
        mLoadingView.animate().alpha(0f).setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });
    }
}
