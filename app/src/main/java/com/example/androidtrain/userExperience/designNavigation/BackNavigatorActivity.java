package com.example.androidtrain.userExperience.designNavigation;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.androidtrain.MainActivity;
import com.example.androidtrain.R;

/**
 * 如若必要，才需要手动设置back Nav
 * 向后导航(Back navigation)是用户根据屏幕历史记录返回之前所查看的界面
 * 在几乎所有情况下,系统可以正确地向后导航.
 * 有少数几种情况需要手动指定app的后退操作，来提供更好的用户体验。
 * 手动指定后退操作需要的导航模式:
 ``1:当用户从notification(通知)，app widget，navigation drawer直接进入深层次activity。
    2:用户在fragment之间切换的某些情况。
    3:当用户在WebView中对网页进行导航。
 *
 * 例如，当用户从通知进入你的app中的深层activity时，你应该添加别的activity到你的任务的后退栈中，
 * 这样当点击后退(Back)时向上导航，而不是退出app。
 */
public class BackNavigatorActivity extends AppCompatActivity {

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_navigator);
    }

    //通知
    public void backNavFromNotification(){
        // 当用户选择通知时，启动activity的intent
        Intent detailsIntent = new Intent(this, MainActivity.class);

        // 使用TaskStackBuilder创建后退栈，并获取PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        // 添加所有MainActivity的父activity到栈中,
                        // 然后再添加MainActivity自己
                .addNextIntentWithParentStack(detailsIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);

    }

    //网页
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }

        // 否则遵从系统的默认操作.
        super.onBackPressed();
    }


    //Fragment
    public void backNavFromFragment(){
        getSupportFragmentManager().beginTransaction()
                .add(new Fragment(), "detail")
                //提交这一事务到后退栈中
                .addToBackStack("A")
                .commit();
    }
}
