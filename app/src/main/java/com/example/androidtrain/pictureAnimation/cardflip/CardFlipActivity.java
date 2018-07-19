package com.example.androidtrain.pictureAnimation.cardflip;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidtrain.R;

public class CardFlipActivity extends AppCompatActivity {

    private boolean mShowingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.card_flip_container, new CardFrontFragment())
                    .commit();
        }
    }

    private void flipCard(){
        if (mShowingBack){
            getSupportFragmentManager().popBackStack();
            return;
        }

        // Flip to the back
        // 向背面翻转
        mShowingBack = true;

        //开启Fragment转换，将Fragment转换成背面的卡片，并使用动画进行翻转，
        //并将其设置为FragmentManager栈空间的一员
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations()
    }


    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    /**
     * A fragment representing the back of the card
     *
     */
    public static class CardBackFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }
}
