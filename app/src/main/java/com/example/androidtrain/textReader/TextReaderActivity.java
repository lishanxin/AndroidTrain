package com.example.androidtrain.textReader;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;

import static com.example.androidtrain.textReader.TextRederUtil.getPage;

public class TextReaderActivity extends BaseActivity {

    private static final String TAG = "TextReader";
    TextView mTextView;

    int mPage = 0;
    private String testText ;
    Handler handler = new Handler();

    int[] mPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reader);
        testText = getResources().getString(R.string.screen_slide_text);
        mTextView = (TextView) findViewById(R.id.text_reader_activity_textview);
        mTextView.setText(testText);

//        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        mTextView.measure(spec, spec);
        ViewTreeObserver vto = mTextView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPageIndex = getPage(mTextView);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(testText.substring(0,mPageIndex[0]));
                        Log.d(TAG, "start setTextPage");
                    }
                });
                mTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void lastPage(View view) {
    }

    public void nextPage(View view) {
    }
}
