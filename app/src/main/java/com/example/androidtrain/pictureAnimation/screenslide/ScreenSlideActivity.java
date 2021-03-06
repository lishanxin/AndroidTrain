package com.example.androidtrain.pictureAnimation.screenslide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;

public class ScreenSlideActivity extends BaseActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    public static final int NUM_PAGES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //步骤1：设置Activity包含ViewPager布局
        setContentView(R.layout.activity_screen_slide);

        //步骤3：把PagerAdapter和ViewPager关联起来。
        mPager = (ViewPager) findViewById(R.id.screen_slide_viewpager);
        mPagerAdapter = new ScreenSlideAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        //步骤5：用PageTransformer自定义动画。需实现ViewPager.PageTransformer接口，
        //这个接口只暴露了一个方法，transformPage()。
        //在transformPage()的实现中，基于当前屏幕显示的页面的position（position
        // 由transformPage()方法的参数给出）决定哪些页面需要被动画转换
        //position表示特定界面主轴start位置相对于屏幕中间位置而言
        //当某一页面填充屏幕，它的值为0。当页面刚向屏幕右侧方向被拖走，它的值为1。
        // 如果用户在页面1和页面2间滑动到一半，那么页面1的position为-0.5并且页面2的
        // position为 0.5
//        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setPageTransformer(true, new DepthPageTransformer());
    }

    //步骤4：处理Back按钮，按下变为在虚拟的Fragment栈中回退。
    // 如果用户已经在第一个页面了，则在Activity的回退栈（back stack）中回退。


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0 ){
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }else {
            // Otherwise, select the previous step
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    //步骤2：创建一个继承自FragmentStatePagerAdapter抽象类的类，
    // 然后实现getItem()方法来把ScreenSlidePageFragment实例作为
    // 新页面补充进来。PagerAdapter还需要实现getCount()方法，它返
    // 回 Adapter将要创建页面的总数
    private class ScreenSlideAdapter extends FragmentStatePagerAdapter{

        public ScreenSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
