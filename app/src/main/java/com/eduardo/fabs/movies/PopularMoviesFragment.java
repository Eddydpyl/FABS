package com.eduardo.fabs.movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.SearchResultsActivity;
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

public class PopularMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    // Each loader in an activity needs a different ID
    private static final int POPULARMOVIES_LOADER = 0;
    private CursorRecyclerAdapter cursorRecyclerAdapter;
    private TextView emptyCursorTextView;

    public static final String[] POPULAR_MOVIES_COLUMNS = {
            FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.POPULAR_MOVIES_TABLE._ID,
            FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POSTER_IMAGE,
            FABSContract.POPULAR_MOVIES_TABLE.COLUMN_OVERVIEW,
            FABSContract.POPULAR_MOVIES_TABLE.COLUMN_RELEASE_DATE,
            FABSContract.POPULAR_MOVIES_TABLE.COLUMN_TITLE,
            FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY,
            FABSContract.POPULAR_MOVIES_TABLE.COLUMN_VOTE_AVERAGE
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public PopularMoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getContext().getString(R.string.title_fragment_popular_movies));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discovermovies, container, false);
        View view = rootView.findViewById(R.id.recyclerView);
        emptyCursorTextView = (TextView) rootView.findViewById(R.id.empty_cursor);
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
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies, parent, false);
                    return new MovieViewHolder(view);
                }
            };
            recyclerView.setAdapter(cursorRecyclerAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.ID);
                    String ID = textView.getText().toString();
                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                    intent.putExtra(getString(R.string.intent_movie_id), ID);
                    intent.putExtra(getString(R.string.intent_activity), DiscoverMoviesActivity.TAG);
                    intent.putExtra(getString(R.string.intent_fragment), 0);
                    intent.putExtra(getString(R.string.intent_sort_order), DiscoverMoviesActivity.sortOrder);
                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if(page > 100 || DiscoverMoviesActivity.searching){
                        // The online database has only up to 100 pages at any time and we don't want to load any more while searching
                        return;
                    }
                    try {
                        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPref.edit();
                        edit.putInt(getString(R.string.pref_pages_loaded_popular_movies), page);
                        edit.commit();

                        List<MovieModel> popularMovies = new FetchMovies.FetchMoreMoviesTask(Constants.TMDBConstants.REQUEST_POPULAR).execute(page).get();
                        Vector<ContentValues> popularMoviesVector = new Vector<ContentValues>(popularMovies.size());

                        for (int i = 0; i < popularMovies.size(); i++) {
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
                        if (popularMoviesVector.size() > 0) {
                            ContentValues[] cvArray = new ContentValues[popularMoviesVector.size()];
                            popularMoviesVector.toArray(cvArray);
                            context.getContentResolver().bulkInsert(FABSContract.POPULAR_MOVIES_TABLE.CONTENT_URI, cvArray);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
            SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            Integer previouslyStarted = sharedPref.getInt(getString(R.string.pref_pages_loaded_popular_movies), 1);
            endlessRecyclerViewScrollListener.setCurrentPage(previouslyStarted);
            recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        DiscoverMoviesActivity.searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic
        FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                final Uri uri = FABSContract.POPULAR_MOVIES_TABLE.CONTENT_URI;
                String selection = FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.POPULAR_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                String[] selectionArgs = {charSequence.toString() + "%"};
                return getActivity().getContentResolver().query(uri, PopularMoviesFragment.POPULAR_MOVIES_COLUMNS, selection, selectionArgs, DiscoverMoviesActivity.sortOrder);

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
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(getContext(), SearchResultsActivity.class);
        intent.putExtra(getString(R.string.intent_query), query);
        intent.putExtra(getString(R.string.intent_activity), DiscoverMoviesActivity.TAG);
        intent.putExtra(getString(R.string.intent_sort_order), DiscoverMoviesActivity.sortOrder);
        startActivity(intent);
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(POPULARMOVIES_LOADER, null, this);
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
        Uri uri = FABSContract.POPULAR_MOVIES_TABLE.CONTENT_URI;
        return new CursorLoader(getActivity(), uri, POPULAR_MOVIES_COLUMNS, null, null, DiscoverMoviesActivity.sortOrder);
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
