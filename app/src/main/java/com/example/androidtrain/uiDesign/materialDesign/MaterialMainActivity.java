package com.example.androidtrain.uiDesign.materialDesign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidtrain.R;


/**
 * Material设计规范：http://www.google.com/design/spec
 * 要在应用中使用 Material 主题，需要定义一个继承于 android:Theme.Material 的 style 文件：
 */
public class MaterialMainActivity extends Activity {

    TextView mainTextView;
    CardView mCardView;
    Button mButton;
    ImageView mVectorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_main);

        mButton = (Button) findViewById(R.id.main_material_button);
        mCardView = (CardView) findViewById(R.id.main_material_cardview);
        mainTextView = (TextView) findViewById(R.id.main_material_text);
        mVectorView = (ImageView) findViewById(R.id.vector_drawable);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21)
                mainTextView.setTranslationZ(100);
                onSharedElementActivity();
//                onSomeButtonClicked();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21){
                    if (mCardView.getVisibility() == View.VISIBLE){
                        animateInvisible(mCardView);
                    }else {
                        animateVisible(mCardView);
                    }
                }
            }
        });
        mVectorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateByPath();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 21)
            mainTextView.setTranslationZ(10);
    }

    private void onSomeButtonClicked(){
        Intent intent = new Intent(MaterialMainActivity.this, SecondMaterialActivity.class);
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().setExitTransition(new Explode());
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else {
            startActivity(intent);
        }
    }

    private void onSharedElementActivity(){
        if (Build.VERSION.SDK_INT >= 21){
            Intent intent = new Intent(this, SecondMaterialActivity.class);

            // create the transition animation - the images in the layouts
            // of both activites are defined with android:transitionName='iuImage'
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.create((View)mCardView, "iuImage"));
            startActivity(intent, options.toBundle());
        }
    }

    //使组件动态显示
    public void animateVisible(final View view){
        if (Build.VERSION.SDK_INT >= 21){
            //view is previously invisible view

            //get the center for the clipping circle
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;

            // get the final radius for the clipping circle
            int finalRadius = view.getWidth();

            // create and start the animator for this view
            // (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });
            anim.start();
        }

    }

    //使组件动态隐藏
    public void animateInvisible(final View view){
        if (Build.VERSION.SDK_INT >= 21){
            // previously visible view

            //get the center for the clipping circle
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;

            // get the initial radius for the clipping circle
            int initialRadius = view.getWidth();

            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();
        }
    }

    public void animateByPath(){
        if (Build.VERSION.SDK_INT >= 21){
            Path path = new Path();
            path.moveTo(0,0);
            path.quadTo(0f,110f, 90f,200f);
            ObjectAnimator mAnim;
            mAnim = ObjectAnimator.ofFloat(mCardView, View.X, View.Y, path);
            mAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mCardView.setVisibility(View.VISIBLE);
                }
            });
            mAnim.start();
        }
    }

    public void startVectorAnimate(View view) {
        ImageView imageView=(ImageView)view;
        Drawable drawable=imageView.getDrawable();
        if(drawable instanceof Animatable){
            ((Animatable)drawable).start();
        }
    }
}
