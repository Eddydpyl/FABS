package com.eduardo.fabs.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Eduardo on 02/08/2016.
 */
public class FABSDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FABS.db";

    public FABSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE_MOVIE = "CREATE TABLE " + FABSContract.MOVIES_TABLE.TABLE_NAME + " (" +
                FABSContract.MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                FABSContract.MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                FABSContract.MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FABSContract.MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                FABSContract.MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FABSContract.MOVIES_TABLE.COLUMN_POPULARITY + " REAL NOT NULL," +
                FABSContract.MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                FABSContract.MOVIES_TABLE.COLUMN_USER_CATEGORY + " TEXT NOT NULL," +
                FABSContract.MOVIES_TABLE.COLUMN_USER_RATING + " REAL NOT NULL " +
                ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FABSContract.MOVIES_TABLE.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
