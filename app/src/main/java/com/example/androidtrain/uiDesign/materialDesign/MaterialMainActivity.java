package com.example.androidtrain.uiDesign.materialDesign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.view.ViewAnimationUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_main);

        mButton = (Button) findViewById(R.id.main_material_button);
        mCardView = (CardView) findViewById(R.id.main_material_cardview);
        mainTextView = (TextView) findViewById(R.id.main_material_text);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21)
                mainTextView.setTranslationZ(100);

                onSomeButtonClicked();
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

    public void animateVisible(final View view){
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

    public void animateInvisible(final View view){
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
