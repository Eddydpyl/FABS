package com.eduardo.fabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eduardo.fabs.movies.DiscoverMoviesActivity;
import com.eduardo.fabs.movies.MyMoviesActivity;

//TODO: Write Donate Activity

public class DonateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_donate);
        setSupportActionBar(toolbar);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = getIntent();
            String origin = intent.getStringExtra(getString(R.string.intent_activity));
            Integer state = intent.getIntExtra(getString(R.string.intent_fragment), 0);
            Intent resIntent = new Intent();
            resIntent.putExtra(getString(R.string.intent_fragment), state);
            switch (origin){
                case MyMoviesActivity.TAG:
                    resIntent.setClass(this, MyMoviesActivity.class);
                    startActivity(resIntent);
                case DiscoverMoviesActivity.TAG:
                    resIntent.setClass(this, DiscoverMoviesActivity.class);
                    startActivity(resIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
