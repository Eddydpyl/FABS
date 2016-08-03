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

        final String CREATE_TABLE_MY_MOVIES = "CREATE TABLE " + FABSContract.MY_MOVIES_TABLE.TABLE_NAME + " (" +
                FABSContract.MY_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                FABSContract.MY_MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FABSContract.MY_MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                FABSContract.MY_MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FABSContract.MY_MOVIES_TABLE.COLUMN_POPULARITY + " REAL NOT NULL," +
                FABSContract.MY_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY + " TEXT NOT NULL," +
                FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " REAL NOT NULL " +
                ")";
        final String CREATE_TABLE_POPULAR_MOVIES = "CREATE TABLE " + FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME + " (" +
                FABSContract.POPULAR_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                FABSContract.POPULAR_MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                FABSContract.POPULAR_MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                FABSContract.POPULAR_MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY + " REAL NOT NULL," +
                FABSContract.POPULAR_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL NOT NULL " +
                ")";
        final String CREATE_TABLE_TOP_RATED_MOVIES = "CREATE TABLE " + FABSContract.TOP_RATED_MOVIES_TABLE.TABLE_NAME + " (" +
                FABSContract.TOP_RATED_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_POPULARITY + " REAL NOT NULL," +
                FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL NOT NULL " +
                ")";
        final String CREATE_TABLE_UPCOMING_MOVIES = "CREATE TABLE " + FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME + " (" +
                FABSContract.UPCOMING_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_POPULARITY + " REAL NOT NULL," +
                FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL NOT NULL " +
                ")";
        final String CREATE_TABLE_NOW_IN_THEATERS_MOVIES = "CREATE TABLE " + FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME + " (" +
                FABSContract.NOW_IN_THEATERS_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_POPULARITY + " REAL NOT NULL," +
                FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL NOT NULL " +
                ")";

        sqLiteDatabase.execSQL(CREATE_TABLE_MY_MOVIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_POPULAR_MOVIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_TOP_RATED_MOVIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_UPCOMING_MOVIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_NOW_IN_THEATERS_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FABSContract.TOP_RATED_MOVIES_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
