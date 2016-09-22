package com.eduardo.fabs.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.model.MovieModel;
import com.eduardo.fabs.model.UserCategory;
import com.eduardo.fabs.model.data.FABSContract;
import com.eduardo.fabs.model.fetch.FetchMovies;
import com.eduardo.fabs.presenter.miscellany.CursorRecyclerAdapter;
import com.eduardo.fabs.view.SearchResultsActivity;
import com.eduardo.fabs.view.movies.DiscoverMoviesActivity;
import com.eduardo.fabs.view.movies.MovieDetailsActivity;
import com.eduardo.fabs.view.movies.MyMoviesActivity;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * Created by Eduardo on 22/09/2016.
 */

public class MovieFragmentMethods {

    public static void filterCursorRecycler(final CursorRecyclerAdapter cursorRecyclerAdapter, final TextView emptyCursorTextView, String query, final Uri uri, final String selection, final String[] columns, final Context context){
        FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                String[] selectionArgs = {charSequence.toString() + "%"};
                return context.getContentResolver().query(uri, columns, selection, selectionArgs, DiscoverMoviesActivity.sortOrder);

            }
        };
        cursorRecyclerAdapter.setFilterQueryProvider(filterQueryProvider);
        cursorRecyclerAdapter.getFilter().filter(query);
        // Execute some code after 0.2 seconds have passed
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cursorRecyclerAdapter.getItemCount()==0){
                    emptyCursorTextView.setVisibility(View.VISIBLE);
                } else {
                    emptyCursorTextView.setVisibility(View.GONE);
                }
            }
        }, 200);
    }

    public static void filterMyCursorRecycler(final CursorRecyclerAdapter cursorRecyclerAdapter, final TextView emptyCursorTextView, String query, final int loader, final Context context){
        // Here is where we are going to implement the filter logic
        FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                final Uri uri = FABSContract.MY_MOVIES_TABLE.CONTENT_URI;
                switch (loader){
                    case 0: {
                        String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                        String[] selectionArgs = {charSequence.toString() + "%"};
                        return context.getContentResolver().query(uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
                    }
                    case 1: {
                        String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY + " = ? AND " + FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                        String[] selectionArgs = {UserCategory.COMPLETED.toString(), charSequence.toString() + "%"};
                        return context.getContentResolver().query(uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
                    }
                    case 2: {
                        String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY + " = ? AND " + FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                        String[] selectionArgs = {UserCategory.PLANTOWATCH.toString(), charSequence.toString() + "%"};
                        return context.getContentResolver().query(uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
                    }
                    default:
                        String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                        String[] selectionArgs = {charSequence.toString() + "%"};
                        return context.getContentResolver().query(uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
                }
            }
        };
        cursorRecyclerAdapter.setFilterQueryProvider(filterQueryProvider);
        cursorRecyclerAdapter.getFilter().filter(query);
        // Execute some code after 0.2 seconds have passed
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cursorRecyclerAdapter.getItemCount()==0){
                    emptyCursorTextView.setVisibility(View.VISIBLE);
                } else {
                    emptyCursorTextView.setVisibility(View.GONE);
                }
            }
        }, 200);
    }

    public static void launchSearchActivity(String query, Context context){
        Intent intent = new Intent(context, SearchResultsActivity.class);
        intent.putExtra(context.getString(R.string.intent_query), query);
        intent.putExtra(context.getString(R.string.intent_activity), DiscoverMoviesActivity.TAG);
        intent.putExtra(context.getString(R.string.intent_sort_order), DiscoverMoviesActivity.sortOrder);
        context.startActivity(intent);
    }

    public static void launchMovieDetailsActivity(View view, Context context){
        TextView textView = (TextView) view.findViewById(R.id.ID);
        String ID = textView.getText().toString();
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(context.getString(R.string.intent_movie_id), ID);
        intent.putExtra(context.getString(R.string.intent_activity), DiscoverMoviesActivity.TAG);
        intent.putExtra(context.getString(R.string.intent_sort_order), DiscoverMoviesActivity.sortOrder);
        context.startActivity(intent);
    }

    public static void launchMyMovieDetailsActivity(View view, Context context){
        TextView textView = (TextView) view.findViewById(R.id.ID);
        String ID = textView.getText().toString();
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(context.getString(R.string.intent_movie_id), ID);
        intent.putExtra(context.getString(R.string.intent_activity), MyMoviesActivity.TAG);
        intent.putExtra(context.getString(R.string.intent_sort_order), MyMoviesActivity.sortOrder);
        context.startActivity(intent);
    }

    public static void loadMoreMovies(int page, String request, Uri contentUri, String prefString, Context context){
        if(page > 100D || DiscoverMoviesActivity.searching){
            // The online database has only up to 100 pages at any time
            return;
        }
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.putInt(prefString, page);
            edit.commit();

            List<MovieModel> movies = new FetchMovies.FetchMoreMoviesTask(request).execute(page).get();
            Vector<ContentValues> moviesVector = new Vector<ContentValues>(movies.size());

            for (int i = 0; i < movies.size(); i++) {
                ContentValues movieValues = new ContentValues();
                String id = movies.get(i).getId();
                String title = movies.get(i).getTitle();
                String overview = movies.get(i).getOverview();
                String poster_image = movies.get(i).getPosterPath();
                String release_date = movies.get(i).getReleaseDate();
                Double popularity = movies.get(i).getPopularity();
                Double vote_average = movies.get(i).getVoteAverage();

                movieValues.put(FABSContract.POPULAR_MOVIES_TABLE._ID, Integer.valueOf(id));
                movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_TITLE, title);
                movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_OVERVIEW, overview);
                movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POSTER_IMAGE, poster_image);
                movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_RELEASE_DATE, release_date);
                movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY, popularity);
                movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_VOTE_AVERAGE, vote_average);

                moviesVector.add(movieValues);
            }

            // add movies to database
            if (moviesVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[moviesVector.size()];
                moviesVector.toArray(cvArray);
                context.getContentResolver().bulkInsert(contentUri, cvArray);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
