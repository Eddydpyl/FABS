package com.eduardo.fabs;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eduardo.fabs.movies.DiscoverMoviesActivity;
import com.eduardo.fabs.movies.MyMoviesActivity;

import java.util.concurrent.ExecutionException;

public class SearchResultsActivity extends AppCompatActivity {

    private String QUERY;
    private String TAG;
    private Integer STATE;
    private String SORT_ORDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_results);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        try {
            handleIntent(getIntent());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            STATE = intent.getIntExtra(getString(R.string.intent_fragment), 0);
            SORT_ORDER = intent.getStringExtra(getString(R.string.intent_sort_order));
            //TODO: use query, origin and state to search your data
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent resIntent = new Intent();
            resIntent.putExtra(getString(R.string.intent_fragment), STATE);
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
