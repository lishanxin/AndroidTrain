package com.example.androidtrain.userExperience.designNavigation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidtrain.R;

public class NavDrawerActivity extends Activity {

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private ActionBarDrawerToggle mDrawerToggle;
    private static final int MENU_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer_list_view);

        //set Adapter for ListView
        mDrawerList.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.item_nav_drawer,
                mPlanetTitles));
        //set click listener for ListView
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        selectItem(0);

        mTitle = mDrawerTitle = getTitle();

        //监听打开和关闭事件
        addListenerForOpenClose();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Add either a "photo" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, MENU_ID, Menu.NONE,
                        "照片");
        item.setIcon(R.drawable.ic_action_info);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    private void addListenerForOpenClose() {
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                           /** 承载Activity */
                mDrawerLayout,                          /** DrawerLayout 对象 */
                R.drawable.jiguang_socialize_wechat,    /** nav drawer 图标用来替换'Up'符号 */
                R.string.open,                          /* "打开 drawer" 描述 */
                R.string.close                          /* "关闭 drawer" 描述 */
        ){
            /**
             * 当drawer处于完全关闭的状态时调用
             * @param drawerView
             */
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();//创建对onPrepareOptionsMene()的调用
            }

            /**
             * 当drawer处于完全打开的状态时调用
             * @param drawerView
             */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();//创建对onPrepareOptionsMene()的调用
            }
        };
        //添加监听
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //在onRestoreInstanceState发生后，同步触发器状态.显示mDrawerToggle创建时的图标
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //将事件传递给ActionBarDrawerToggle,如果返回true，表示app图标点击事件已经被处理
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //如果nav drawer 时打开的，隐藏于内容视图相关联的action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(MENU_ID).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * 在主内容视图中交换fragment
     * @param position
     */
    private void selectItem(int position) {
        //创建一个新的fragment并且填入相应信息
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.POSITION, position);
        fragment.setArguments(args);

        //通过替换已存在的fragment来插入新的fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_drawer_content_frame,
                fragment).commit();

        //高亮被选择的item，更新标题，并关闭drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    public static class PlanetFragment extends Fragment {

        public static final String POSITION = "POSITION";
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_nav_drawer_content, container, false);
            Bundle bundle = getArguments();
            int position = bundle.getInt(POSITION);
            ((TextView)view.findViewById(R.id.fragment_nav_drawer_textview)).setText(Integer.toString(position + 1));
            return view;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
}
