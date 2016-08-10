package com.eduardo.fabs.movies;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.adapters.CursorRecyclerAdapter;
import com.eduardo.fabs.adapters.RecyclerItemClickListener;
import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.utils.UserCategory;

import java.io.IOException;

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
        getActivity().setTitle(getContext().getString(R.string.title_fragment_plan_to_watch_movies));

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
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mymovies, parent, false);
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
                    intent.putExtra(getString(R.string.intent_activity), MyMoviesActivity.TAG);
                    intent.putExtra(getString(R.string.intent_fragment), 2);
                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PLANTOWATCHMOVIES_LOADER, null, this);
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
        Uri uri = FABSContract.MY_MOVIES_TABLE.buildMovieUriWithUserCategory(UserCategory.PLANTOWATCH);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, MyMoviesActivity.sortOrder);
        return new CursorLoader(getActivity(), uri, MyMoviesActivity.MY_MOVIES_COLUMNS, null, null, MyMoviesActivity.sortOrder);
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
