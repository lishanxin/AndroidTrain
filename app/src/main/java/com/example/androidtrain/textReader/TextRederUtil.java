package com.example.androidtrain.textReader;

import android.graphics.Rect;
import android.widget.TextView;

/**
 * Created by sx on 2018/7/26.
 */

public class TextRederUtil {
    public static int[] getPage( TextView textView){
        int count = textView.getLineCount();

        int pageLine = getPageLineNumber(textView);
        int pageNum = count/pageLine;
        if (count%pageLine > 0){
            pageNum++;
        }
        int page[] = new int[pageNum];
        for(int i=0;i<pageNum - 1;i++){
            page[i]=textView.getLayout().getLineEnd((i+1)*pageLine - 1);
        }
        page[pageNum - 1] = textView.getText().toString().length();
        return page;
    }


    private static int getPageLineNumber(TextView textView){

        int height = textView.getBottom() - textView.getTop() - textView.getPaddingTop();

        //第一页与其他页的高度不一样
        int firstH = getLineHeight(0, textView);
        int otherH = getLineHeight(1, textView);

        int line = (height - firstH)/otherH + 1;
        return line;
    }

    private static int getLineHeight(int i, TextView textView) {
        Rect rect = new Rect();
        textView.getLineBounds(i, rect);
        return rect.bottom - rect.top;
    }
//
//    public static void setCharacterWidth(TextView textView) {
//        textView.setMinEms((int)textView.getPaint().measureText("王"));
//    }

//    public static String ToDBC(String input) {
//        char[] c = input.toCharArray();
//        for (int i = 0; i< c.length; i++) {
//            if (c[i] == 12288) {
//                c[i] = (char) 32;
//                continue;
//            }if (c[i]> 65280&& c[i]< 65375)
//                c[i] = (char) (c[i] - 65248);
//        }
//        return new String(c);
//    }
}
