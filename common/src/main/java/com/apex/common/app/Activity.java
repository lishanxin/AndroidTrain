package com.apex.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by sx on 2018/11/20.
 */

public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (initArgs(getIntent().getExtras())){
            initWindow();
            int layId = getContentLayoutId();
            setContentView(layId);
            initWidget();
            initData();
        }else {
            finish();
        }
    }

    public abstract int getContentLayoutId();
    /**
     * 初始化窗口
     */
    protected void initWindow(){

    }

    /**
     * 初始化参数
     * @param bundle 传入的参数
     * @return
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 初始化控件
     */
    protected void initWidget(){

    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }


    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        //得到当前Activity下的所有Fragment
        List<Fragment> fragments =getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments!=null && fragments.size()>0)
        for (Fragment fragment: fragments){
            //判断是否为我们能够处理的Fragment类型
            if (fragment instanceof com.apex.common.app.Fragment){
                //如果返回逻辑已经被处理，则直接return，不进行其他操作
                if (((com.apex.common.app.Fragment)fragment).onBackPressed()){
                    return;
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
