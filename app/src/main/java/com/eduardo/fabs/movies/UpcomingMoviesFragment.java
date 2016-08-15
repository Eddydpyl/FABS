package com.eduardo.fabs.movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.adapters.CursorRecyclerAdapter;
import com.eduardo.fabs.adapters.EndlessRecyclerViewScrollListener;
import com.eduardo.fabs.adapters.RecyclerItemClickListener;
import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.fetch.FetchMovies;
import com.eduardo.fabs.models.MovieModel;
import com.eduardo.fabs.utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class UpcomingMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // Each loader in an activity needs a different ID
    private static final int UPCOMINGMOVIES_LOADER = 2;
    private CursorRecyclerAdapter cursorRecyclerAdapter;

    public static final String[] UPCOMING_MOVIES_COLUMNS = {
            FABSContract.UPCOMING_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.UPCOMING_MOVIES_TABLE._ID,
            FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_POSTER_IMAGE,
            FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_OVERVIEW,
            FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_RELEASE_DATE,
            FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_TITLE,
            FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_POPULARITY,
            FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_VOTE_AVERAGE
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public UpcomingMoviesFragment() {
    }

    public static UpcomingMoviesFragment newInstance() {
        UpcomingMoviesFragment fragment = new UpcomingMoviesFragment();
        DiscoverMoviesActivity.sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_RELEASE_DATE;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getContext().getString(R.string.title_fragment_upcoming_movies));
        DiscoverMoviesActivity.setState(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcomingmovies_list, container, false);

        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            // We initialize the cursorRecyclerAdapter without a cursor, so we can set it as the recyclerView's adapter
            cursorRecyclerAdapter = new CursorRecyclerAdapter<MovieViewHolder>(null) {
                @Override
                public void onBindViewHolderCursor(MovieViewHolder holder, Cursor cursor) {
                    try {
                        MovieViewHolder.populateMovieModel(getContext(), holder, cursor);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_discovermovies, parent, false);
                    return new MovieViewHolder(view);
                }
            };
            recyclerView.setAdapter(cursorRecyclerAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener(){

                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.ID);
                    String ID = textView.getText().toString();
                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                    intent.putExtra(getString(R.string.intent_movie_id), ID);
                    intent.putExtra(getString(R.string.intent_activity), DiscoverMoviesActivity.TAG);
                    intent.putExtra(getString(R.string.intent_fragment), 2);
                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if(page > 100){
                        // The online database has only up to 100 pages at any time
                        return;
                    }
                    try {
                        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPref.edit();
                        edit.putInt(getString(R.string.pref_pages_loaded_upcoming_movies), page);
                        edit.commit();

                        List<MovieModel> upcomingMovies = new FetchMovies.FetchMoreMoviesTask(context, Constants.TMDBConstants.REQUEST_UPCOMING_MOVIES).execute(page).get();
                        Vector<ContentValues> upcomingMoviesVector = new Vector<ContentValues>(upcomingMovies.size());

                        for (int i = 0; i < upcomingMovies.size(); i++) {
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
                        if (upcomingMoviesVector.size() > 0) {
                            ContentValues[] cvArray = new ContentValues[upcomingMoviesVector.size()];
                            upcomingMoviesVector.toArray(cvArray);
                            context.getContentResolver().bulkInsert(FABSContract.UPCOMING_MOVIES_TABLE.CONTENT_URI, cvArray);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
            SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            Integer previouslyStarted = sharedPref.getInt(getString(R.string.pref_pages_loaded_upcoming_movies), 1);
            endlessRecyclerViewScrollListener.setCurrentPage(previouslyStarted);
            recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(UPCOMINGMOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // We create the cursor that our adapter will use, but we don't assign it here
        Uri uri = FABSContract.UPCOMING_MOVIES_TABLE.CONTENT_URI;
        return new CursorLoader(getActivity(), uri, UPCOMING_MOVIES_COLUMNS, null, null, DiscoverMoviesActivity.sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Here cursorRecyclerAdapter receives the cursor it needs to work
        cursorRecyclerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorRecyclerAdapter.swapCursor(null);
    }
}
