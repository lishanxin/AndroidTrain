package com.example.androidtrain.bestUserInput;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.androidtrain.R;
import com.example.androidtrain.bestUserInput.gestures.DragAndScaleActivity;
import com.example.androidtrain.bestUserInput.gestures.DragAndScaleView;
import com.example.androidtrain.bestUserInput.gestures.MainGesturesActivity;
import com.example.androidtrain.bestUserInput.gestures.MovementGesturesActivity;

public class BestUserInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_user_input);
    }

    private void goToActivity(Class activity){
        Intent intent = new Intent(BestUserInputActivity.this, activity);
        startActivity(intent);
    }
    public void mainGestures(View view) {
        goToActivity(MainGesturesActivity.class);
    }


    public void MovementGestures(View view) {
        goToActivity(MovementGesturesActivity.class);
    }

    public void dragAndScaleGestures(View view) {
        goToActivity(DragAndScaleActivity.class);
    }
}
