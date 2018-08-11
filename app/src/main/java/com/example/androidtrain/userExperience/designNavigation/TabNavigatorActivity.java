package com.example.androidtrain.userExperience.designNavigation;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.androidtrain.R;

public class TabNavigatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_navigator);

        final ActionBar actionBar = getSupportActionBar();

        //制定在action bar中显示tab
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //创建一个tab listener， 在用户切换tab时调用
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                 // 显示指定的tab
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                //隐藏指定的tab
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // 可以忽略这个事件
            }
        };

        //添加3个tab，并指定tab的文字和TabListener
        for (int i = 0; i < 3; i++){
            actionBar.addTab(
                    actionBar.newTab().setText("Tab" + (i + 1)).setTabListener(tabListener)
            );
        }
    }
}
