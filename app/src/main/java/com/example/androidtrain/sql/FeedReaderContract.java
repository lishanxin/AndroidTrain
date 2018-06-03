package com.example.androidtrain.sql;

import android.provider.BaseColumns;

/**
 * Created by lizz on 2018/6/3.
 */

public class FeedReaderContract {

    public FeedReaderContract(){}

    /*Inner class tha defines  the table contents */
    public static abstract class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
