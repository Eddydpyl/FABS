package com.eduardo.fabs.movies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eduardo.fabs.R;
import com.eduardo.fabs.adapters.CursorRecyclerAdapter;
import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.utils.UserCategory;

public class PlanToWatchMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // Each loader in an activity needs a different ID
    private static final int PLANTOWATCHMOVIES_LOADER = 2;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private CursorRecyclerAdapter cursorRecyclerAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public PlanToWatchMoviesFragment() {
    }

    public static PlanToWatchMoviesFragment newInstance(int columnCount) {
        PlanToWatchMoviesFragment fragment = new PlanToWatchMoviesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        MyMoviesActivity.setState(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plantowatchmovies_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // We initialize the cursorRecyclerAdapter without a cursor, so we can set it as the recyclerView's adapter
            cursorRecyclerAdapter = new CursorRecyclerAdapter(null) {
                @Override
                public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
                    // TODO: Fill view with data from cursor
                    ((MovieViewHolder) holder).mContentView.setText(cursor.getString(MyMoviesActivity.COL_MY_MOVIES_OVERVIEW));
                }

                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // TODO: Create layout for movies in fragment_mymovies
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mymovies, parent, false);
                    return new MovieViewHolder(view);
                }
            };
            recyclerView.setAdapter(cursorRecyclerAdapter);
        }
        return view;
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
        // TODO: Use sort order specified by the user
        String sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_POPULARITY;
        Uri uri = FABSContract.MY_MOVIES_TABLE.buildMovieUriWithUserCategory(UserCategory.PLANTOWATCH);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, sortOrder);
        return new CursorLoader(getActivity(), uri, MyMoviesActivity.MY_MOVIES_COLUMNS, null, null, sortOrder);
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
