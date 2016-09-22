package com.eduardo.fabs.view.movies;

import android.content.Context;
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
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eduardo.fabs.Constants;
import com.eduardo.fabs.R;
import com.eduardo.fabs.model.data.FABSContract;
import com.eduardo.fabs.presenter.miscellany.CursorRecyclerAdapter;
import com.eduardo.fabs.presenter.miscellany.EndlessRecyclerViewScrollListener;
import com.eduardo.fabs.presenter.miscellany.RecyclerItemClickListener;

import java.io.IOException;

import static com.eduardo.fabs.presenter.MovieFragmentMethods.filterCursorRecycler;
import static com.eduardo.fabs.presenter.MovieFragmentMethods.launchMovieDetailsActivity;
import static com.eduardo.fabs.presenter.MovieFragmentMethods.launchSearchActivity;
import static com.eduardo.fabs.presenter.MovieFragmentMethods.loadMoreMovies;

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
                    launchMovieDetailsActivity(view, context);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    String request = Constants.TMDBConstants.REQUEST_POPULAR;
                    Uri contentUri = FABSContract.POPULAR_MOVIES_TABLE.CONTENT_URI;
                    String prefString = getString(R.string.pref_pages_loaded_popular_movies);
                    loadMoreMovies(page, request, contentUri, prefString, context);
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
        final Uri uri = FABSContract.POPULAR_MOVIES_TABLE.CONTENT_URI;
        String selection = FABSContract.POPULAR_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.POPULAR_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
        filterCursorRecycler(cursorRecyclerAdapter, emptyCursorTextView, query, uri, selection, POPULAR_MOVIES_COLUMNS, getContext());
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        launchSearchActivity(query, getContext());
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
