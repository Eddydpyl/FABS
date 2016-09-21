package com.eduardo.fabs.view.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eduardo.fabs.R;
import com.eduardo.fabs.view.SearchResultsActivity;

public class MovieDetailsActivity extends AppCompatActivity {

    private static String TAG;
    private static String SORT_ORDER;
    private static String QUERY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_moviedetails);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        TAG = intent.getStringExtra(getString(R.string.intent_activity));
        SORT_ORDER = intent.getStringExtra(getString(R.string.intent_sort_order));
        QUERY = intent.getStringExtra(getString(R.string.intent_query));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent resIntent = new Intent();
            resIntent.putExtra(getString(R.string.intent_sort_order), SORT_ORDER);
            if (QUERY != null) {
                resIntent.putExtra(getString(R.string.intent_activity), TAG);
                resIntent.putExtra(getString(R.string.intent_query), QUERY);
                resIntent.setClass(this, SearchResultsActivity.class);
                startActivity(resIntent);
                return true;
            }
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
