package com.example.androidtrain.buildingconnect.networkOps;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidtrain.R;

import org.w3c.dom.Text;

import java.io.IOException;

public class NetWorkOpsActivity extends AppCompatActivity {

    NetWorkUtil mNetWorkUtil;

    EditText mEditText;

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work_ops);

        mEditText = (EditText)findViewById(R.id.net_work_ops_edit_view);
        mTextView = (TextView)findViewById(R.id.net_work_ops_text_view);
        mNetWorkUtil = new NetWorkUtil(this);
    }

    public void getHttpResponse(View view) {
        String url = mEditText.getText().toString();
        if (mNetWorkUtil.isConnected()){
            new DownloadWebpageText().execute(url);
        }else {
            mTextView.setText("No network connection available.");
        }
    }

    private class DownloadWebpageText extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            try {
                return mNetWorkUtil.downloadUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            mTextView.setText(result);
        }
    }
}
