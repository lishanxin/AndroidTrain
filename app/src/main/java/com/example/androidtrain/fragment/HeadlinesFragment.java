package com.example.androidtrain.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidtrain.R;

import static com.example.androidtrain.MainActivity.EXTRA_MESSAGE;

/**
 * Created by lizz on 2018/6/2.
 */

public class HeadlinesFragment extends Fragment {

    OnHeadlinesSelectedListener mCallback;

    public interface OnHeadlinesSelectedListener{
        public void onArticleSelected(int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.head_view, container, false);
//        TextView textView = null;
//        if ((textView = (TextView) view.findViewById(R.id.head_view_text)) != null){
//            textView.setText(getArguments().getString(EXTRA_MESSAGE));
//        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (OnHeadlinesSelectedListener) activity;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.onArticleSelected(100);
    }
}
