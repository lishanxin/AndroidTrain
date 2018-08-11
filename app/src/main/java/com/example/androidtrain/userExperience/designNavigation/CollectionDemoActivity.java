package com.example.androidtrain.userExperience.designNavigation;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidtrain.R;

//Tab导航与viewPager自身title
public class CollectionDemoActivity extends AppCompatActivity {

    //当被请求时，这个Adapter会返回一个DemoObjectFragment，
    //代表在对象集中的一个对象
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_collection_demo);


        // ViewPager和他的adapter使用了support library fragment，
        // 所以要用getSupportFragmentManager
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
                getSupportFragmentManager()
        );
        mViewPager = (ViewPager) findViewById(R.id.navigator_collection_view_pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);

        final ActionBar actionBar = getSupportActionBar();

        //制定在action bar中显示tab
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //监听tab来替换ViewPager
        //创建一个tab listener， 在用户切换tab时调用
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // 显示指定的tab
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                //隐藏指定的tab
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // 可以忽略这个事件
                if (mViewPager.getCurrentItem() != tab.getPosition())
                mViewPager.setCurrentItem(tab.getPosition());
            }
        };

        //添加3个tab，并指定tab的文字和TabListener
        for (int i = 0; i < 3; i++){
            actionBar.addTab(
                    actionBar.newTab().setText("Tab" + (i + 1)).setTabListener(tabListener)
            );
        }

        //监听ViewPager来替换tab
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当滑屏切换页面时，选择相应的tab
                if (position < getSupportActionBar().getTabCount())
                    getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        // Set up action bar.
//        final ActionBar actionBar = getActionBar();
//
//        // Specify that the Home button should show an "Up" caret, indicating that touching the
//        // button will take the user one step up in the application's hierarchy.
//        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    // 有固定数量的Fragment用FragmentPagerAdapter，否则用FragmentStatePagerAdapter。
    //FragmentStatePagerAdapter会自动回收不使用的Fragment.
    //因为这是一个对象集，所以使用FragmentStatePagerAdapter
    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter{
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();

            // 我们的对象只是一个整数： -P
            args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    //这个类的实例是一个代表了数据集中一个对象的fragment
    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //最后两个参数保证LayoutParam能被正确填充
            View rootView = inflater.inflate(R.layout.fragment_navigator_view_pager_item, container, false);
            Bundle args = getArguments();
            ((TextView)rootView.findViewById(R.id.fragment_navigator_view_pager_item_text_view))
                    .setText(Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }
}
