package com.example.androidtrain.userExperience.notifyUser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.androidtrain.R;

public class NotifyResultWithoutStackActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "extra_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_result_without_stack);
    }
}
