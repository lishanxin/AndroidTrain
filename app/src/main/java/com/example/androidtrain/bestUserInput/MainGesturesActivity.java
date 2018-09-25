package com.example.androidtrain.bestUserInput;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.androidtrain.R;

public class MainGesturesActivity extends AppCompatActivity {

    private static final String TAG = "MainGesturesA";
    private static final String TAGB = "MainGesturesB";

    private Button mTestButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gestures);

        mTestButton1 = (Button) findViewById(R.id.main_gestures_button_test1);
        mTestButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAGB, "Action was DOWN");
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAGB, "Action was MOVE");
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAGB, "Action was UP");
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        Log.d(TAGB, "Action was CANCEL");
                        return true;
                    case MotionEvent.ACTION_OUTSIDE:
                        Log.d(TAGB, "Action was OUTSIDE" +
                                "Movement occurred outside bounds of current screen element");
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    //MotionEventCompat并不是MotionEvent的替代品，而是提供了一些静态工具类函数。我们可以
    // 把MotionEvent对象作为参数传递给这些工具类函数，来获得与触摸事件相关的动作(action)。
    /**
     * Called when a touch screen event was not handled by any of the views
     * under it.  This is most useful to process touch events that happen
     * outside of your window bounds, where there is no view to receive it.
     *
     * @param event The touch screen event being processed.
     * @return Return true if you have consumed the event, false if you haven't.
     * The default implementation always returns false.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "Action was DOWN");
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "Action was MOVE");
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Action was UP");
                return true;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "Action was CANCEL");
                return true;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "Action was OUTSIDE" +
                "Movement occurred outside bounds of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);

        }
    }
}
