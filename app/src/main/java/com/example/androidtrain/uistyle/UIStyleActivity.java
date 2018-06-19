package com.example.androidtrain.uistyle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.androidtrain.R;

public class UIStyleActivity extends AppCompatActivity {

    private Dialog allMsg;
    //Dialog的布局View
    private View allMsgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uistyle);

        AlertDialog.Builder builder = new AlertDialog.Builder(UIStyleActivity.this, 0);
        LayoutInflater inflater = UIStyleActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_uistyle, null);
        builder.setView(view);

        //设置背景透明度
        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 0, 0, 0));
        window.setBackgroundDrawable(colorDrawable);
        alertDialog.show();

    }
}
