package com.eduardo.fabs.fetch;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.models.MovieModel;
import com.eduardo.fabs.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Eduardo on 03/08/2016.
 */

public class FetchMovies {

    public static final String LOG_TAG = FetchMovies.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    public static final String RESULTS = "results";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String OVERVIEW = "overview";
    public static final String POSTER_IMAGE = "poster_path";
    public static final String RELEASE_DATE = "release_date";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String ADULT = "adult";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String BELONGS_TO_COLLECTION = "belongs_to_collection";
    public static final String BUDGET = "budget";
    public static final String GENRESA = "genres";
    public static final String GENRESA_ID = "id";
    public static final String GENRESA_NAME = "name";
    public static final String GENRESB = "genre_ids";
    public static final String HOMEPAGE = "homepage";
    public static final String IMDB_ID = "imdb_id";
    public static final String ORIGINAL_LANGUAGE = "original_language";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String PRODUCTION_COMPANIES = "production_companies";
    public static final String PRODUCTION_COMPANY_ID = "id";
    public static final String PRODUCTION_COMPANY_NAME = "name";
    public static final String PRODUCTION_COUNTRIES = "production_countries";
    public static final String PRODUCTION_COUNTRIES_ISO = "iso_3166_1";
    public static final String PRODUCTION_COUNTRIES_NAME = "name";
    public static final String REVENUE = "revenue";
    public static final String RUNTIME = "runtime";
    public static final String SPOKEN_LANGUAGES = "spoken_languages";
    public static final String SPOKEN_LANGUAGES_ISO = "iso_639_1";
    public static final String SPOKEN_LANGUAGES_NAME = "name";
    public static final String STATUS = "status";
    public static final String TAGLINE = "tagline";
    public static final String VIDEO = "video";
    public static final String VOTE_COUNT = "vote_count";

    // Updates all local databases
    public static void updateData(Context context){
        final String popularMoviesStr = Constants.TMDBConstants.BASE_URL + Constants.TMDBConstants.MOVIE_TAG + "/"
                + Constants.TMDBConstants.REQUEST_POPULAR + "?" + Constants.TMDBConstants.API_KEY_QUERY_PARAM
                + Constants.TMDBConstants.API_KEY;
        final List<MovieModel> popularMovies = fetchMovies(popularMoviesStr);
        final String topRatedMoviesStr = Constants.TMDBConstants.BASE_URL + Constants.TMDBConstants.MOVIE_TAG + "/"
                + Constants.TMDBConstants.REQUEST_TOP_RATED + "?" + Constants.TMDBConstants.API_KEY_QUERY_PARAM
                + Constants.TMDBConstants.API_KEY;;
        final List<MovieModel> topRatedMovies = fetchMovies(topRatedMoviesStr);
        final String upcomingMoviesStr = Constants.TMDBConstants.BASE_URL + Constants.TMDBConstants.MOVIE_TAG + "/"
                + Constants.TMDBConstants.REQUEST_UPCOMING_MOVIES + "?" + Constants.TMDBConstants.API_KEY_QUERY_PARAM
                + Constants.TMDBConstants.API_KEY;;
        final List<MovieModel> upcomingMovies = fetchMovies(upcomingMoviesStr);
        final String nowInTheatersMoviesStr = Constants.TMDBConstants.BASE_URL + Constants.TMDBConstants.MOVIE_TAG + "/"
                + Constants.TMDBConstants.REQUEST_MOVIES_IN_THEATERS + "?" + Constants.TMDBConstants.API_KEY_QUERY_PARAM
                + Constants.TMDBConstants.API_KEY;;
        final List<MovieModel> nowInTheatersMovies = fetchMovies(nowInTheatersMoviesStr);

        // delete old data so we don't build up an endless history
        context.getContentResolver().delete(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.CONTENT_URI,null,null);
        context.getContentResolver().delete(FABSContract.POPULAR_MOVIES_TABLE.CONTENT_URI,null,null);
        context.getContentResolver().delete(FABSContract.TOP_RATED_MOVIES_TABLE.CONTENT_URI,null,null);
        context.getContentResolver().delete(FABSContract.UPCOMING_MOVIES_TABLE.CONTENT_URI,null,null);

        Vector<ContentValues> popularMoviesVector = new Vector<ContentValues>(popularMovies.size());

        for(int i = 0; i < popularMovies.size(); i++){
            ContentValues movieValues = new ContentValues();
            String id = popularMovies.get(i).getId();
            String title = popularMovies.get(i).getTitle();
            String overview = popularMovies.get(i).getOverview();
            String poster_image = popularMovies.get(i).getPosterPath();
            String release_date = popularMovies.get(i).getReleaseDate();
            Double popularity = popularMovies.get(i).getPopularity();
            Double vote_average = popularMovies.get(i).getVoteAverage();

            movieValues.put(FABSContract.POPULAR_MOVIES_TABLE._ID, Integer.valueOf(id));
            movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_TITLE, title);
            movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_OVERVIEW, overview);
            movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POSTER_IMAGE, poster_image);
            movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_RELEASE_DATE, release_date);
            movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY, popularity);
            movieValues.put(FABSContract.POPULAR_MOVIES_TABLE.COLUMN_VOTE_AVERAGE, vote_average);

            popularMoviesVector.add(movieValues);
        }

        // add popular movies to database
        if ( popularMoviesVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[popularMoviesVector.size()];
            popularMoviesVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(FABSContract.POPULAR_MOVIES_TABLE.CONTENT_URI, cvArray);
        }

        Vector<ContentValues>topRatedMoviesVector = new Vector<ContentValues>(topRatedMovies.size());

        for(int i = 0; i < topRatedMovies.size(); i++){
            ContentValues movieValues = new ContentValues();
            String id = topRatedMovies.get(i).getId();
            String title = topRatedMovies.get(i).getTitle();
            String overview = topRatedMovies.get(i).getOverview();
            String poster_image = topRatedMovies.get(i).getPosterPath();
            String release_date = topRatedMovies.get(i).getReleaseDate();
            Double popularity = topRatedMovies.get(i).getPopularity();
            Double vote_average = topRatedMovies.get(i).getVoteAverage();

            movieValues.put(FABSContract.TOP_RATED_MOVIES_TABLE._ID, Integer.valueOf(id));
            movieValues.put(FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_TITLE, title);
            movieValues.put(FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_OVERVIEW, overview);
            movieValues.put(FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_POSTER_IMAGE, poster_image);
            movieValues.put(FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_RELEASE_DATE, release_date);
            movieValues.put(FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_POPULARITY, popularity);
            movieValues.put(FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_VOTE_AVERAGE, vote_average);

            topRatedMoviesVector.add(movieValues);
        }

        // add top rated movies to database
        if ( topRatedMoviesVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[topRatedMoviesVector.size()];
            topRatedMoviesVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(FABSContract.TOP_RATED_MOVIES_TABLE.CONTENT_URI, cvArray);
        }

        Vector<ContentValues>upcomingMoviesVector = new Vector<ContentValues>(upcomingMovies.size());

        for(int i = 0; i < upcomingMovies.size(); i++){
            ContentValues movieValues = new ContentValues();
            String id = upcomingMovies.get(i).getId();
            String title = upcomingMovies.get(i).getTitle();
            String overview = upcomingMovies.get(i).getOverview();
            String poster_image = upcomingMovies.get(i).getPosterPath();
            String release_date = upcomingMovies.get(i).getReleaseDate();
            Double popularity = upcomingMovies.get(i).getPopularity();
            Double vote_average = upcomingMovies.get(i).getVoteAverage();

            movieValues.put(FABSContract.UPCOMING_MOVIES_TABLE._ID, Integer.valueOf(id));
            movieValues.put(FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_TITLE, title);
            movieValues.put(FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_OVERVIEW, overview);
            movieValues.put(FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_POSTER_IMAGE, poster_image);
            movieValues.put(FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_RELEASE_DATE, release_date);
            movieValues.put(FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_POPULARITY, popularity);
            movieValues.put(FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_VOTE_AVERAGE, vote_average);

            upcomingMoviesVector.add(movieValues);
        }

        // add upcoming movies to database
        if ( upcomingMoviesVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[upcomingMoviesVector.size()];
            upcomingMoviesVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(FABSContract.UPCOMING_MOVIES_TABLE.CONTENT_URI, cvArray);
        }

        Vector<ContentValues>nowInTheatersMoviesVector = new Vector<ContentValues>(nowInTheatersMovies.size());

        for(int i = 0; i < upcomingMovies.size(); i++){
            ContentValues movieValues = new ContentValues();
            String id = nowInTheatersMovies.get(i).getId();
            String title = nowInTheatersMovies.get(i).getTitle();
            String overview = nowInTheatersMovies.get(i).getOverview();
            String poster_image = nowInTheatersMovies.get(i).getPosterPath();
            String release_date = nowInTheatersMovies.get(i).getReleaseDate();
            Double popularity = nowInTheatersMovies.get(i).getPopularity();
            Double vote_average = nowInTheatersMovies.get(i).getVoteAverage();

            movieValues.put(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE._ID, Integer.valueOf(id));
            movieValues.put(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_TITLE, title);
            movieValues.put(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_OVERVIEW, overview);
            movieValues.put(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_POSTER_IMAGE, poster_image);
            movieValues.put(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_RELEASE_DATE, release_date);
            movieValues.put(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_POPULARITY, popularity);
            movieValues.put(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_VOTE_AVERAGE, vote_average);

            nowInTheatersMoviesVector.add(movieValues);
        }

        // add now in theaters movies to database
        if ( nowInTheatersMoviesVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[nowInTheatersMoviesVector.size()];
            nowInTheatersMoviesVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.CONTENT_URI, cvArray);
        }
    }

    private static List<MovieModel> fetchMovies(String urlStr){
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonStr = null;

        try {
            URL url = new URL(urlStr);
            // Create the request to TMDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonStr = buffer.toString();
            return getDataFromJson(jsonStr);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private static List<MovieModel> getDataFromJson(String jsonStr) throws JSONException{
        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray movieArray = movieJson.getJSONArray(RESULTS);

        List<MovieModel> movies = new ArrayList<MovieModel>();
        for(int i = 0; i < movieArray.length(); i++){
            // Get the JSON object representing the movie
            JSONObject movieJsonObject = movieArray.getJSONObject(i);
            MovieModel movieModel = new MovieModel(movieJsonObject);
            movies.add(movieModel);
        }
        return movies;
    }

    // Searches for movies in the online movie database
    public static class SearchOnlineMoviesTask extends AsyncTask<String, Void, List<MovieModel>> {

        public final String LOG_TAG = SearchOnlineMoviesTask.class.getSimpleName();

        private Context mContext;

        public SearchOnlineMoviesTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected List<MovieModel> doInBackground(String... searchQuery) {
            String urlStr = Constants.TMDBConstants.BASE_URL + Constants.TMDBConstants.SEARCH_TAG + "/"
                    + Constants.TMDBConstants.MOVIE_TAG + "?" + Constants.TMDBConstants.API_KEY_QUERY_PARAM
                    + Constants.TMDBConstants.API_KEY + "&" + Constants.TMDBConstants.QUERY_PARAM + searchQuery;
            return fetchMovies(urlStr);
        }

    }

    // Retrieves a movie's details from the online movie database
    public static class FetchMovieDetailsTask extends AsyncTask<String, Void, MovieModel> {

        public final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();

        private Context mContext;

        public FetchMovieDetailsTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected MovieModel doInBackground(String... movieID) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr = null;

            String urlStr = Constants.TMDBConstants.BASE_URL + Constants.TMDBConstants.MOVIE_TAG + "/" + movieID
                    + "?" + Constants.TMDBConstants.API_KEY_QUERY_PARAM + Constants.TMDBConstants.API_KEY;
            ;

            try {
                URL url = new URL(urlStr);
                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                JSONObject movieJson = new JSONObject(jsonStr);
                MovieModel movieModel = new MovieModel(movieJson);
                return movieModel;
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
