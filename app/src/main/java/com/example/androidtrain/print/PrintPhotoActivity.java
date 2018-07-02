package com.example.androidtrain.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.androidtrain.R;

import java.util.ArrayList;
import java.util.List;

public class PrintPhotoActivity extends Activity {

    private final static String TAG = "PrintPhoto";

    private WebView mWebView;
    private List<PrintJob> mPrintJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_photo);

        mPrintJobs = new ArrayList<>();

    }

    public void printPhoto(View view) {
        doPhotoPrint();
    }

    public void printHTML(View view) {
        doWebViewPrint();
    }

    public void printDefineFile(View view) {
    }

    private void doPhotoPrint(){
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }

    private void doWebViewPrint(){
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                url = "https://www.baidu.com";
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "page finished loading " + url);
                createWebPrintJob(view);
                mWebView = null;
            }
        });

        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, Testing, Testing</p></body></html>";
//        webView.loadDataWithBaseURL("file:///android_asset/images/iu.jpg", htmlDocument, "text/Html", "UTF-8", null);
        webView.loadUrl("https://www.baidu.com");
        mWebView = webView;
    }

    private void createWebPrintJob(WebView view) {

        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        if (Build.VERSION.SDK_INT >= 19){
            try {
                PrintDocumentAdapter printAdapter = view.createPrintDocumentAdapter();
                String jobName = getString(R.string.app_name) + "Document";
                PrintJob printJob = printManager.print(jobName, printAdapter,
                        new PrintAttributes.Builder().build());

                mPrintJobs.add(printJob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
