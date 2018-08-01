package com.example.androidtrain.pictureAnimation.cardflip;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.MainActivity;
import com.example.androidtrain.R;

public class CardFlipActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "CardFlipAct";

    private static final int MENU_ID = 101;

    /**
     * A handler object, used for deferring UI operations.
     */
    private Handler mHandler = new Handler();

    /**
     * Whether or not we're showing the back of the card (otherwise showing the front).
     */
    private boolean mShowingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.card_flip_container, new CardFrontFragment())
                    .commit();
        }else {
            int stackCount = getFragmentManager().getBackStackEntryCount();
            mShowingBack = (stackCount > 0);
        }

        // Monitor back stack changes to ensure the action bar shows the appropriate
        // button (either "photo" or "info").
        //监听堆栈的变化，以显示正确的按钮
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Add either a "photo" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, MENU_ID, Menu.NONE,
                mShowingBack
                        ? "照片"
                        : "文本");
        item.setIcon(mShowingBack
                ? R.drawable.ic_action_photo
                : R.drawable.ic_action_info);
        Log.d(TAG, "mShowingBack: " + mShowingBack);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case MENU_ID:
                flipCard();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void flipCard(){
        if (mShowingBack){
            mShowingBack = false;
            getSupportFragmentManager().popBackStack();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    invalidateOptionsMenu();
                }
            });
            return;
        }

        // Flip to the back
        // 向背面翻转
        mShowingBack = true;

        //开启Fragment转换，将Fragment转换成背面的卡片，并使用动画进行翻转，
        //并将其设置为FragmentManager栈空间的一员
        //添加的四个动画效果分别是：向右滑入，向右滑出，向左滑入，向左滑出
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.card_flip_container, new CardBackFragment())

                .addToBackStack(null)
                .commit();


        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "stackCount" + getFragmentManager().getBackStackEntryCount());
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }


    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    /**
     * A fragment representing the back of the card
     *
     */
    public static class CardBackFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }
}
