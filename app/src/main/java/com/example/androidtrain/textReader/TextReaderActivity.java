package com.example.androidtrain.textReader;

import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.androidtrain.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextReaderActivity extends AppCompatActivity {

    private static final String TAG = "TextReader";
    ReaderText mTextView;

    int mPage = 0;
    private String testText = "很多次，我都想爬起来，可我找不到任何理由。似乎在那一瞬间，我有种被整个世界抛弃的感觉。这时，月亮从我头顶走过。我小心翼翼的把月亮捧在手心，月亮慢慢的渗透进我的皮肤。一丝丝凉意席卷全身，我禁不住浑身颤抖，满头大汗。我知道，这是月亮在吸食我的灵魂。终于，我感觉身轻如燕，漂浮在静谧的黑夜里。" +
            "很多次，我都想爬起来，可我找不到任何理由。似乎在那一瞬间，我有种被整个世界抛弃的感觉。这时，月亮从我头顶走过。我小心翼翼的把月亮捧在手心，月亮慢慢的渗透进我的皮肤。一丝丝凉意席卷全身，我禁不住浑身颤抖，满头大汗。我知道，这是月亮在吸食我的灵魂。终于，我感觉身轻如燕，漂浮在静谧的黑夜里";

    Handler handler = new Handler();

    int[] mPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reader);

        mTextView = (ReaderText) findViewById(R.id.text_reader_activity_textview);
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
    public int[] getPage( TextView textView){
        int count = textView.getLineCount();

        int pageLine = getPageLineNumber(textView);
        int pageNum = count/pageLine;
        int page[] = new int[pageNum];
        for(int i=0;i<pageNum;i++){
            page[i]=textView.getLayout().getLineEnd((i+1)*pageLine - 1);
        }
        return page;
    }


    public int getPageLineNumber(TextView textView){

        int height = textView.getBottom() - textView.getTop() - textView.getPaddingTop();

        //第一页与其他页的高度不一样
        int firstH = getLineHeight(0, textView);
        int otherH = getLineHeight(1, textView);

        int line = (height - firstH)/otherH + 1;
        return line;
    }

    private int getLineHeight(int i, TextView textView) {
        Rect rect = new Rect();
        textView.getLineBounds(i, rect);
        return rect.bottom - rect.top;
    }
}
