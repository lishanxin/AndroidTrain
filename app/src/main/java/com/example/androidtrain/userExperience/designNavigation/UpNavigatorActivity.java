package com.example.androidtrain.userExperience.designNavigation;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.androidtrain.R;

public class UpNavigatorActivity extends AppCompatActivity {

    private static final String TAG = "UpNavigator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_navigator);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //对action bar的Up/home按钮做出反应
            case android.R.id.home:
                Log.d(TAG, "item home");
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this,upIntent)){
                    //这个activity不是这个app任务的一部分，所以当向上导航时创建
                    //用合同后退栈（synthesized back stack)创建一个新任务
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                }else {
                    //这个activity时这个app任务的一部分，所以向上导航至逻辑父activity
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
