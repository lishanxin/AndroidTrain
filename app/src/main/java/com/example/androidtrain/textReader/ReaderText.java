package com.example.androidtrain.textReader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sx on 2018/7/25.
 */

public class ReaderText extends android.support.v7.widget.AppCompatTextView {
    public ReaderText(Context context) {
        this(context, null);
    }

    public ReaderText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ReaderText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
