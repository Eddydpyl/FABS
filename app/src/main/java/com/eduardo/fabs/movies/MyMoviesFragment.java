package com.eduardo.fabs.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.eduardo.fabs.adapters.CursorRecyclerAdapter;
import com.eduardo.fabs.adapters.RecyclerItemClickListener;
import com.eduardo.fabs.adapters.SwipeToDeleteCursorWrapper;
import com.eduardo.fabs.adapters.SwipeableRecyclerViewTouchListener;
import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.utils.UserCategory;

import java.io.IOException;

public class MyMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener{

    // Each loader in an activity needs a different ID
    private static int loader;
    private static CursorRecyclerAdapter cursorRecyclerAdapter;
    private TextView emptyCursorTextView;

    private final static String SELECTION = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE._ID + " = ? ";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public MyMoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        loader = prefs.getInt(getString(R.string.pref_my_movies_state),0);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mymovies, container, false);
        View view = rootView.findViewById(R.id.recyclerView);
        emptyCursorTextView = (TextView) rootView.findViewById(R.id.empty_cursor);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
                    intent.putExtra(getString(R.string.intent_activity), MyMoviesActivity.TAG);
                    intent.putExtra(getString(R.string.intent_fragment), loader);
                    intent.putExtra(getString(R.string.intent_sort_order), MyMoviesActivity.sortOrder);
                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            recyclerView.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(recyclerView, new SwipeableRecyclerViewTouchListener.SwipeListener() {
                @Override
                public boolean canSwipeLeft(int position) {
                    return false;
                }

                @Override
                public boolean canSwipeRight(int position) {
                    return true;
                }

                @Override
                public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {

                }

                @Override
                public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                    TextView textView = (TextView) recyclerView.getChildAt(reverseSortedPositions[0]).findViewById(R.id.ID);
                    String ID = textView.getText().toString();
                    // This wrapper deals with the flicker that takes place when swiping for delete
                    SwipeToDeleteCursorWrapper cursorWrapper = new SwipeToDeleteCursorWrapper(cursorRecyclerAdapter.getCursor(), reverseSortedPositions[0]);
                    cursorRecyclerAdapter.swapCursor(cursorWrapper);
                    getActivity().getContentResolver().delete(FABSContract.MY_MOVIES_TABLE.CONTENT_URI, SELECTION, new String[] {ID});
                }
            }));
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_mymovies).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic
        FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                final Uri uri = FABSContract.MY_MOVIES_TABLE.CONTENT_URI;
                switch (loader){
                    case 0: {
                        String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                        String[] selectionArgs = {charSequence.toString() + "%"};
                        return getActivity().getContentResolver().query(uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
                    }
                    case 1: {
                        String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY + " = ? AND " + FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                        String[] selectionArgs = {UserCategory.COMPLETED.toString(), charSequence.toString() + "%"};
                        return getActivity().getContentResolver().query(uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
                    }
                    case 2: {
                        String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY + " = ? AND " + FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                        String[] selectionArgs = {UserCategory.PLANTOWATCH.toString(), charSequence.toString() + "%"};
                        return getActivity().getContentResolver().query(uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
                    }
                    default:
                        String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE + " LIKE ? ";
                        String[] selectionArgs = {charSequence.toString() + "%"};
                        return getActivity().getContentResolver().query(uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
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
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(loader, null, this);
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
        final Uri uri = FABSContract.MY_MOVIES_TABLE.CONTENT_URI;
        switch (loader){
            case 0:
                return new CursorLoader(getActivity(), uri, MyMoviesActivity.MY_MOVIES_COLUMNS, null, null, MyMoviesActivity.sortOrder);
            case 1: {
                String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY + " = ? ";
                String[] selectionArgs = {UserCategory.COMPLETED.toString()};
                return new CursorLoader(getActivity(), uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
            }
            case 2: {
                String selection = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY + " = ? ";
                String[] selectionArgs = {UserCategory.PLANTOWATCH.toString()};
                return new CursorLoader(getActivity(), uri, MyMoviesActivity.MY_MOVIES_COLUMNS, selection, selectionArgs, MyMoviesActivity.sortOrder);
            }
            default:
                return new CursorLoader(getActivity(), uri, MyMoviesActivity.MY_MOVIES_COLUMNS, null, null, MyMoviesActivity.sortOrder);
        }
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
