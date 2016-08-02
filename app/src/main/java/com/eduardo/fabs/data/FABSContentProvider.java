package com.eduardo.fabs.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.eduardo.fabs.utils.UserCategory;

public class FABSContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private FABSDbHelper dbHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int MOVIE_WITH_USER_CATEGORY = 102;

    private static final SQLiteQueryBuilder sqLiteQueryBuilder;

    static {
        sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(FABSContract.MOVIES_TABLE.TABLE_NAME);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FABSContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FABSContract.MOVIES_TABLE.TABLE_NAME, MOVIE);
        matcher.addURI(authority, FABSContract.MOVIES_TABLE.TABLE_NAME + "/#", MOVIE_WITH_ID);
        matcher.addURI(authority, FABSContract.MOVIES_TABLE.TABLE_NAME + "/*", MOVIE_WITH_USER_CATEGORY);
        return matcher;
    }

    private static final String sUserCategorySelection = FABSContract.MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MOVIES_TABLE.COLUMN_USER_CATEGORY + " = ? ";
    private static final String sIDSelection = FABSContract.MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MOVIES_TABLE._ID + " = ? ";

    public FABSContentProvider() {}

    private Cursor getMovieByID(Uri uri, String[] projection, String sortOrder){
        String _id = FABSContract.MOVIES_TABLE.getMovieIDFromUri(uri);
        return sqLiteQueryBuilder.query(dbHelper.getReadableDatabase(), projection, sIDSelection, new String[]{_id}, null, null, sortOrder);
    }

    private Cursor getMovieByUserCategory(Uri uri, String[] projection, String sortOrder){
        UserCategory userCategory = FABSContract.MOVIES_TABLE.getUserCategoryFromUri(uri);
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
            case MOVIE:
                rowsDeleted = db.delete(FABSContract.MOVIES_TABLE.TABLE_NAME, selection, selectionArgs);
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
            case MOVIE:
                return FABSContract.MOVIES_TABLE.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return FABSContract.MOVIES_TABLE.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_USER_CATEGORY:
                return FABSContract.MOVIES_TABLE.CONTENT_TYPE;
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
            case MOVIE:
                long _id = db.insert(FABSContract.MOVIES_TABLE.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FABSContract.MOVIES_TABLE.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
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
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FABSContract.MOVIES_TABLE.TABLE_NAME, null, value);
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
            case MOVIE_WITH_ID:
                returnCursor = getMovieByID(uri, projection, sortOrder);
                break;
            case MOVIE_WITH_USER_CATEGORY:
                returnCursor = getMovieByUserCategory(uri, projection, sortOrder);
                break;
            case MOVIE:
                returnCursor = dbHelper.getReadableDatabase().query(FABSContract.MOVIES_TABLE.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
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
            case MOVIE:
                rowsUpdated = db.update(FABSContract.MOVIES_TABLE.TABLE_NAME, values, selection, selectionArgs);
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
