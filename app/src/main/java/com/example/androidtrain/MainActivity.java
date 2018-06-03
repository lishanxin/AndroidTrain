package com.example.androidtrain;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.androidtrain.connectApp.ConnectAppActivity;
import com.example.androidtrain.fragment.HeadlinesFragment;
import com.example.androidtrain.sql.SqlTestActivity;

public class MainActivity extends FragmentActivity  {

    public static final String TAG = "MainActivity";

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Send button */
    public void sendMessageActivity(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void sendMessageFragment(View view){
        Intent intent = new Intent(this, ShowExchangeFragment.class);
        intent.putExtra(EXTRA_MESSAGE, "from MainActivity");
        startActivity(intent);
    }

    public void goToSqlTest(View view){
        Intent intent = new Intent(this, SqlTestActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "from MainActivity");
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //为ActionBar扩展菜单项
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //处理动作按钮的点击事件
        switch (item.getItemId()){
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {
        getActionBar().hide();
        Log.d(TAG, "action_settings");

    }

    private void openSearch() {

        Log.d(TAG, "action_search");

    }

    public void goToConnectOtherAppTest(View view) {
        Intent intent = new Intent(this, ConnectAppActivity.class);
        startActivity(intent);
    }
}
