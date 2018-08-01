package com.example.androidtrain.pictureAnimation.zoom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;

public class ZoomActivity extends BaseActivity {

    private static final String TAG = "ZoomActivity";

    //保存当前动画效果的引用，为了在程序运行中将其动画效果取消
    private Animator mCurrentAnimator;

    //动画效果的duration
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        final View thumb1View = findViewById(R.id.zoom_thumb_button_1);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(thumb1View, R.drawable.viewpagersample4);
            }
        });

        //Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }


    /**
     * 1:把高清图像资源设置到已经被隐藏的“放大版”的ImageView中。为表简单，下面的例子在UI线程
     * 中加载了一张大图。但是我们需要在一个单独的线程中来加载以免阻塞UI线程，然后再回到UI线程
     * 中设置。理想状况下，图片不要大过屏幕尺寸。(AsyncTask)
     * 2:计算ImageView开始和结束时的边界。
     * 3:从起始边到结束边同步地动态改变四个位置和大小属性X，Y（SCALE_X 和 SCALE_Y）。这四个动
     * 画被加入到了AnimatorSet，所以我们可以让它们一起开始。
     * 4:缩小则运行相同的动画，但是是在用户点击屏幕放大时的逆向效果。我们可以在ImageView中添加
     * 一个View.OnClickListener来实现它。当点击时，ImageView缩回到原来缩略图的大小，然后设置它
     * 的visibility为GONE来隐藏。
     */


    private void zoomImageFromThumb(final View thumbView, int imageResId) {

        // 如果在此进程中有动画正在进行，则立即停止该动画，并继续往下执行
        if (mCurrentAnimator != null){
            mCurrentAnimator.cancel();
        }

        //加载放大后的图片，并设置图片源
        final ImageView expandedImageView = (ImageView) findViewById(R.id.zoom_expanded_image);
        expandedImageView.setImageResource(imageResId);

        //计算动画前与动画后的组件边界，此步骤设计较多数学方面的知识
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        Point globalOffset = new Point();

        //设置动画前的边界为缩略图组件在视图中的边界，设置动画后的边界为放大图组件在视图中的边界
        //坐标值为左上角为出发点，向右向下为正。最左上角的值为（0，0）
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.zoom_container)
                .getGlobalVisibleRect(finalBounds, globalOffset);

        Log.d(TAG, "startBounds: " + startBounds +
                "; finalBounds: " + finalBounds + "; globalOffset: " + globalOffset);

        //设置xy偏移量
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        final float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.width()){
            // 横向比例较大，所以取纵向比例为缩放比例
            startScale = (float) startBounds.height() / finalBounds.height();

            //由于缩放比例按高度获取，所以高度方面不用特别计算，不过需要重新计算宽度开始值
            float startWidth = startScale * finalBounds.width();

            //计算开始宽度值的偏移量，以便对开始的Rect进行设置
            float deltaWidth = (startWidth - startBounds.width()) / 2;

            //向左偏移一半
            startBounds.left -= deltaWidth;
            //向右偏移另一半
            startBounds.right += deltaWidth;
        }else {
            // 纵向比例较大,所以取横向比例为缩放比例
            startScale = (float) startBounds.width() / finalBounds.width();

            //由于缩放比例按宽度获取，所以宽度方面不用特别计算，不过需要重新计算高度开始值
            float startHeight = startScale * finalBounds.height();

            //计算开始高度值的偏移量，以便对开始的Rect进行设置
            float deltaHeight = (startHeight - startBounds.height()) / 2;

            //向上偏移一半
            startBounds.top -= deltaHeight;
            //向下偏移另一半
            startBounds.bottom += deltaHeight;
        }

        //隐藏缩略图，替换成放大图
        thumbView.setAlpha(0.0f);
        expandedImageView.setVisibility(View.VISIBLE);

        //设置缩放的支点为视图的左上角点（默认为视图的中点）
        expandedImageView.setPivotX(0.0f);
        expandedImageView.setPivotY(0.0f);

        //设置四个平行方向的变换动画效果，和比例
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));

        //设置时间
        set.setDuration(mShortAnimationDuration);

        //？？
        set.setInterpolator(new DecelerateInterpolator());

        //设置监听
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });

        set.start();
        mCurrentAnimator = set;

        //当点击放大后的图片时，需要缩小回缩略图
        float startScaleFinal  = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                //设置四个平行方向的变换动画效果，和比例
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale));

                //设置时间
                set.setDuration(mShortAnimationDuration);

                //？？
                set.setInterpolator(new DecelerateInterpolator());

                //设置监听
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1.0f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1.0f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });

                set.start();
                mCurrentAnimator = set;
            }
        });
    }


}
