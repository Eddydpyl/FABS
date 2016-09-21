package com.eduardo.fabs.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.presenter.miscellany.RecyclerItemClickListener;
import com.eduardo.fabs.model.fetch.FetchMovies;
import com.eduardo.fabs.model.MovieModel;
import com.eduardo.fabs.view.movies.DiscoverMoviesActivity;
import com.eduardo.fabs.view.movies.MovieDetailsActivity;
import com.eduardo.fabs.view.movies.MovieViewHolder;
import com.eduardo.fabs.view.movies.MyMoviesActivity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchResultsActivity extends AppCompatActivity {

    private String QUERY;
    private String TAG;
    private String SORT_ORDER;

    private static RecyclerView recyclerView;
    private static TextView emptyCursorTextView;
    private static List<MovieModel> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_results);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        emptyCursorTextView = (TextView) findViewById(R.id.empty_cursor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter<MovieViewHolder> adapter = new RecyclerView.Adapter<MovieViewHolder>(){

            @Override
            public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies, parent, false);
                return new MovieViewHolder(view);
            }

            @Override
            public void onBindViewHolder(MovieViewHolder holder, int position) {
                try {
                    MovieViewHolder.populateMovieModel(SearchResultsActivity.this, holder, movies.get(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int getItemCount() {
                return movies.size();
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.ID);
                String ID = textView.getText().toString();
                Intent intent = new Intent(SearchResultsActivity.this, MovieDetailsActivity.class);
                intent.putExtra(getString(R.string.intent_movie_id), ID);
                intent.putExtra(getString(R.string.intent_activity), TAG);
                intent.putExtra(getString(R.string.intent_sort_order), SORT_ORDER);
                intent.putExtra(getString(R.string.intent_query), QUERY);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        try {
            handleIntent(getIntent());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(movies.isEmpty()){
            emptyCursorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            handleIntent(intent);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleIntent(Intent intent) throws ExecutionException, InterruptedException {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            QUERY = intent.getStringExtra(SearchManager.QUERY);
            TAG = intent.getStringExtra(getString(R.string.intent_activity));
            SORT_ORDER = intent.getStringExtra(getString(R.string.intent_sort_order));
        } else {
            QUERY = intent.getStringExtra(getString(R.string.intent_query));
            TAG = intent.getStringExtra(getString(R.string.intent_activity));
            SORT_ORDER = intent.getStringExtra(getString(R.string.intent_sort_order));
        }
        movies = new FetchMovies.SearchOnlineMoviesTask().execute(QUERY).get();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent resIntent = new Intent();
            resIntent.putExtra(getString(R.string.intent_sort_order), SORT_ORDER);
            switch (TAG){
                case MyMoviesActivity.TAG:
                    resIntent.setClass(this, MyMoviesActivity.class);
                    startActivity(resIntent);
                    break;
                case DiscoverMoviesActivity.TAG:
                    resIntent.setClass(this, DiscoverMoviesActivity.class);
                    startActivity(resIntent);
                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
