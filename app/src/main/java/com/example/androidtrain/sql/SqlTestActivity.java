package com.example.androidtrain.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidtrain.R;

public class SqlTestActivity extends Activity {

    private static final String TAG = "SqlTestActivity";

    TextView textView;

    EditText editText;

    FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_test);

        initSql();
        editText = (EditText) findViewById(R.id.sql_edit_test);
        textView = (TextView) findViewById(R.id.sql_show_test);

    }

    private void initSql() {

        mDbHelper = new FeedReaderDbHelper(this);
    }

    public void storeText(View view) {
        String storeMessage = editText.getText().toString();


        //Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, storeMessage);

        long newRowId;
        newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        Log.d(TAG, "Store id: " + newRowId);
    }

    public void showText(View view) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //return values
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
        };

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                null, null, null, null,null

        );
    }
}
