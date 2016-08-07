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

public class NowInTheatersMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // Each loader in an activity needs a different ID
    private static final int NOWINTHEATERSMOVIES_LOADER = 3;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private CursorRecyclerAdapter cursorRecyclerAdapter;

    public static final String[] NOW_IN_THEATERS_MOVIES_COLUMNS = {
            FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.NOW_IN_THEATERS_MOVIES_TABLE._ID,
            FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_POSTER_IMAGE,
            FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_OVERVIEW,
            FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_RELEASE_DATE,
            FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_TITLE,
            FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_POPULARITY,
            FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_VOTE_AVERAGE
    };

    static final int COL_NOW_IN_THEATERS_MOVIES_ID = 0;
    static final int COL_NOW_IN_THEATERS_MOVIES_POSTER_IMAGE = 1;
    static final int COL_NOW_IN_THEATERS_MOVIES_OVERVIEW= 2;
    static final int COL_NOW_IN_THEATERS_MOVIES_RELEASE_DATE = 3;
    static final int COL_NOW_IN_THEATERS_MOVIES_TITLE = 4;
    static final int COL_NOW_IN_THEATERS_MOVIES_POPULARITY = 5;
    static final int COL_NOW_IN_THEATERS_MOVIES_VOTE_AVERAGE = 6;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public NowInTheatersMoviesFragment() {
    }

    public static NowInTheatersMoviesFragment newInstance(int columnCount) {
        NowInTheatersMoviesFragment fragment = new NowInTheatersMoviesFragment();
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

        DiscoverMoviesActivity.setState(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nowintheatersmovies_list, container, false);

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
                    ((MovieViewHolder) holder).mContentView.setText(cursor.getString(COL_NOW_IN_THEATERS_MOVIES_OVERVIEW));
                }

                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // TODO: Create layout for movies in fragment_discovermovies
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_discovermovies, parent, false);
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
        String sortOrder = FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_POPULARITY;
        Uri uri = FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, sortOrder);
        return new CursorLoader(getActivity(), uri, NOW_IN_THEATERS_MOVIES_COLUMNS, null, null, sortOrder);
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