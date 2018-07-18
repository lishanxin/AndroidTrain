package com.example.androidtrain.pictureAnimation.screenslide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidtrain.R;

/**
 * Created by lizz on 2018/7/18.
 */

//提供每一个Slide-Pager碎片视图
public class ScreenSlidePageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        return rootView;
    }
}
