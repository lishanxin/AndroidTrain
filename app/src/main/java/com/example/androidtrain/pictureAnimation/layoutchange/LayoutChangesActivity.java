package com.example.androidtrain.pictureAnimation.layoutchange;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.MainActivity;
import com.example.androidtrain.R;

import org.w3c.dom.Text;

public class LayoutChangesActivity extends BaseActivity {

    //Because mContainerView has android:animateLayoutChanges set to true,
    // adding this view is automatically animated.

    private static final String TAG = "LayoutChanges";

    ViewGroup mContainerView;

    private static final String[] COUNTRYS = {"Belgium", "France", "Italy", "Germany", "Spain",
            "Austria", "Russia", "Poland", "Croatia", "Greece",
            "Ukraine",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_changes);

        mContainerView = findViewById(R.id.layout_changes_container);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        super.onCreatePanelMenu(featureId, menu);
        getMenuInflater().inflate(R.menu.layout_changes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //跳转至主界面
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));

                return true;
            case R.id.layout_changes_add_new:
                findViewById(R.id.layout_changes_empty).setVisibility(View.GONE);
                addItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addItem() {
        final View newItem = getLayoutInflater().inflate(R.layout.layout_changes_item, mContainerView, false);
        ((TextView)newItem.findViewById(R.id.layout_changes_item_text))
                .setText(COUNTRYS[(int)(Math.random() * (COUNTRYS.length - 1))]);
        mContainerView.addView(newItem);

        newItem.findViewById(R.id.layout_changes_item_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContainerView.removeView(newItem);
                        Log.d(TAG, "child view count: " + mContainerView.getChildCount());
                        if (mContainerView.getChildCount() == 1){
                            findViewById(R.id.layout_changes_empty).setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
