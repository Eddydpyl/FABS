package com.eduardo.fabs.fetch;

import android.content.Context;
import android.os.AsyncTask;

import com.eduardo.fabs.models.MovieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 03/08/2016.
 */

public class FetchMovies {

    // Updates all local databases
    public static void updateData(){

    }

    // Searches for movies in the online movie database
    public class FetchOnlineMoviesTask extends AsyncTask<String, Void, List<MovieModel>> {

        private Context mContext;

        public FetchOnlineMoviesTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<MovieModel> doInBackground(String... searchQuery) {
            return new ArrayList<>();
        }
    }

    // Searches for movies in the local movie database
    public class FetchLocalMoviesTask extends AsyncTask<String, Void, List<MovieModel>> {

        private Context mContext;

        public FetchLocalMoviesTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<MovieModel> doInBackground(String... searchQuery) {
            return new ArrayList<>();
        }
    }

}
