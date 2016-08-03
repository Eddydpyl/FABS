package com.eduardo.fabs.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.eduardo.fabs.utils.Constants;
import com.eduardo.fabs.utils.UserCategory;

/**
 * Created by Eduardo on 02/08/2016.
 */

public class FABSContract {

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + Constants.CONTENT_AUTHORITY);

    public static final class MY_MOVIES_TABLE implements BaseColumns {

        public static final String TABLE_NAME = "my_movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // THE TITLE OF THE MOVIE
        public static final String COLUMN_TITLE = "title";

        // A SHORT DESCRIPTION OF THE MOVIE
        public static final String COLUMN_OVERVIEW = "overview";

        // THE POSTER IMAGE OF THE MOVIE
        public static final String COLUMN_POSTER_IMAGE = "poster_image";

        // THE RELEASE DATE OF THE MOVIE
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // POPULARITY OF THE MOVIE
        public static final String COLUMN_POPULARITY = "popularity";

        // VOTE AVERAGE
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // USER CATEGORY
        public static final String COLUMN_USER_CATEGORY = "user_category";

        // USER RATING
        public static final String COLUMN_USER_RATING = "user_rating";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIDFromUri(Uri uri) {
            return uri.getQueryParameter(_ID);
        }

        public static Uri buildMovieUriWithUserCategory(UserCategory userCategory){
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_USER_CATEGORY, userCategory.toString()).build();
        }

        public static UserCategory getUserCategoryFromUri(Uri uri){
            return UserCategory.valueOf(uri.getQueryParameter(COLUMN_USER_CATEGORY));
        }
    }

    public static final class POPULAR_MOVIES_TABLE implements BaseColumns {

        public static final String TABLE_NAME = "popular_movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // THE TITLE OF THE MOVIE
        public static final String COLUMN_TITLE = "title";

        // A SHORT DESCRIPTION OF THE MOVIE
        public static final String COLUMN_OVERVIEW = "overview";

        // THE POSTER IMAGE OF THE MOVIE
        public static final String COLUMN_POSTER_IMAGE = "poster_image";

        // THE RELEASE DATE OF THE MOVIE
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // POPULARITY OF THE MOVIE
        public static final String COLUMN_POPULARITY = "popularity";

        // VOTE AVERAGE
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TOP_RATED_MOVIES_TABLE implements BaseColumns {

        public static final String TABLE_NAME = "top_rated_movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // THE TITLE OF THE MOVIE
        public static final String COLUMN_TITLE = "title";

        // A SHORT DESCRIPTION OF THE MOVIE
        public static final String COLUMN_OVERVIEW = "overview";

        // THE POSTER IMAGE OF THE MOVIE
        public static final String COLUMN_POSTER_IMAGE = "poster_image";

        // THE RELEASE DATE OF THE MOVIE
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // POPULARITY OF THE MOVIE
        public static final String COLUMN_POPULARITY = "popularity";

        // VOTE AVERAGE
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class UPCOMING_MOVIES_TABLE implements BaseColumns {

        public static final String TABLE_NAME = "upcoming_movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // THE TITLE OF THE MOVIE
        public static final String COLUMN_TITLE = "title";

        // A SHORT DESCRIPTION OF THE MOVIE
        public static final String COLUMN_OVERVIEW = "overview";

        // THE POSTER IMAGE OF THE MOVIE
        public static final String COLUMN_POSTER_IMAGE = "poster_image";

        // THE RELEASE DATE OF THE MOVIE
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // POPULARITY OF THE MOVIE
        public static final String COLUMN_POPULARITY = "popularity";

        // VOTE AVERAGE
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class NOW_IN_THEATERS_MOVIES_TABLE implements BaseColumns {

        public static final String TABLE_NAME = "now_in_theaters_movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // THE TITLE OF THE MOVIE
        public static final String COLUMN_TITLE = "title";

        // A SHORT DESCRIPTION OF THE MOVIE
        public static final String COLUMN_OVERVIEW = "overview";

        // THE POSTER IMAGE OF THE MOVIE
        public static final String COLUMN_POSTER_IMAGE = "poster_image";

        // THE RELEASE DATE OF THE MOVIE
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // POPULARITY OF THE MOVIE
        public static final String COLUMN_POPULARITY = "popularity";

        // VOTE AVERAGE
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
