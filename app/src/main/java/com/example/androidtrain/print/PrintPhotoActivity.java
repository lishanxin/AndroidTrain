package com.example.androidtrain.print;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.Surface;
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
        Log.d(TAG, "onCreate");
        mPrintJobs = new ArrayList<>();

    }

    /**
     * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onCreate
     * @see #onStop
     * @see #onResume
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     * <p>
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {@link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into
     * the background, but has not (yet) been killed.  The counterpart to
     * {@link #onResume}.
     * <p>
     * <p>When activity B is launched in front of activity A, this callback will
     * be invoked on A.  B will not be created until A's {@link #onPause} returns,
     * so be sure to not do anything lengthy here.
     * <p>
     * <p>This callback is mostly used for saving any persistent state the
     * activity is editing, to present a "edit in place" model to the user and
     * making sure nothing is lost if there are not enough resources to start
     * the new activity without first killing this one.  This is also a good
     * place to do things like stop animations and other things that consume a
     * noticeable amount of CPU in order to make the switch to the next activity
     * as fast as possible, or to close resources that are exclusive access
     * such as the camera.
     * <p>
     * <p>In situations where the system needs more memory it may kill paused
     * processes to reclaim resources.  Because of this, you should be sure
     * that all of your state is saved by the time you return from
     * this function.  In general {@link #onSaveInstanceState} is used to save
     * per-instance state in the activity and this method is used to store
     * global persistent data (in content providers, files, etc.)
     * <p>
     * <p>After receiving this call you will usually receive a following call
     * to {@link #onStop} (after the next activity has been resumed and
     * displayed), however in some cases there will be a direct call back to
     * {@link #onResume} without going through the stopped state.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onStop
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onRestart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestart
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onDestroy
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
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
        webView.loadUrl("https://www.youku.com");
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void doCustomPrint(){
        //Get a PrintManager instance
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) + "Document";

        //Start a print job, passing in a PrintDocumentAdapter implementation
        //to handle the generation of a print document
        printManager.print(jobName, new MyPrintDocumentAdapter(this), null);
    }

    /**
     * Called by the system when the device configuration changes while your
     * activity is running.  Note that this will <em>only</em> be called if
     * you have selected configurations you would like to handle with the
     * {@link android.R.attr#configChanges} attribute in your manifest.  If
     * any configuration change occurs that is not selected to be reported
     * by that attribute, then instead of reporting it the system will stop
     * and restart the activity (to have it launched with the new
     * configuration).
     * <p>
     * <p>At the time that this function has been called, your Resources
     * object will have been updated to return resource values matching the
     * new configuration.
     *
     * @param newConfig The new device configuration.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int rotate = getDisplayRotation(this);
        Log.d(TAG, "orientation: " + rotate);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.d(TAG, "ORIENTATION LANDSCAPE");
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d(TAG, "ORIENTATION PORTRAIT");
        }else if (newConfig.orientation == Configuration.ORIENTATION_UNDEFINED){
            Log.d(TAG, "ORIENTATION_UNDEFINED");
        } else if (newConfig.orientation == Configuration.ORIENTATION_SQUARE){
            Log.d(TAG, "ORIENTATION_SQUARE");
        }
        super.onConfigurationChanged(newConfig);
    }

    public static int getDisplayRotation(Activity activity) {
        if(activity == null)
            return 0;

        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }
}
