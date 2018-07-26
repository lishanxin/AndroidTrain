package com.example.androidtrain.media.testVideo;

/**
 * Created by user on 2017/3/7.
 */

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidtrain.R;


public class VideoActivity extends VideoAbsActivity {
    @Override
    protected int onSetVideoLayout() {
        return R.layout.android_video;
    }

    @Override
    protected void act_capture() {
        setButtonStatus(STATUS_RECORDING);
    }

    @Override
    protected void act_change() {

    }

    @Override
    protected void act_stop() {
        setButtonStatus(STATUS_START);
    }

    @Override
    protected void act_save() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        DEBUG=true;


    }

    @Override
    protected void onSetVideoComponent(ComponpentGroup componpentGroup) {
        componpentGroup.mPreview = (TextureView) findViewById(R.id.surface_view);
        componpentGroup.captureButton = (Button) findViewById(R.id.button_capture);
        componpentGroup.stopButton = (Button) findViewById(R.id.button_stop);
        componpentGroup.saveButton = (Button) findViewById(R.id.button_save);
        componpentGroup.changeButton = (Button) findViewById(R.id.button_change);
        componpentGroup.videoPlay = (Button) findViewById(R.id.button_play);
        componpentGroup.recordtime = (TextView)findViewById(R.id.recordtime);
        //话术
        componpentGroup.ruleTalk = (TextView) findViewById(R.id.rule_talk_text_view);
        componpentGroup.pageTip = (TextView)findViewById(R.id.rule_talk_page_tip);
        componpentGroup.nextPageButton = (Button) findViewById(R.id.rule_talk_next_page);
        componpentGroup.lastPageButton = (Button) findViewById(R.id.rule_talk_last_page);
    }

    private void setButtonStatus(int status) {

        switch (status) {
            case STATUS_START:
                componpentGroup.captureButton.setVisibility(View.VISIBLE);
                componpentGroup.stopButton.setVisibility(View.GONE);
                componpentGroup.changeButton.setVisibility(View.VISIBLE);
                if (recorded) {
                    ( (Button)componpentGroup.captureButton).setText("重新录制");
                    componpentGroup.saveButton.setVisibility(View.VISIBLE);
                    componpentGroup.videoPlay.setVisibility(View.VISIBLE);
                    componpentGroup.changeButton.setVisibility(View.GONE);
                } else {
                    ( (Button)componpentGroup.captureButton).setText("录制");
                    componpentGroup.saveButton.setVisibility(View.GONE);
                    componpentGroup.videoPlay.setVisibility(View.GONE);
                    componpentGroup.changeButton.setVisibility(View.VISIBLE);
                }

                break;
            case STATUS_RECORDING:
                ( (Button)componpentGroup.captureButton).setText("录制");
                componpentGroup.captureButton.setVisibility(View.GONE);
                componpentGroup.stopButton.setVisibility(View.VISIBLE);
                componpentGroup.saveButton.setVisibility(View.GONE);
                componpentGroup.changeButton.setVisibility(View.GONE);
                componpentGroup.videoPlay.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


}

