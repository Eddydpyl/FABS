package com.eduardo.fabs.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.eduardo.fabs.utils.Constants;
import com.eduardo.fabs.utils.UserCategory;

public class FABSContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private FABSDbHelper dbHelper;

    static final int MY_MOVIE = 100;
    static final int MY_MOVIE_WITH_ID = 101;
    static final int MY_MOVIE_WITH_USER_CATEGORY = 102;
    static final int POPULAR_MOVIE = 103;
    static final int TOP_RATED_MOVIE = 104;;
    static final int UPCOMING_MOVIE = 105;
    static final int NOW_IN_THEATERS_MOVIE = 106;


    private static final SQLiteQueryBuilder sqLiteQueryBuilder;

    static {
        sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(FABSContract.MY_MOVIES_TABLE.TABLE_NAME);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Constants.CONTENT_AUTHORITY;
        matcher.addURI(authority, FABSContract.MY_MOVIES_TABLE.TABLE_NAME, MY_MOVIE);
        matcher.addURI(authority, FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "/#", MY_MOVIE_WITH_ID);
        matcher.addURI(authority, FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "/*", MY_MOVIE_WITH_USER_CATEGORY);
        matcher.addURI(authority, FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME, POPULAR_MOVIE);
        matcher.addURI(authority, FABSContract.TOP_RATED_MOVIES_TABLE.TABLE_NAME, TOP_RATED_MOVIE);
        matcher.addURI(authority, FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME, UPCOMING_MOVIE);
        matcher.addURI(authority, FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME, NOW_IN_THEATERS_MOVIE);
        return matcher;
    }

    private static final String sUserCategorySelection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY + " = ? ";
    private static String sIDSelection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE._ID + " = ? ";

    public FABSContentProvider() {}

    private Cursor getMovieByID(Uri uri, String[] projection, String sortOrder){
        String _id = FABSContract.MY_MOVIES_TABLE.getMovieIDFromUri(uri);
        return sqLiteQueryBuilder.query(dbHelper.getReadableDatabase(), projection, sIDSelection, new String[]{_id}, null, null, sortOrder);
    }

    private Cursor getMovieByUserCategory(Uri uri, String[] projection, String sortOrder){
        UserCategory userCategory = FABSContract.MY_MOVIES_TABLE.getUserCategoryFromUri(uri);
        return sqLiteQueryBuilder.query(dbHelper.getReadableDatabase(), projection, sUserCategorySelection, new String[]{userCategory.toString()}, null, null, sortOrder);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match){
            case MY_MOVIE:
                rowsDeleted = db.delete(FABSContract.MY_MOVIES_TABLE.TABLE_NAME, selection, selectionArgs);
                break;
            case POPULAR_MOVIE:
                rowsDeleted = db.delete(FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIE:
                rowsDeleted = db.delete(FABSContract.TOP_RATED_MOVIES_TABLE.TABLE_NAME, selection, selectionArgs);
                break;
            case UPCOMING_MOVIE:
                rowsDeleted = db.delete(FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME, selection, selectionArgs);
                break;
            case NOW_IN_THEATERS_MOVIE:
                rowsDeleted = db.delete(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case MY_MOVIE:
                return FABSContract.MY_MOVIES_TABLE.CONTENT_TYPE;
            case MY_MOVIE_WITH_ID:
                return FABSContract.MY_MOVIES_TABLE.CONTENT_ITEM_TYPE;
            case MY_MOVIE_WITH_USER_CATEGORY:
                return FABSContract.MY_MOVIES_TABLE.CONTENT_TYPE;
            case POPULAR_MOVIE:
                return FABSContract.POPULAR_MOVIES_TABLE.CONTENT_TYPE;
            case TOP_RATED_MOVIE:
                return FABSContract.TOP_RATED_MOVIES_TABLE.CONTENT_TYPE;
            case UPCOMING_MOVIE:
                return FABSContract.UPCOMING_MOVIES_TABLE.CONTENT_TYPE;
            case NOW_IN_THEATERS_MOVIE:
                return FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MY_MOVIE:{
                long _id = db.insert(FABSContract.MY_MOVIES_TABLE.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FABSContract.MY_MOVIES_TABLE.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case POPULAR_MOVIE:{
                long _id = db.insert(FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FABSContract.POPULAR_MOVIES_TABLE.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TOP_RATED_MOVIE:{
                long _id = db.insert(FABSContract.TOP_RATED_MOVIES_TABLE.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FABSContract.TOP_RATED_MOVIES_TABLE.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case UPCOMING_MOVIE:{
                long _id = db.insert(FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FABSContract.UPCOMING_MOVIES_TABLE.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case NOW_IN_THEATERS_MOVIE:{
                long _id = db.insert(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match){
            case MY_MOVIE:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FABSContract.MY_MOVIES_TABLE.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case POPULAR_MOVIE:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case TOP_RATED_MOVIE:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FABSContract.TOP_RATED_MOVIES_TABLE.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case UPCOMING_MOVIE:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case NOW_IN_THEATERS_MOVIE:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FABSDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        int match = uriMatcher.match(uri);
        switch (match){
            case MY_MOVIE_WITH_ID:
                returnCursor = getMovieByID(uri, projection, sortOrder);
                break;
            case MY_MOVIE_WITH_USER_CATEGORY:
                returnCursor = getMovieByUserCategory(uri, projection, sortOrder);
                break;
            case MY_MOVIE:
                returnCursor = dbHelper.getReadableDatabase().query(FABSContract.MY_MOVIES_TABLE.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case POPULAR_MOVIE:
                returnCursor = dbHelper.getReadableDatabase().query(FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TOP_RATED_MOVIE:
                returnCursor = dbHelper.getReadableDatabase().query(FABSContract.TOP_RATED_MOVIES_TABLE.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case UPCOMING_MOVIE:
                returnCursor = dbHelper.getReadableDatabase().query(FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NOW_IN_THEATERS_MOVIE:
                returnCursor = dbHelper.getReadableDatabase().query(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case MY_MOVIE:
                rowsUpdated = db.update(FABSContract.MY_MOVIES_TABLE.TABLE_NAME, values, selection, selectionArgs);
                break;
            case POPULAR_MOVIE:
                rowsUpdated = db.update(FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIE:
                rowsUpdated = db.update(FABSContract.TOP_RATED_MOVIES_TABLE.TABLE_NAME, values, selection, selectionArgs);
                break;
            case UPCOMING_MOVIE:
                rowsUpdated = db.update(FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOW_IN_THEATERS_MOVIE:
                rowsUpdated = db.update(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
